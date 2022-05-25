package com.the9grounds.aeadditions.container

import appeng.api.implementations.guiobjects.IGuiItemObject
import appeng.api.networking.security.IActionHost
import appeng.api.networking.security.IActionSource
import appeng.api.parts.IPart
import appeng.me.helpers.PlayerSource
import com.the9grounds.aeadditions.container.slot.AEASlot
import com.the9grounds.aeadditions.container.slot.DisabledSlot
import com.the9grounds.aeadditions.network.NetworkManager
import com.the9grounds.aeadditions.network.packets.BasePacket
import com.the9grounds.aeadditions.network.packets.GuiDataSyncPacket
import com.the9grounds.aeadditions.sync.gui.DataSync
import com.the9grounds.aeadditions.util.Utils
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.container.Container
import net.minecraft.inventory.container.ContainerType
import net.minecraft.inventory.container.IContainerListener
import net.minecraft.inventory.container.Slot
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.items.wrapper.PlayerInvWrapper
import javax.annotation.OverridingMethodsMustInvokeSuper

abstract class AbstractContainer<T>(type: ContainerType<*>?, id: Int, protected val playerInventory: PlayerInventory, protected val bindInventory: Boolean, host: Any) : Container(type, id) {
    
    val lockedSlots = mutableSetOf<Int>()
    
    val slotsByType = mutableMapOf<SlotType, MutableList<Slot>>()
    val typeBySlot = mutableMapOf<Slot, SlotType>()
    
    val mySrc: IActionSource
    var tileEntity: TileEntity? = null
    var part: IPart? = null
    
    var isValidContainer = true
    
    val dataSync = DataSync<T>(this)
    
    // Wait until portable cell to use
    var guiItem: IGuiItemObject? = null
    
    var locator: Locator? = null
    
    val isClient: Boolean
    get() = playerInventory.player.entityWorld.isRemote
    
    val isServer: Boolean
    get() = !isClient
    
    var posX = 0
    var posY = 0
    
    init {
        tileEntity = host as? TileEntity
        part = host as? IPart
        
        if (tileEntity == null && part == null) {
            throw IllegalArgumentException("Invalid host")
        }
        
        mySrc = PlayerSource(playerInventory.player, getActionHost())
        
        if (bindInventory) {
            bindPlayerInventory(posX, posY)
        }
    }
    
    protected fun getActionHost(): IActionHost? {
        return when(true) {
//            guiItem is IActionHost -> guiItem as IActionHost
            tileEntity is IActionHost -> tileEntity as IActionHost
            part is IActionHost -> part as IActionHost
            else -> null
        }
    }

    override fun canInteractWith(playerIn: PlayerEntity): Boolean {
        if (isValidContainer) {
            if (tileEntity is IInventory) {
                return (tileEntity as IInventory).isUsableByPlayer(playerIn)
            }
            
            return true
        }
        
        return false
    }

    override fun addListener(listener: IContainerListener) {
        super.addListener(listener)
        
        if (listener is ServerPlayerEntity && dataSync.hasFields()) {
            sendPacketToClient(GuiDataSyncPacket(windowId, dataSync::writeFull))
        }
    }
    
    fun receiveServerDataSync(packet: GuiDataSyncPacket) {
        this.dataSync.readUpdate(packet.data!!)
        this.onServerDataSync()
    }
    
    fun addSlot(slotIn: Slot, slotType: SlotType): Slot {
        val slot = addSlot(slotIn)
        
        if (!slotsByType.containsKey(slotType)) {
            slotsByType[slotType] = mutableListOf()
        }
        
        slotsByType[slotType]?.add(slot)
        typeBySlot[slot] = slotType
        
        return slot
    }
    
    fun getSlotsForType(slotType: SlotType): List<Slot> {
        if (!slotsByType.containsKey(slotType)) {
            slotsByType[slotType] = mutableListOf()
        }

        return slotsByType[slotType]!!.toList()
    }

    override fun addSlot(slotIn: Slot): Slot {
        if (slotIn is AEASlot) {
            slotIn.container = this
        }
        
        return super.addSlot(slotIn)
    }

    override fun detectAndSendChanges() {
        if (isServer) {
            if (tileEntity != null && tileEntity!!.world!!.getTileEntity(tileEntity!!.pos) != tileEntity) {
                this.isValidContainer = false
            }
            
            if (dataSync.hasChanges()) {
                sendPacketToClient(GuiDataSyncPacket(windowId, dataSync::writeUpdate))
            }
        }
        
        super.detectAndSendChanges()
    }

