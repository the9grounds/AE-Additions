package com.the9grounds.aeadditions.fabric.integration

import net.fabricmc.loader.api.FabricLoader

object ModListImpl {
    @JvmStatic
    fun isModLoaded(modId: String): Boolean {
        return FabricLoader.getInstance().isModLoaded(modId)
    }
}