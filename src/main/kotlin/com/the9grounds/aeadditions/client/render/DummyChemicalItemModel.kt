package com.the9grounds.aeadditions.client.render

import com.mojang.datafixers.util.Pair
import com.the9grounds.aeadditions.AEAdditions
import net.minecraft.client.renderer.model.*
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.IModelConfiguration
import net.minecraftforge.client.model.geometry.IModelGeometry
import java.util.function.Function

class DummyChemicalItemModel : IModelGeometry<DummyChemicalItemModel> {
    
    companion object {
        val MODEL_BASE = ResourceLocation(AEAdditions.ID, "item/dummy_chemical_item_base")
    }
    
    override fun bake(
        owner: IModelConfiguration?,
        bakery: ModelBakery,
        spriteGetter: Function<RenderMaterial, TextureAtlasSprite>,
        modelTransform: IModelTransform,
        overrides: ItemOverrideList?,
        modelLocation: ResourceLocation?
    ): IBakedModel {
        val baseModel = bakery.getBakedModel(MODEL_BASE, modelTransform, spriteGetter)!!
        
        return DummyChemicalDispatcherBakedModel(baseModel, spriteGetter)
    }

    override fun getTextures(
        owner: IModelConfiguration?,
        modelGetter: Function<ResourceLocation, IUnbakedModel>?,
        missingTextureErrors: MutableSet<Pair<String, String>>?
    ): MutableCollection<RenderMaterial> {
        return mutableListOf()
    }
}