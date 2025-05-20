package com.the9grounds.aeadditions.data.provider

import appeng.block.crafting.AbstractCraftingUnitBlock
import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.registries.Blocks
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
import net.neoforged.neoforge.client.model.generators.CustomLoaderBuilder
import net.neoforged.neoforge.common.data.ExistingFileHelper

class BlockModelProvider(gen: PackOutput?, modid: String?, exFileHelper: ExistingFileHelper?) : BlockStateProvider(gen, modid,
    exFileHelper
) {

    override fun registerStatesAndModels() {
        registerCraftingStorage(Blocks.BLOCK_CRAFTING_STORAGE_1024k.block.get(), "1024k_storage_formed", "1024k_storage")
        registerCraftingStorage(Blocks.BLOCK_CRAFTING_STORAGE_4096k.block.get(), "4096k_storage_formed", "4096k_storage")
        registerCraftingStorage(Blocks.BLOCK_CRAFTING_STORAGE_16384k.block.get(), "16384k_storage_formed", "16384k_storage")
        registerCraftingStorage(Blocks.BLOCK_CRAFTING_STORAGE_65536k.block.get(), "65536k_storage_formed", "65536k_storage")
    }

    private fun registerCraftingStorage(block: AbstractCraftingUnitBlock<*>, modelName: String, secondModelName: String) {
        val model = models().getBuilder("block/crafting/$modelName")
        val loader = ResourceLocation.fromNamespaceAndPath(AEAdditions.ID, "block/crafting/$modelName")

        model.customLoader() { bmb, efh ->
            object : CustomLoaderBuilder<BlockModelBuilder>(loader, bmb, efh, false) {}
        }

        val blockModel = models().cubeAll(
            "block/crafting/$secondModelName",
            ResourceLocation.fromNamespaceAndPath(AEAdditions.ID, "block/crafting/$secondModelName")
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