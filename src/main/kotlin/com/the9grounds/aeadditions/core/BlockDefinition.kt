package com.the9grounds.aeadditions.core

import net.minecraft.item.BlockItem

data class BlockDefinition<T>(val block: T, val item: BlockItem)
