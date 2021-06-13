package com.the9grounds.aeadditions.integration.enderio

import com.the9grounds.aeadditions.api.AEAApi

object EnderIO {

    @JvmStatic fun init() {
        AEAApi.instance().registerWrenchHandler(WrenchHandler)
    }
}