package com.the9grounds.aeadditions.neoforge;

import net.neoforged.fml.common.Mod;

import com.the9grounds.aeadditions.ExampleMod;

@Mod(ExampleMod.MOD_ID)
public final class ExampleModNeoForge {
    public ExampleModNeoForge() {
        // Run our common setup.
        ExampleMod.init();
    }
}
