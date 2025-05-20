package com.the9grounds.aeadditions.core

import net.minecraft.world.item.BlockItem
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredItem

data class BlockDefinition<T: Block>(val block: DeferredBlock<T>, val item: DeferredItem<BlockItem>)
