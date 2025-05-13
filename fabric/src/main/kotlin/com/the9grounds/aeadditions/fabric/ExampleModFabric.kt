package com.the9grounds.aeadditions.fabric

import com.the9grounds.aeadditions.AEAdditions.init
import net.fabricmc.api.ModInitializer

class AEAdditionsFabric : ModInitializer {
    override fun onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.

        init()
    }
}
