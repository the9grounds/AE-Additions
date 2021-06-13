package com.the9grounds.aeadditions.integration.mekanism

import com.the9grounds.aeadditions.api.AEAApi
import com.the9grounds.aeadditions.integration.Integration

object Mekanism {

    @JvmStatic fun init() {
        if (Integration.Mods.MEKANISMGAS.isEnabled) {
            AEAApi.instance().addExternalStorageInterface(HandlerMekanismGasTank)
        }

        AEAApi.instance().registerWrenchHandler(WrenchHandler)
    }
}