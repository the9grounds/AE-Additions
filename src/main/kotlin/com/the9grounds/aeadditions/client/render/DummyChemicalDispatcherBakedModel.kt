package com.the9grounds.aeadditions.client.render

import com.the9grounds.aeadditions.item.ChemicalDummyItem
import mekanism.common.registries.MekanismGases
import net.minecraft.block.BlockState
import net.minecraft.client.renderer.model.BakedQuad
import net.minecraft.client.renderer.model.IBakedModel
import net.minecraft.client.renderer.model.ItemOverrideList
import net.minecraft.client.renderer.model.RenderMaterial
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.LivingEntity
import net.minecraft.inventory.container.PlayerContainer
import net.minecraft.item.ItemStack
import net.minecraft.util.Direction
import net.minecraft.util.math.vector.TransformationMatrix
import net.minecraftforge.client.model.ItemLayerModel
import java.util.*
import java.util.function.Function

class DummyChemicalDispatcherBakedModel(val baseModel: IBakedModel, val bakedTextureGetter: Function<RenderMaterial, TextureAtlasSprite>) : IBakedModel {
    override fun getQuads(state: BlockState?, side: Direction?, rand: Random): MutableList<BakedQuad> {
        return mutableListOf()
    }

    override fun isAmbientOcclusion(): Boolean = baseModel.isAmbientOcclusion

    override fun getParticleTexture(): TextureAtlasSprite = baseModel.particleTexture

    override fun isGui3d(): Boolean = baseModel.isGui3d

    override fun isSideLit(): Boolean = baseModel.isSideLit

    override fun isBuiltInRenderer(): Boolean = baseModel.isBuiltInRenderer

    override fun getOverrides(): ItemOverrideList {
        return object : ItemOverrideList() {
            override fun getOverrideModel(
                model: IBakedModel,
                stack: ItemStack,
                world: ClientWorld?,
                livingEntity: LivingEntity?
            ): IBakedModel? {
                val item = stack.item
                
                if (item !is ChemicalDummyItem) {
                    return model
                }
                
                var chemicalStack = item.getChemicalStack(stack)
                
                if (chemicalStack.isEmpty()) {
                    chemicalStack = MekanismGases.HYDROGEN.getStack(1)
                }
                
                val icon = chemicalStack.getType().getIcon()
                val material = RenderMaterial(PlayerContainer.LOCATION_BLOCKS_TEXTURE, icon)
                
                val sprite: TextureAtlasSprite = bakedTextureGetter.apply(material) ?: return DummyChemicalBakedModel(listOf())

                return DummyChemicalBakedModel(ItemLayerModel.getQuadsForSprite(0, sprite, TransformationMatrix.identity()))
            }
        }
    }
}