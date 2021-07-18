package com.the9grounds.aeadditions.item.block

import net.minecraft.block.Block
import net.minecraft.item.ItemBlock

class ItemBlockGasInterface(block: Block) : ItemBlock(block) {
    init {
        maxDamage = 0
    }
}