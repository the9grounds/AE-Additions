package com.the9grounds.aeadditions.container.slot

import appeng.api.implementations.items.IUpgradeModule
import net.minecraft.item.ItemStack
import net.minecraftforge.items.IItemHandler

class FilterSlot(val itemType: FilterSlot.ItemTypes, inv: IItemHandler, slot: Int) :
    AEASlot(inv, slot) {

    override fun isItemValid(stack: ItemStack): Boolean {
        return when(itemType) {
            ItemTypes.UPGRADES -> stack.item is IUpgradeModule && ((stack.item as IUpgradeModule).getType(stack)) != null
            else -> false
        }
    }
        
        
    enum class ItemTypes {
        UPGRADES;
    }
}