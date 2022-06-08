package com.the9grounds.aeadditions.registries.client

import appeng.client.render.SimpleModelLoader
import appeng.client.render.crafting.CraftingCubeModel
import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.block.crafting.ExtendedCraftingUnitType
import com.the9grounds.aeadditions.client.render.crafting.ExtendedCraftingStorageModelProvider
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry
import net.minecraft.client.resources.model.UnbakedModel
import net.minecraft.resources.ResourceLocation
import java.util.function.Supplier

object Models {
    fun init() {
        addBuiltInModel("block/crafting/1024k_storage_formed") { CraftingCubeModel(
            ExtendedCraftingStorageModelProvider(
                ExtendedCraftingUnitType.STORAGE_1024)
        ) }
        addBuiltInModel("block/crafting/4096k_storage_formed") { CraftingCubeModel(
            ExtendedCraftingStorageModelProvider(
                ExtendedCraftingUnitType.STORAGE_4096)
        ) }
        addBuiltInModel("block/crafting/16384k_storage_formed") { CraftingCubeModel(
            ExtendedCraftingStorageModelProvider(
                ExtendedCraftingUnitType.STORAGE_16384)
        ) }
        addBuiltInModel("block/crafting/65536k_storage_formed") { CraftingCubeModel(
            ExtendedCraftingStorageModelProvider(
                ExtendedCraftingUnitType.STORAGE_65536)
        ) }
    }

    private fun <T: UnbakedModel> addBuiltInModel(id: String, modelFactory: Supplier<T>) {
        ModelLoadingRegistry.INSTANCE.registerResourceProvider { resourceManager ->
            SimpleModelLoader(ResourceLocation(AEAdditions.MOD_ID, id), modelFactory)
        }
    }
}