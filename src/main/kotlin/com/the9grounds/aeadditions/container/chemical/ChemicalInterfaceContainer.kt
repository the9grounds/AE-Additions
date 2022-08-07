package com.the9grounds.aeadditions.container.chemical

import appeng.api.config.SecurityPermissions
import com.the9grounds.aeadditions.api.IAEAChemicalConfig
import com.the9grounds.aeadditions.api.IAEAHasChemicalConfig
import com.the9grounds.aeadditions.api.ICombinedChemicalContainer
import com.the9grounds.aeadditions.api.container.IChemicalSyncContainer
import com.the9grounds.aeadditions.container.AbstractContainer
import com.the9grounds.aeadditions.container.ContainerTypeBuilder
import com.the9grounds.aeadditions.container.slot.DisabledSlot
import com.the9grounds.aeadditions.integration.mekanism.Mekanism
import com.the9grounds.aeadditions.network.NetworkManager
import com.the9grounds.aeadditions.network.packets.ChemicalConfigPacket
import com.the9grounds.aeadditions.network.packets.ChemicalInterfaceContentsChangedPacket
import com.the9grounds.aeadditions.tile.ChemicalInterfaceTileEntity
import mekanism.api.chemical.Chemical
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.inventory.container.ContainerType
import net.minecraft.inventory.container.IContainerListener
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.text.StringTextComponent

class ChemicalInterfaceContainer(
    type: ContainerType<*>?,
    id: Int,
    playerInventory: PlayerInventory,
    host: Any
) : AbstractContainer<ChemicalInterfaceContainer>(type, id, playerInventory, false, host), IChemicalSyncContainer {
    
    init {
        bindPlayerInventory(7, 148)
    }
    
    var chemicalTankList = ChemicalTankList(6)
    
    companion object {
        val CHEMICAL_INTERFACE = ContainerTypeBuilder(ChemicalInterfaceTileEntity::class) { containerType: ContainerType<ChemicalInterfaceContainer>, windowId: Int, playerInventory: PlayerInventory?, chemicalInterfaceTileEntity: ChemicalInterfaceTileEntity -> 
            ChemicalInterfaceContainer(containerType, windowId, playerInventory!!, chemicalInterfaceTileEntity)
        }.requirePermission(SecurityPermissions.BUILD).apply { titleComponent = StringTextComponent("Chemical Interface") }.build("chemical_interface")
    }
    
    var chemicalList = arrayOf<Chemical<*>?>()

    var gui: ICombinedChemicalContainer? = null

    fun getChemicalConfig(): IAEAChemicalConfig = (tileEntity as IAEAHasChemicalConfig).getChemicalConfig()
    
    fun onChemicalTankListChanged(chemicalTankList: ChemicalTankList) {
        this.chemicalTankList = chemicalTankList
        
        gui!!.onChemicalListChange()
    }

    override fun receiveChemicals(chemicals: Array<Chemical<*>?>) {
        chemicalList = chemicals

        if (gui != null) {
            gui!!.onChemicalConfigChange()
        }
    }

    override fun setChemicalForSlot(chemical: Chemical<*>?, slot: Int) {
        getChemicalConfig().setChemicalInSlot(slot, chemical)

        sendChemicalListToAllValidListeners()
    }

    override fun transferStackInSlot(player: PlayerEntity, idx: Int): ItemStack {

        if (!isServer) {
            return ItemStack.EMPTY
        }

        val clickSlot = inventorySlots[idx]

        if (clickSlot is DisabledSlot) {
            return ItemStack.EMPTY
        }

        if (clickSlot.hasStack) {
            val tis = clickSlot.stack

            if (tis.isEmpty) {
                return ItemStack.EMPTY
            }

            val chemicalInStack = Mekanism.getStoredChemicalStackFromStack(tis)

            if (chemicalInStack != null) {
                val config = getChemicalConfig()

                for (i in 0 until config.size) {
                    if (config.hasChemicalInSlot(i) && isValidForConfig(i)) {
                        config.setChemicalInSlot(i, chemicalInStack.getType())

                        sendChemicalListToAllValidListeners()

                        break
                    }
                }

                return ItemStack.EMPTY
            }
        }

        detectAndSendChanges()

        return super.transferStackInSlot(player, idx)
    }

    protected fun isValidForConfig(slot: Int): Boolean {
        return true
    }

    override fun detectAndSendChanges() {

        if (isServer) {
            val config = getChemicalConfig()

            var shouldSendPacket = false

            for (i in 0 until config.size) {
                if (config.getChemicalInSlot(i) != null && !isValidForConfig(i)) {
                    config.setChemicalInSlot(i, null)
                    shouldSendPacket = true
                }
            }

            if (shouldSendPacket) {
                sendChemicalListToAllValidListeners()
            }
        }

        super.detectAndSendChanges()
    }

    private fun sendChemicalListToAllValidListeners() {
        for (listener in listeners) {
            if (listener is ServerPlayerEntity) {
                sendChemicalListToPlayer(listener)
            }
        }
    }

    override fun addListener(listener: IContainerListener) {
        super.addListener(listener)

        // Send network packet

        if (listener is ServerPlayerEntity) {
            sendChemicalListToPlayer(listener)
            NetworkManager.sendTo(ChemicalInterfaceContentsChangedPacket((tileEntity!! as ChemicalInterfaceTileEntity).getChemicalTankListToSend()), listener)
        }
    }

    private fun sendChemicalListToPlayer(player: ServerPlayerEntity) {
        NetworkManager.sendTo(ChemicalConfigPacket(windowId, getChemicalConfig().getConfig()), player)
    }
    
    class ChemicalTankList(val size: Int) {
        val chemicalTanks = arrayOfNulls<ChemicalTank>(size)
        
        fun setChemicalTank(index: Int, chemical: Chemical<*>?, amount: Long, capacity: Long) {
            val chemicalTank = ChemicalTank()
            chemicalTank.chemical = chemical
            chemicalTank.amount = amount
            chemicalTank.capacity = capacity
            
            chemicalTanks[index] = chemicalTank
        }
        
        fun readFromNbt(nbt: CompoundNBT) {
            for (i in 0 until size) {
                chemicalTanks[i] = ChemicalTank()
                if (nbt.contains("tank#${i}")) {
                    chemicalTanks[i]!!.readFromNbt(nbt.getCompound("tank#${i}"))
                }
            }
        }
        
        fun writeToNbt(nbt: CompoundNBT) {
            nbt.putInt("size", size)
            chemicalTanks.forEachIndexed { index, chemicalTank -> 
                if (chemicalTank != null && chemicalTank.chemical !== null) {
                    val chemicalTankCompound = CompoundNBT()
                    
                    chemicalTank.writeToNbt(chemicalTankCompound)
                    
                    nbt.put("tank#${index}", chemicalTankCompound)
                }
            }
        }
    }
    
    class ChemicalTank {
        var chemical: Chemical<*>? = null
        var amount: Long = 0L
        var capacity: Long = 0L
        
        fun readFromNbt(nbt: CompoundNBT) {
            if (nbt.contains("chemicalType")) {
                chemical = Mekanism.readChemicalFromNbt(nbt)
                amount = nbt.getLong("amount")
                capacity = nbt.getLong("capacity")
            }
        }
        
        fun writeToNbt(nbt: CompoundNBT) {
            if (chemical == null) return
            nbt.putLong("amount", amount)
            nbt.putLong("capacity", capacity)
            nbt.putString("chemicalType", Mekanism.getType(chemical!!))
            chemical!!.write(nbt)
        }
    }
}