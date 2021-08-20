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
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.container.Container
import net.minecraft.inventory.container.ContainerType
import net.minecraft.inventory.container.IContainerListener
import net.minecraft.inventory.container.Slot
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.items.wrapper.PlayerInvWrapper
import javax.annotation.OverridingMethodsMustInvokeSuper

abstract class AbstractContainer<T>(type: ContainerType<*>?, id: Int, protected val playerInventory: PlayerInventory, protected val bindInventory: Boolean, host: Any) : Container(type, id) {
    
    val lockedSlots = mutableSetOf<Int>()
    
    val slotsByType = mutableMapOf<SlotType, MutableList<Slot>>()
    
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
        
        slotsByType.getOrDefault(slotType, mutableListOf()).add(slot)
        
        return slot
    }
    
    fun getSlotsForType(slotType: SlotType): List<Slot> {
        return slotsByType.getOrDefault(slotType, mutableListOf()).toList()
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
    
    protected fun bindPlayerInventory(posX: Int, posY: Int) {
        
        val itemHandler = PlayerInvWrapper(playerInventory)
        
        for (i in 0 until playerInventory.sizeInventory) {
            val slot = if (lockedSlots.contains(i)) {
                DisabledSlot(itemHandler, i)
            } else {
                Slot(playerInventory, i, 0, 0)
            }
            
            val type = if (i < PlayerInventory.getHotbarSize()) {
                SlotType.PlayerHotbar
            } else {
                SlotType.PlayerInventory
            }
            
            addSlot(slot, type)
        }
    }
}