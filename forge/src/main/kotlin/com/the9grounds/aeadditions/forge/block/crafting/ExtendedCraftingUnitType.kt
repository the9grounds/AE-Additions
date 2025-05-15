package com.the9grounds.aeadditions.forge.block.crafting

import appeng.block.crafting.ICraftingUnitType
import com.the9grounds.aeadditions.forge.registries.Blocks
import net.minecraft.world.item.Item

enum class ExtendedCraftingUnitType(val kiloBytes: Int) : ICraftingUnitType {
    STORAGE_1024(1024),
    STORAGE_4096(4096),
    STORAGE_16384(16384),
    STORAGE_65536(65536);

    override fun getStorageBytes(): Long = 1024L * kiloBytes

    override fun getAcceleratorThreads(): Int = 0

    override fun getItemFromType(): Item? {
        return when(this) {
            STORAGE_1024 -> Blocks.BLOCK_CRAFTING_STORAGE_1024k.item
            STORAGE_4096 -> Blocks.BLOCK_CRAFTING_STORAGE_4096k.item
            STORAGE_16384 -> Blocks.BLOCK_CRAFTING_STORAGE_16384k.item
            STORAGE_65536 -> Blocks.BLOCK_CRAFTING_STORAGE_65536k.item
            else -> null
        }
    }
}