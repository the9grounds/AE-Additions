package com.the9grounds.aeadditions.block

import appeng.block.crafting.AbstractCraftingUnitBlock
import appeng.blockentity.crafting.CraftingBlockEntity
import com.the9grounds.aeadditions.block.crafting.ExtendedCraftingUnitType
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntityType.Builder
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.loot.LootParams


class CraftingStorageBlock(props: BlockBehaviour.Properties, type: ExtendedCraftingUnitType?) : AbstractCraftingUnitBlock<CraftingBlockEntity>(props, type) {
    override fun getDrops(state: BlockState, p_287596_: LootParams.Builder): MutableList<ItemStack> {
        return mutableListOf(ItemStack(state.block.asItem(), 1))
    }

}