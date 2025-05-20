package com.the9grounds.aeadditions.integration

import net.neoforged.fml.ModList

enum class Mods(val modId: String, val modName: String) {
    MEKANISM("mekanism", "Mekanism"),
    APPMEK("appmek", "Applied Mekanistics"),
    AE2THINGS("ae2things", "AE2 Things"),
    APPLIEDBOTANICS("appbot", "Applied Botanics"),
    TOP("theoneprobe", "The One Probe"),
    FTBTEAMS("ftbteams", "FTB Teams");

    val isEnabled: Boolean
    get() {
        return ModList.get().isLoaded(modId)
    }
}