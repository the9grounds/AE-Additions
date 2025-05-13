package com.the9grounds.aeadditions.forge.core.mixin;

import com.the9grounds.aeadditions.blockentity.MEWirelessTransceiverBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(MEWirelessTransceiverBlockEntity.class)
public abstract class MEWirelessTransceiverBlockEntityMixin {
    @Inject(at = @At("RETURN"), method = "onChunkUnloaded")
    private void onChunkUnloaded() {
        handleChunkUnloaded();
    }

    @Inject(at = @At("RETURN"), method = "setRemoved")
    private void setRemoved() {
        handleSetRemoved();
    }

    @Inject(at = @At("RETURN"), method = "onLoad")
    private void onLoad() {
        handleOnLoad();
    }

    @Shadow
    protected abstract void handleSetRemoved();

    @Shadow
    protected abstract void handleChunkUnloaded();

    @Shadow
    protected abstract void handleOnLoad();
}
