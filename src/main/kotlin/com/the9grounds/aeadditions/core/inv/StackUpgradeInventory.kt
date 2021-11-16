package com.the9grounds.aeadditions.core.inv

import appeng.api.config.Upgrades
import net.minecraft.item.ItemStack

class StackUpgradeInventory(val itemStack: ItemStack, parent: IAEAInventory, size: Int): AbstractUpgradeInventory(parent, size) {
    
    override fun getMaxInstalled(upgrade: Upgrades): Int {
        for (supported in upgrade.supported) {
            if (supported.isSupported(itemStack.item)) {
                return supported.maxCount
            }
        }
        
        return 0
    }
}