package com.the9grounds.aeadditions.forge.block

import appeng.block.crafting.AbstractCraftingUnitBlock
import appeng.blockentity.crafting.CraftingBlockEntity
import com.the9grounds.aeadditions.forge.block.crafting.ExtendedCraftingUnitType
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType.Builder
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraftforge.registries.ForgeRegistries


class CraftingStorageBlock(props: BlockBehaviour.Properties, type: ExtendedCraftingUnitType?) : AbstractCraftingUnitBlock<CraftingBlockEntity>(props, type) {
    init {
        val supplier = { blockPos: BlockPos, blockState: BlockState ->
            CraftingBlockEntity(ForgeRegistries.BLOCK_ENTITY_TYPES.getValue(ResourceLocation("ae2", "crafting_storage")), blockPos, blockState)
        }
        val type = Builder.of(supplier, this).build(null)
        setBlockEntity(CraftingBlockEntity::class.java, type, null, null)
    }

    override fun getDrops(state: BlockState, p_287596_: LootParams.Builder): MutableList<ItemStack> {
        return mutableListOf(ItemStack(state.block.asItem(), 1))
    }

}