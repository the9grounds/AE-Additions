package com.the9grounds.aeadditions.integration.appeng

import com.the9grounds.aeadditions.api.AEAApi

object AppEng {
    @JvmStatic fun init() {
        AEAApi.instance().registerWrenchHandler(WrenchHandler)
    }
}