package com.the9grounds.aeadditions.registries.client

import appeng.client.render.SimpleModelLoader
import appeng.client.render.crafting.CraftingCubeModel
import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.block.crafting.ExtendedCraftingUnitType
import com.the9grounds.aeadditions.client.render.crafting.ExtendedCraftingStorageModelProvider
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.client.model.ModelLoaderRegistry
import net.minecraftforge.client.model.geometry.IModelGeometry
import java.util.function.Supplier

object Models {
    fun init() {
        addBuiltInModel("block/crafting/1024k_storage_formed") { CraftingCubeModel(ExtendedCraftingStorageModelProvider(ExtendedCraftingUnitType.STORAGE_1024)) }
        addBuiltInModel("block/crafting/4096k_storage_formed") { CraftingCubeModel(ExtendedCraftingStorageModelProvider(ExtendedCraftingUnitType.STORAGE_4096)) }
        addBuiltInModel("block/crafting/16384k_storage_formed") { CraftingCubeModel(ExtendedCraftingStorageModelProvider(ExtendedCraftingUnitType.STORAGE_16384)) }
        addBuiltInModel("block/crafting/65536k_storage_formed") { CraftingCubeModel(ExtendedCraftingStorageModelProvider(ExtendedCraftingUnitType.STORAGE_65536)) }
    }

    private fun <T: IModelGeometry<T>> addBuiltInModel(id: String, modelFactory: Supplier<T>) {
        ModelLoaderRegistry.registerLoader(ResourceLocation(AEAdditions.ID, id), SimpleModelLoader(modelFactory))
    }
}