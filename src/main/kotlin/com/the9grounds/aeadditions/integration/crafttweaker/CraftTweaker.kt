package com.the9grounds.aeadditions.integration.crafttweaker

import crafttweaker.CraftTweakerAPI

object CraftTweaker {

    @JvmStatic fun preInit() {
        CraftTweakerAPI.registerClass(FluidVibrationChamberRegistry::class.java)
    }
}