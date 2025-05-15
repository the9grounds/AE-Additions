package com.the9grounds.aeadditions.forge.core.mixin;

import com.the9grounds.aeadditions.core.injections.IChannelHolderAccess;
import com.the9grounds.aeadditions.util.ChannelHolder;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerLevel.class)
public class MixinServerLevel implements IChannelHolderAccess {
    @Override
    public @NotNull ChannelHolder getChannelHolder() {
        throw new AssertionError();
    }
}
