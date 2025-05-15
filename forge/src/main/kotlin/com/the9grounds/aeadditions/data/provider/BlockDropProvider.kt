package com.the9grounds.aeadditions.data.provider

import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.registries.Blocks
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlagSet
import net.minecraft.world.level.block.Block
import net.minecraftforge.registries.ForgeRegistries

class BlockDropProvider() : BlockLootSubProvider(mutableSetOf(), FeatureFlagSet.of()) {

    override fun generate() {
        this.dropSelf(Blocks.BLOCK_CRAFTING_STORAGE_1024k.block)
        this.dropSelf(Blocks.BLOCK_CRAFTING_STORAGE_4096k.block)
        this.dropSelf(Blocks.BLOCK_CRAFTING_STORAGE_16384k.block)
        this.dropSelf(Blocks.BLOCK_CRAFTING_STORAGE_65536k.block)
        this.dropSelf(Blocks.BLOCK_ME_WIRELESS_TRANSCEIVER.block)
    }

    override fun getKnownBlocks(): MutableIterable<Block> {
        return super.getKnownBlocks().filter { ForgeRegistries.BLOCKS.getKey(it)!!.namespace == AEAdditions.ID }.toMutableList()
    }
}