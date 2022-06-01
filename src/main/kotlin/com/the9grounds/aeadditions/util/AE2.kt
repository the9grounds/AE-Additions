package com.the9grounds.aeadditions.util

import appeng.block.crafting.AbstractCraftingUnitBlock
import appeng.blockentity.crafting.CraftingStorageBlockEntity
import com.the9grounds.aeadditions.AEAdditions

object AE2 {
    
    @JvmStatic fun getStorageBytes(blockEntity: CraftingStorageBlockEntity): Int? {
        if (!blockEntity.hasLevel() || blockEntity.notLoaded() || blockEntity.isRemoved) {
            return null
        }
        
        val unit = blockEntity.level!!.getBlockState(blockEntity.blockPos).block as AbstractCraftingUnitBlock<*>
        
        return when(unit.type) {
            AEAdditions.craftingType1024k -> 1024 * 1024
            AEAdditions.craftingType4096k -> 4096 * 1024
            AEAdditions.craftingType16384k -> 16384 * 1024
            AEAdditions.craftingType65536k -> 65536 * 1024
            else -> null
        }
    }
}