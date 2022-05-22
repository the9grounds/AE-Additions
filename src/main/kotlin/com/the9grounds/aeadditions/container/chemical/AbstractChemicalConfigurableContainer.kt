package com.the9grounds.aeadditions.container.chemical

import appeng.api.config.Upgrades
import com.the9grounds.aeadditions.api.IAEAChemicalConfig
import com.the9grounds.aeadditions.api.IUpgradeableHost
import com.the9grounds.aeadditions.api.client.IChemicalConfigListener
import com.the9grounds.aeadditions.api.container.IChemicalSyncContainer
import com.the9grounds.aeadditions.container.AbstractUpgradableContainer
import com.the9grounds.aeadditions.container.slot.DisabledSlot
import com.the9grounds.aeadditions.integration.mekanism.Mekanism
import com.the9grounds.aeadditions.network.NetworkManager
import com.the9grounds.aeadditions.network.packets.ChemicalConfigPacket
import mekanism.api.chemical.Chemical
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.inventory.container.ContainerType
import net.minecraft.inventory.container.IContainerListener
import net.minecraft.item.ItemStack

abstract class AbstractChemicalConfigurableContainer<T>(
    type: ContainerType<*>?,
    id: Int,
    playerInventory: PlayerInventory,
    host: IUpgradeableHost
) : AbstractUpgradableContainer<T>(type, id, playerInventory, host), IChemicalSyncContainer {
    
    var chemicalList = arrayOf<Chemical<*>?>()

    var gui: IChemicalConfigListener? = null
    
    abstract fun getChemicalConfig(): IAEAChemicalConfig
    
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
        if (supportCapacity) {
            val upgrades = upgradable.getInstalledUpgrades(Upgrades.CAPACITY)
            
            if (slot > 0 && upgrades < 1) {
                return false
            }
            
            if (slot > 4 && upgrades < 2) {
                return false
            }
        }
        
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
        }
    }

    private fun sendChemicalListToPlayer(player: ServerPlayerEntity) {
        NetworkManager.sendTo(ChemicalConfigPacket(windowId, getChemicalConfig().getConfig()), player)
    }
}