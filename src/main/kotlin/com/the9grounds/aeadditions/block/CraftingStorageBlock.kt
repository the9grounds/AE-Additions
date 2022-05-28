package com.the9grounds.aeadditions.block

import appeng.block.crafting.CraftingStorageBlock
import appeng.blockentity.crafting.CraftingStorageBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.entity.BlockEntityType.Builder
import net.minecraft.world.level.block.state.BlockState


class CraftingStorageBlock(props: Properties?, type: CraftingUnitType?) : CraftingStorageBlock(props, type) {
    init {
        val supplier = { blockPos: BlockPos, blockState: BlockState ->
            CraftingStorageBlockEntity(Registry.BLOCK_ENTITY_TYPE.get(ResourceLocation("ae2", "crafting_storage")), blockPos, blockState)
        }
        val type = Builder.of(supplier, this).build(null)
        setBlockEntity(CraftingStorageBlockEntity::class.java, type, null, null)
    }
}