package com.the9grounds.aeadditions.util

import appeng.block.crafting.AbstractCraftingUnitBlock
import appeng.blockentity.crafting.CraftingStorageBlockEntity
import com.the9grounds.aeadditions.AEAdditions
import net.minecraft.client.renderer.texture.TextureAtlas
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.resources.model.BakedModel
import net.minecraft.client.resources.model.Material
import net.minecraft.resources.ResourceLocation
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import java.util.stream.Stream
import kotlin.reflect.jvm.isAccessible

object AE2 {

    val texture1024k = texture("1024k_storage_light")
    val texture4096k = texture("4096k_storage_light")
    val texture16384k = texture("16384k_storage_light")
    val texture65536k = texture("65536k_storage_light")
    
    @JvmStatic fun bakeExtraCpus(type: AbstractCraftingUnitBlock.CraftingUnitType, lightBase: Material, ringCorner: Material, ringSideHor: Material, ringSideVer: Material, spriteGetter: java.util.function.Function<Material, TextureAtlasSprite>, callbackInfoReturnable: CallbackInfoReturnable<BakedModel>) {
        
        val _ringCorner = spriteGetter.apply(ringCorner)
        val _ringSideHor = spriteGetter.apply(ringSideHor)
        val _ringSideVer = spriteGetter.apply(ringSideVer)
        
        when(type) {
            AEAdditions.craftingType1024k, AEAdditions.craftingType4096k, AEAdditions.craftingType16384k, AEAdditions.craftingType65536k -> {
                val lightBakedModelClass = Class.forName("appeng.client.render.crafting.LightBakedModel").kotlin
                val constructor = lightBakedModelClass.constructors.first()!!
                constructor.isAccessible = true
                val instance = constructor.call(
                    _ringCorner,
                    _ringSideHor,
                    _ringSideVer,
                    spriteGetter.apply(lightBase),
                    getLightTexture(spriteGetter, type)
                )
                
                callbackInfoReturnable.returnValue = instance as BakedModel?
            }
        }
    }
    
    fun getLightTexture(spriteGetter: java.util.function.Function<Material, TextureAtlasSprite>, type: AbstractCraftingUnitBlock.CraftingUnitType): TextureAtlasSprite {
        return when(type) {
            AEAdditions.craftingType1024k -> spriteGetter.apply(texture1024k)
            AEAdditions.craftingType4096k -> spriteGetter.apply(texture4096k)
            AEAdditions.craftingType16384k -> spriteGetter.apply(texture16384k)
            AEAdditions.craftingType65536k -> spriteGetter.apply(texture65536k)
            else -> throw IllegalArgumentException(
                "Crafting unit type $type does not use a light texture."
            )
        }
    }
    
    fun texture(name: String): Material {
        return Material(TextureAtlas.LOCATION_BLOCKS, ResourceLocation(AEAdditions.MOD_ID, "block/crafting/$name"))
    }
    
    @JvmStatic fun combineTextures(stream: Stream<Material>): Stream<Material> {
        val list = mutableListOf(texture1024k, texture4096k, texture16384k, texture65536k)
        list.addAll(stream.toList())
        return list.stream()
    }
    
    @JvmStatic fun getStorageBytes(blockEntity: CraftingStorageBlockEntity): Int? {
        if (!blockEntity.hasLevel() || blockEntity.notLoaded() || blockEntity.isRemoved) {
            return null
        }
        
        val unit = blockEntity.level!!.getBlockState(blockEntity.blockPos).block as AbstractCraftingUnitBlock<*>
        
        return when(unit.type) {
            AEAdditions.craftingType1024k -> 1024 * 1024
            AEAdditions.craftingType4096k -> 4096 * 1024
            AEAdditions.craftingType16384k -> 16384 * 1024
            AEAdditions.craftingType65536k -> 65536 * 1024
            else -> null
        }
    }
}