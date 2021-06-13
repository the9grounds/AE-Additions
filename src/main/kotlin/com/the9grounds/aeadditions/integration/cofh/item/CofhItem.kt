package com.the9grounds.aeadditions.integration.cofh.item

import com.the9grounds.aeadditions.api.AEAApi

object CofhItem {
    @JvmStatic fun init() {
        AEAApi.instance().registerWrenchHandler(WrenchHandler)
    }
}