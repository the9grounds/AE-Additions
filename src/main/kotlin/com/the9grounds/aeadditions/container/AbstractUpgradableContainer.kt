package com.the9grounds.aeadditions.container

import appeng.api.config.Upgrades
import appeng.items.contents.NetworkToolViewer
import appeng.items.tools.NetworkToolItem
import com.the9grounds.aeadditions.api.IUpgradeableHost
import com.the9grounds.aeadditions.container.slot.FilterSlot
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.container.ContainerType
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent

abstract class AbstractUpgradableContainer<T>(
    type: ContainerType<*>?,
    id: Int,
    playerInventory: PlayerInventory,
    host: IUpgradeableHost
) : AbstractContainer<T>(type, id, playerInventory, false, host) {
    
    val upgradable: IUpgradeableHost
    
    var networkToolSlot: Int? = null
    var networkToolInventory: NetworkToolViewer? = null
    
    open val supportCapacity: Boolean = true
    
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
        
        setupConfig()
    }
    
    val hasToolbox: Boolean
    get () = networkToolInventory != null
    
    val toolboxName: ITextComponent
    get() = if (networkToolInventory != null) networkToolInventory!!.itemStack.displayName else StringTextComponent.EMPTY
    
    open val availableUpgrades: Int
    get() = 4
    
    abstract fun setupConfig()
    
    protected fun setupUpgrades() {
        val upgrades = upgradable.getUpgradeInventory()
        
        for (i in 0 until availableUpgrades) {
            val slot = FilterSlot(FilterSlot.ItemTypes.UPGRADES, upgrades!!, i)
            
            addSlot(slot, SlotType.Upgrade)
        }
    }
    
    fun isConfigGroupEnabled(group: Int): Boolean {
        val capacityUpgrades = upgradable.getInstalledUpgrades(Upgrades.CAPACITY)
        
        return group == 0 || (group == 1 && capacityUpgrades > 0) || (group == 2 && capacityUpgrades > 1)
    }
}