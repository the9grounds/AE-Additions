package com.the9grounds.aeadditions.data.provider

import appeng.block.crafting.AbstractCraftingUnitBlock
import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.registries.Blocks
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.client.model.generators.BlockModelBuilder
import net.minecraftforge.client.model.generators.BlockStateProvider
import net.minecraftforge.client.model.generators.ConfiguredModel
import net.minecraftforge.client.model.generators.CustomLoaderBuilder
import net.minecraftforge.common.data.ExistingFileHelper

class BlockModelProvider(gen: PackOutput?, modid: String?, exFileHelper: ExistingFileHelper?) : BlockStateProvider(gen, modid,
    exFileHelper
) {

    override fun registerStatesAndModels() {
        registerCraftingStorage(Blocks.BLOCK_CRAFTING_STORAGE_1024k.block, "1024k_storage_formed", "1024k_storage")
        registerCraftingStorage(Blocks.BLOCK_CRAFTING_STORAGE_4096k.block, "4096k_storage_formed", "4096k_storage")
        registerCraftingStorage(Blocks.BLOCK_CRAFTING_STORAGE_16384k.block, "16384k_storage_formed", "16384k_storage")
        registerCraftingStorage(Blocks.BLOCK_CRAFTING_STORAGE_65536k.block, "65536k_storage_formed", "65536k_storage")
    }

    private fun registerCraftingStorage(block: AbstractCraftingUnitBlock<*>, modelName: String, secondModelName: String) {
        val model = models().getBuilder("block/crafting/$modelName")
        val loader = ResourceLocation(AEAdditions.ID, "block/crafting/$modelName")

        model.customLoader() { bmb, efh ->
            object : CustomLoaderBuilder<BlockModelBuilder>(loader, bmb, efh) {}
        }

        val blockModel = models().cubeAll(
            "block/crafting/$secondModelName",
            ResourceLocation(AEAdditions.ID, "block/crafting/$secondModelName")
        )
        getVariantBuilder(block)
            .partialState().with(AbstractCraftingUnitBlock.FORMED, false)
            .setModels(ConfiguredModel(blockModel))
            .partialState().with(AbstractCraftingUnitBlock.FORMED, true).setModels(
                ConfiguredModel(models().getBuilder("block/crafting/$modelName"))
            )
        simpleBlockItem(block, blockModel)
    }
}