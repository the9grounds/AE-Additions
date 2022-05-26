package com.the9grounds.aeadditions.integration

import net.minecraftforge.fml.ModList

enum class Mods(val modId: String, val modName: String) {
    MEKANISM("mekanism", "Mekanism"),
    APPMEK("appmek", "Applied Mekanistics");

    val isEnabled: Boolean
    get() {
        return ModList.get().isLoaded(modId)
    }
}