package com.the9grounds.aeadditions.integration

import net.fabricmc.loader.api.FabricLoader

enum class Mods(val modId: String, val modName: String) {
    AE2THINGS("ae2things", "AE2 Things");

    val isEnabled: Boolean
    get() {
        return FabricLoader.getInstance().isModLoaded(modId)
    }
}