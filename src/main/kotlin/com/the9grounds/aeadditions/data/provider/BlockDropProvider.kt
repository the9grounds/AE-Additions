package com.the9grounds.aeadditions.data.provider

import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.registries.Blocks
import net.minecraft.data.loot.BlockLoot
import net.minecraft.world.level.block.Block

class BlockDropProvider : BlockLoot() {
    override fun addTables() {
        this.dropSelf(Blocks.BLOCK_CRAFTING_STORAGE_1024k.block)
        this.dropSelf(Blocks.BLOCK_CRAFTING_STORAGE_4096k.block)
        this.dropSelf(Blocks.BLOCK_CRAFTING_STORAGE_16384k.block)
        this.dropSelf(Blocks.BLOCK_CRAFTING_STORAGE_65536k.block)
        this.dropSelf(Blocks.BLOCK_ME_WIRELESS_TRANSCEIVER.block)
    }

    override fun getKnownBlocks(): MutableIterable<Block> {
        return super.getKnownBlocks().filter { it.registryName!!.namespace == AEAdditions.ID }.toMutableList()
    }
}