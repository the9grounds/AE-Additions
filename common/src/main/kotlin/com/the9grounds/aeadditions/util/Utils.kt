package com.the9grounds.aeadditions.util

import net.minecraft.nbt.CompoundTag
import net.minecraft.world.item.ItemStack

object Utils {
    
    fun isItemStackEqual(left: ItemStack, right: ItemStack): Boolean {
        return left.`is`(right.item) && isNbtEqual(left.tag, right.tag)
    }
    
    fun isNbtEqual(left: CompoundTag?, right: CompoundTag?): Boolean {
        if (left == right) {
            return true;
        }
        
        val isLeftNullOrEmpty = left == null || left.isEmpty
        val isRightNullOrEmpty = right == null || right.isEmpty
        
        if (isLeftNullOrEmpty && isRightNullOrEmpty) {
            return true
        }
        
        if (isLeftNullOrEmpty != isRightNullOrEmpty) {
            return false
        }
        
        return left?.equals(right) ?: false
    }
}