package com.the9grounds.aeadditions.block

import net.minecraft.block.Block
import net.minecraftforge.common.ToolType

class GasInterfaceBlock(properties: Properties) : Block(properties) {
    
    init {
        properties.hardnessAndResistance(2f, 11f).harvestTool(ToolType.PICKAXE).harvestLevel(0)
    }
}