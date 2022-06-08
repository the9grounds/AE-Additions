package com.the9grounds.aeadditions.client.render.crafting

import appeng.client.render.crafting.AbstractCraftingUnitModelProvider
import appeng.client.render.crafting.LightBakedModel
import appeng.core.AppEng
import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.block.crafting.ExtendedCraftingUnitType
import net.minecraft.client.renderer.texture.TextureAtlas
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.resources.model.BakedModel
import net.minecraft.client.resources.model.Material
import net.minecraft.resources.ResourceLocation
import java.util.function.Function

class ExtendedCraftingStorageModelProvider(type: ExtendedCraftingUnitType) : AbstractCraftingUnitModelProvider<ExtendedCraftingUnitType>(type) {

    val texture1024k = texture("1024k_storage_light")
    val texture4096k = texture("4096k_storage_light")
    val texture16384k = texture("16384k_storage_light")
    val texture65536k = texture("65536k_storage_light")

    val RING_CORNER = textureAe2("ring_corner")
    val RING_SIDE_HOR = textureAe2("ring_side_hor")
    val RING_SIDE_VER = textureAe2("ring_side_ver")
    val LIGHT_BASE = textureAe2("light_base")
    
    override fun getMaterials(): MutableList<Material> {
        return mutableListOf(
            texture1024k,
            texture4096k,
            texture16384k,
            texture65536k
        )
    }

    fun getLightTexture(spriteGetter: java.util.function.Function<Material, TextureAtlasSprite>, type: ExtendedCraftingUnitType): TextureAtlasSprite {
        return when(type) {
            ExtendedCraftingUnitType.STORAGE_1024 -> spriteGetter.apply(texture1024k)
            ExtendedCraftingUnitType.STORAGE_4096 -> spriteGetter.apply(texture4096k)
            ExtendedCraftingUnitType.STORAGE_16384 -> spriteGetter.apply(texture16384k)
            ExtendedCraftingUnitType.STORAGE_65536 -> spriteGetter.apply(texture65536k)
            else -> throw IllegalArgumentException(
                "Crafting unit type $type does not use a light texture."
            )
        }
    }

    override fun getBakedModel(spriteGetter: Function<Material, TextureAtlasSprite>?): BakedModel? {
        val _ringCorner = spriteGetter!!.apply(RING_CORNER)
        val _ringSideHor = spriteGetter!!.apply(RING_SIDE_HOR)
        val _ringSideVer = spriteGetter!!.apply(RING_SIDE_VER)
        
        if (type is ExtendedCraftingUnitType) {
            return LightBakedModel(_ringCorner, _ringSideHor, _ringSideVer, spriteGetter.apply(LIGHT_BASE), getLightTexture(spriteGetter, type))
        }
        
        return null
    }

    fun texture(name: String): Material {
        return Material(TextureAtlas.LOCATION_BLOCKS, ResourceLocation(AEAdditions.MOD_ID, "block/crafting/$name"))
    }
    
    fun textureAe2(name: String): Material {
        return Material(TextureAtlas.LOCATION_BLOCKS, ResourceLocation(AppEng.MOD_ID, "block/crafting/$name"))
    }
}