package com.the9grounds.aeadditions.block

import appeng.block.crafting.AbstractCraftingUnitBlock
import appeng.blockentity.crafting.CraftingBlockEntity
import com.the9grounds.aeadditions.block.crafting.ExtendedCraftingUnitType
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.entity.BlockEntityType.Builder
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.registries.ForgeRegistries


class CraftingStorageBlock(props: BlockBehaviour.Properties, type: ExtendedCraftingUnitType?) : AbstractCraftingUnitBlock<CraftingBlockEntity>(props, type) {
    init {
        val supplier = { blockPos: BlockPos, blockState: BlockState ->
            CraftingBlockEntity(ForgeRegistries.BLOCK_ENTITY_TYPES.getValue(ResourceLocation("ae2", "crafting_storage")), blockPos, blockState)
        }
        val type = Builder.of(supplier, this).build(null)
        setBlockEntity(CraftingBlockEntity::class.java, type, null, null)
    }
}