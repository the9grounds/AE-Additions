package com.the9grounds.aeadditions.block

import appeng.block.crafting.CraftingStorageBlock
import appeng.blockentity.crafting.CraftingStorageBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.entity.BlockEntityType.Builder
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.registries.ForgeRegistries


class CraftingStorageBlock(props: Properties?, type: CraftingUnitType?) : CraftingStorageBlock(props, type) {
    init {
        val supplier = { blockPos: BlockPos, blockState: BlockState ->
            CraftingStorageBlockEntity(ForgeRegistries.BLOCK_ENTITIES.getValue(ResourceLocation("ae2", "crafting_storage")), blockPos, blockState)
        }
        val type = Builder.of(supplier, this).build(null)
        setBlockEntity(CraftingStorageBlockEntity::class.java, type, null, null)
    }
}