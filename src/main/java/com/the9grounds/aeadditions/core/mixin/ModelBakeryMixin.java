package com.the9grounds.aeadditions.core.mixin;

import com.the9grounds.aeadditions.registries.client.Models;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelBakery.class)
public class ModelBakeryMixin {
    @Inject(at = @At("HEAD"), method = "loadModel", cancellable = true)
    private void loadModelHook(ResourceLocation id, CallbackInfo ci) {
        var model = Models.getModel(id);

        if (model != null) {
            cacheAndQueueDependencies(id, model);
            ci.cancel();
        }
    }

    @Shadow
    protected void cacheAndQueueDependencies(ResourceLocation id, UnbakedModel unbakedModel) {
    }
}

