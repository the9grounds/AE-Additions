package com.the9grounds.aeadditions.registries.client

import appeng.client.render.SimpleModelLoader
import appeng.client.render.crafting.CraftingCubeModel
import com.the9grounds.aeadditions.block.crafting.ExtendedCraftingUnitType
import com.the9grounds.aeadditions.client.render.crafting.ExtendedCraftingStorageModelProvider
import net.minecraftforge.client.model.geometry.IGeometryLoader
import net.minecraftforge.client.model.geometry.IUnbakedGeometry
import java.util.function.BiConsumer
import java.util.function.Supplier

object Models {
    fun init(register: BiConsumer<String, IGeometryLoader<*>>) {
        addBuiltInModel(register, "block/crafting/1024k_storage_formed") { CraftingCubeModel(ExtendedCraftingStorageModelProvider(ExtendedCraftingUnitType.STORAGE_1024)) }
        addBuiltInModel(register, "block/crafting/4096k_storage_formed") { CraftingCubeModel(ExtendedCraftingStorageModelProvider(ExtendedCraftingUnitType.STORAGE_4096)) }
        addBuiltInModel(register, "block/crafting/16384k_storage_formed") { CraftingCubeModel(ExtendedCraftingStorageModelProvider(ExtendedCraftingUnitType.STORAGE_16384)) }
        addBuiltInModel(register, "block/crafting/65536k_storage_formed") { CraftingCubeModel(ExtendedCraftingStorageModelProvider(ExtendedCraftingUnitType.STORAGE_65536)) }
    }

    private fun <T: IUnbakedGeometry<T>> addBuiltInModel(register: BiConsumer<String, IGeometryLoader<*>>, id: String, modelFactory: Supplier<T>) {
        register.accept(id, SimpleModelLoader(modelFactory))
    }
}