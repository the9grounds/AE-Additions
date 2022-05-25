package com.the9grounds.aeadditions.client.render

import net.minecraft.block.BlockState
import net.minecraft.client.renderer.model.BakedQuad
import net.minecraft.client.renderer.model.IBakedModel
import net.minecraft.client.renderer.model.ItemOverrideList
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.util.Direction
import java.util.*

class DummyChemicalBakedModel(val quads: List<BakedQuad>) : IBakedModel {
    override fun getQuads(state: BlockState?, side: Direction?, rand: Random): MutableList<BakedQuad> {
        return quads.toMutableList()
    }

    override fun isAmbientOcclusion(): Boolean = false

    override fun getParticleTexture(): TextureAtlasSprite? = null

    override fun isGui3d(): Boolean = false

    override fun isSideLit(): Boolean = false

    override fun isBuiltInRenderer(): Boolean = false

    override fun getOverrides(): ItemOverrideList? = null
}