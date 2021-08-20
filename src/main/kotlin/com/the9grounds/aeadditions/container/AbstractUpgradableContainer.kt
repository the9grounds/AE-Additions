package com.the9grounds.aeadditions.container

import appeng.items.contents.NetworkToolViewer
import appeng.items.tools.NetworkToolItem
import com.the9grounds.aeadditions.api.IUpgradeableHost
import com.the9grounds.aeadditions.container.slot.FilterSlot
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.container.ContainerType

abstract class AbstractUpgradableContainer<T>(
    type: ContainerType<*>?,
    id: Int,
    playerInventory: PlayerInventory,
    host: IUpgradeableHost
) : AbstractContainer<T>(type, id, playerInventory, false, host) {
    
    val upgradable: IUpgradeableHost
    
    var networkToolSlot: Int? = null
    var networkToolInventory: NetworkToolViewer? = null
    
    init {
        upgradable = host
        
        val playerInventory = playerInventory
        
        for (i in 0 until playerInventory.sizeInventory) {
            val itemStack = playerInventory.getStackInSlot(i)
            
            if (!itemStack.isEmpty && itemStack.item is NetworkToolItem) {
                lockedSlots.add(i)
                networkToolSlot = i
                networkToolInventory = (itemStack.item as NetworkToolItem).getGuiObject(itemStack, i, playerInventory.player.world, null)
                
                break
            }
        }
        
        if (networkToolInventory != null) {
            for (i in 0..8) {
                val slot = FilterSlot(FilterSlot.ItemTypes.UPGRADES, networkToolInventory!!.internalInventory, i)
                
                addSlot(slot, SlotType.NetworkTool)
            }
        }
    }
    
    open val availableUpgrades = 4
    
    abstract fun setupConfig()
    
    protected fun setupUpgrades() {
        val upgrades = upgradable.getUpgradeInventory()
        
        for (i in 0 until availableUpgrades) {
            val slot = FilterSlot(FilterSlot.ItemTypes.UPGRADES, upgrades!!, i)
            
            addSlot(slot, SlotType.Upgrade)
        }
    }
}