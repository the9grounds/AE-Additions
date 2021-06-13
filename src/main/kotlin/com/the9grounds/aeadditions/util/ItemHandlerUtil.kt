package com.the9grounds.aeadditions.util

import net.minecraft.item.ItemStack
import net.minecraftforge.items.IItemHandler

object ItemHandlerUtil {
    @JvmStatic fun insertItemStack(itemHandler: IItemHandler?, itemStack: ItemStack, simulate: Boolean): ItemStack {
        if (itemHandler == null) {
            return itemStack
        }

        var itemStackRemaining = itemStack.copy()

        for (i in 0 until itemHandler.slots) {
            itemStackRemaining = itemHandler.insertItem(i, itemStackRemaining, simulate)
            if (itemStackRemaining.isEmpty) {
                return ItemStack.EMPTY
            }
        }
        return itemStackRemaining
    }
}