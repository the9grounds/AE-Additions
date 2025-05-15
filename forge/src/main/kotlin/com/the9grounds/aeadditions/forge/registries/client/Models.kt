package com.the9grounds.aeadditions.forge.registries.client

import appeng.client.render.crafting.CraftingCubeModel
import com.the9grounds.aeadditions.AEAdditions
import net.minecraft.client.resources.model.UnbakedModel
import net.minecraft.resources.ResourceLocation
import java.util.function.Supplier
import com.the9grounds.aeadditions.forge.client.render.crafting.ExtendedCraftingStorageModelProvider

object Models {

    val models = mutableMapOf<ResourceLocation, UnbakedModel>()
    fun init() {
        addBuiltInModel("block/crafting/1024k_storage_formed") { CraftingCubeModel(ExtendedCraftingStorageModelProvider(ExtendedCraftingUnitType.STORAGE_1024)) }
        addBuiltInModel("block/crafting/4096k_storage_formed") { CraftingCubeModel(ExtendedCraftingStorageModelProvider(ExtendedCraftingUnitType.STORAGE_4096)) }
        addBuiltInModel("block/crafting/16384k_storage_formed") { CraftingCubeModel(ExtendedCraftingStorageModelProvider(ExtendedCraftingUnitType.STORAGE_16384)) }
        addBuiltInModel("block/crafting/65536k_storage_formed") { CraftingCubeModel(ExtendedCraftingStorageModelProvider(ExtendedCraftingUnitType.STORAGE_65536)) }
    }

    private fun <T: UnbakedModel> addBuiltInModel(id: String, modelFactory: Supplier<T>) {
        if (!models.containsKey(ResourceLocation(AEAdditions.ID, id))) {
            models[ResourceLocation(AEAdditions.ID, id)] = modelFactory.get()
        }
    }

    @JvmStatic
    fun getModel(key: ResourceLocation): UnbakedModel? {
        if (!AEAdditions.ID.equals(key.namespace)) {
            return null;
        }

        return models[key];
    }
}