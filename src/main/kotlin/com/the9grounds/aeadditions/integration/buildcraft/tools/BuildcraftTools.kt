package com.the9grounds.aeadditions.integration.buildcraft.tools

import com.the9grounds.aeadditions.api.AEAApi

object BuildcraftTools {
    @JvmStatic fun init() {
        AEAApi.instance().registerWrenchHandler(WrenchHandler)
    }
}