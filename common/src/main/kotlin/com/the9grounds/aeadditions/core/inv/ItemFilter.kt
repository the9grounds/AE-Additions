package com.the9grounds.aeadditions.core.inv

import net.minecraft.world.item.ItemStack
import net.minecraftforge.items.IItemHandler

interface ItemFilter {
    fun allowExtract(inv: IItemHandler, slot: Int, amount: Int): Boolean
    
    fun allowInsert(inv: IItemHandler, slot: Int, itemStack: ItemStack): Boolean
}