package com.the9grounds.aeadditions.core.inv

import net.minecraft.item.ItemStack
import net.minecraftforge.items.IItemHandler

interface IAEAInventory {
    
    fun saveChanges()
    
    fun onInventoryChanged(inv: IItemHandler, slot: Int, operation: Operation, removedStack: ItemStack, newStack: ItemStack)
    
    val isRemote: Boolean
}