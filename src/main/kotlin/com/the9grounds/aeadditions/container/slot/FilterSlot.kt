package com.the9grounds.aeadditions.container.slot

import appeng.api.implementations.items.IUpgradeModule
import net.minecraft.item.ItemStack
import net.minecraftforge.items.IItemHandler

class FilterSlot(val itemType: FilterSlot.ItemTypes, inv: IItemHandler, slot: Int) :
    AEASlot(inv, slot) {

    override fun isItemValid(stack: ItemStack): Boolean {
        val bool = when(itemType) {
            ItemTypes.UPGRADES -> stack.item is IUpgradeModule && ((stack.item as IUpgradeModule).getType(stack)) != null
            else -> false
        }
        
        return bool && inv.isItemValid(slotIndex, stack)
    }
        
        
    enum class ItemTypes {
        UPGRADES;
    }
}