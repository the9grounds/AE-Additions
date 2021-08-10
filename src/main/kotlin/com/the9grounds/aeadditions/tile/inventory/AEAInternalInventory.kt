package com.the9grounds.aeadditions.tile.inventory

import net.minecraft.item.ItemStack
import net.minecraftforge.items.ItemStackHandler

class AEAInternalInventory : ItemStackHandler(), Iterable<ItemStack> {
    override fun iterator(): Iterator<ItemStack> {
        return stacks.toList().iterator()
    }
}