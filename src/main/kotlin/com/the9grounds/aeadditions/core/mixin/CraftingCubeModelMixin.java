package com.the9grounds.aeadditions.core.mixin;

import appeng.block.crafting.AbstractCraftingUnitBlock;
import appeng.client.render.crafting.CraftingCubeModel;
import com.the9grounds.aeadditions.util.AE2Client;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;
import java.util.stream.Stream;

@Mixin(CraftingCubeModel.class)
public abstract class CraftingCubeModelMixin {

    @Shadow
    private AbstractCraftingUnitBlock.CraftingUnitType type;

    @Shadow
    private static Material LIGHT_BASE;
    
    @Shadow
    private static Material RING_CORNER;

    @Shadow
    private static Material RING_SIDE_HOR;

    @Shadow
    private static Material RING_SIDE_VER;
    
    @Inject(method = "bake(Lnet/minecraft/client/resources/model/ModelBakery;Ljava/util/function/Function;Lnet/minecraft/client/resources/model/ModelState;Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/resources/model/BakedModel;", at = @At("HEAD"), cancellable = true, remap = true)
    private void bakeExtraCpus(ModelBakery bakery,
                               Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform, ResourceLocation modelLocation, CallbackInfoReturnable<BakedModel> callbackInfoReturnable) {
        AE2Client.bakeExtraCpus(type, LIGHT_BASE, RING_CORNER, RING_SIDE_HOR, RING_SIDE_VER, spriteGetter, callbackInfoReturnable);
    }
    
    @Inject(method = "getAdditionalTextures()Ljava/util/stream/Stream;", at = @At("RETURN"), cancellable = true, remap = false)
    private void returnExtraCpuTextures(CallbackInfoReturnable<Stream<Material>> callbackInfoReturnable) {
        callbackInfoReturnable.setReturnValue(AE2Client.combineTextures(callbackInfoReturnable.getReturnValue()));
    }
}
