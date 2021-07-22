package com.the9grounds.aeadditions.registries

import appeng.client.render.DummyFluidItemModel
import appeng.client.render.SimpleModelLoader
import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.client.render.DummyChemicalItemModel
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.ModelLoaderRegistry
import net.minecraftforge.client.model.geometry.IModelGeometry
import java.util.function.Supplier

object Models {
    
    fun init() {
        addBuiltInModel("dummy_chemical_item") { DummyChemicalItemModel() }
    }
    
    private fun <T: IModelGeometry<T>> addBuiltInModel(id: String, modelFactory: Supplier<T>) {
        ModelLoaderRegistry.registerLoader(ResourceLocation(AEAdditions.ID, id), SimpleModelLoader(modelFactory))
    }
}