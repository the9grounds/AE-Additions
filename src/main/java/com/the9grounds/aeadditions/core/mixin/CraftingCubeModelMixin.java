package com.the9grounds.aeadditions.core.mixin;

import appeng.block.crafting.AbstractCraftingUnitBlock;
import appeng.client.render.crafting.CraftingCubeModel;
import com.the9grounds.aeadditions.util.AE2;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.IModelConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;
import java.util.stream.Stream;

@Mixin(CraftingCubeModel.class)
public abstract class CraftingCubeModelMixin {

    @Shadow(remap = false)
    private AbstractCraftingUnitBlock.CraftingUnitType type;

    @Shadow(remap = false)
    private static Material LIGHT_BASE;
    
    @Shadow(remap = false)
    private static Material RING_CORNER;

    @Shadow(remap = false)
    private static Material RING_SIDE_HOR;

    @Shadow(remap = false)
    private static Material RING_SIDE_VER;
    
    @Inject(method = "bake(Lnet/minecraftforge/client/model/IModelConfiguration;Lnet/minecraft/client/resources/model/ModelBakery;Ljava/util/function/Function;Lnet/minecraft/client/resources/model/ModelState;Lnet/minecraft/client/renderer/block/model/ItemOverrides;Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/resources/model/BakedModel", at = @At("HEAD"), cancellable = true, remap = false)
    private void bakeExtraCpus(IModelConfiguration owner, ModelBakery bakery,
                               Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform,
                               ItemOverrides overrides, ResourceLocation modelLocation, CallbackInfoReturnable<BakedModel> callbackInfoReturnable) {
        AE2.bakeExtraCpus(type, LIGHT_BASE, RING_CORNER, RING_SIDE_HOR, RING_SIDE_VER, spriteGetter, callbackInfoReturnable);
    }
    
    @Inject(method = "getAdditionalTextures()Ljava/util/stream/Stream", at = @At("RETURN"), cancellable = true, remap = false)
    private void returnExtraCpuTextures(CallbackInfoReturnable<Stream<Material>> callbackInfoReturnable) {
        callbackInfoReturnable.setReturnValue(AE2.combineTextures(callbackInfoReturnable.getReturnValue()));
    }
}