    override fun transferStackInSlot(playerIn: PlayerEntity, index: Int): ItemStack {
        if (!isServer) {
            return ItemStack.EMPTY
        }

        val clickSlot = inventorySlots[index]

        if (clickSlot is DisabledSlot) {
            return ItemStack.EMPTY
        }
        
        val isPlayerSideSlot = isPlayerSideSlot(clickSlot)
        
        var slots = inventorySlots
        
        if (isPlayerSideSlot) {
            slots = slots.filter { it -> !isPlayerSideSlot(it) }
        } else {
            slots = slots.filter { it -> isPlayerSideSlot(it) }
        }

        if (clickSlot.hasStack) {
            val tis = clickSlot.stack

            if (tis.isEmpty) {
                return ItemStack.EMPTY
            }
            
            for (slot in slots.reversed()) {
                if (!slot.isItemValid(tis)) {
                    continue
                }
                
                if (!Utils.isItemStackEqual(slot.stack, tis)) {
                    continue
                }
                
                val stack = slot.stack

                var maxSize = stack.maxStackSize

                if (maxSize > getStackLimitForSlot(slot)) {
                    maxSize = getStackLimitForSlot(slot)
                }
                
                val tmp = stack.copy()

                var amountCanPut = maxSize - tmp.count

                if (amountCanPut > tis.count) {
                    amountCanPut = tis.count
                }


                tmp.count = tmp.count + amountCanPut

                slot.putStack(tmp)
                tis.count -= amountCanPut

                if (tis.count == 0) {
                    clickSlot.putStack(ItemStack.EMPTY)

                    this.detectAndSendChanges()

                    return ItemStack.EMPTY
                } else {
                    this.detectAndSendChanges()
                }
            }
            
            if (tis.count > 0) {
                // Find First Empty Slot
                
                for (slot in slots.reversed()) {
                    if (slot.isItemValid(tis) && slot.stack.isEmpty) {
                        var count = tis.count
                        
                        if (count > getStackLimitForSlot(slot)) {
                            count = getStackLimitForSlot(slot)
                        }
                        
                        val tmp = tis.copy()
                        
                        tmp.count = count
                        
                        slot.putStack(tmp)
                        
                        tis.count -= count
                        
                        this.detectAndSendChanges()
                    }
                }
            }
        }
        
        this.detectAndSendChanges()
        return ItemStack.EMPTY
    }
    
    @OverridingMethodsMustInvokeSuper
    protected open fun onServerDataSync() {
        
    }
    
    protected fun sendPacketToClient(packet: BasePacket) {
        for (listener in listeners) {
            if (listener is ServerPlayerEntity) {
                NetworkManager.sendTo(packet, listener)
            }
        }
    }

    private fun isPlayerSideSlot(slot: Slot): Boolean {
        if (slot.inventory === playerInventory) {
            return true
        }
        val slotType: SlotType = typeBySlot.get(slot)!!
        return slotType == SlotType.PlayerInventory || slotType == SlotType.PlayerHotbar || slotType == SlotType.NetworkTool
    }
    
    private fun getStackLimitForSlot(slot: Slot): Int {
        if (slot is AEASlot) {
            return slot.inv.getSlotLimit(slot.slotIndex)
        }
        
        return slot.slotStackLimit
    }
    
    protected fun bindPlayerInventory(posX: Int, posY: Int) {
        
        val itemHandler = PlayerInvWrapper(playerInventory)
        
        var slotX = posX
        var slotY = posY + (18 * 2) + 18 + 4
        
        var isHotBar = true
        
        for (i in 0 until 36) {
            val slot = if (lockedSlots.contains(i)) {
                DisabledSlot(itemHandler, i, slotX + 1, slotY + 1)
            } else {
                Slot(playerInventory, i, slotX + 1, slotY + 1)
            }

            val type = if (i < PlayerInventory.getHotbarSize()) {
                SlotType.PlayerHotbar
            } else {
                SlotType.PlayerInventory
            }

            addSlot(slot, type)

            slotX += 18

            if ((i + 1).rem(9) == 0) {
                if (isHotBar) {
                    slotY -= 4
                    isHotBar = false
                }

                slotY -= 18
                slotX = posX
            }
        }
    }
}