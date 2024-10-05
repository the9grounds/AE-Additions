package com.the9grounds.aeadditions.integration

import com.the9grounds.aeadditions.Logger

object Integration {

    fun init() {
        for (mod in Mods.values()) {
            if (mod.isEnabled) {
                Logger.info("Integration for ${mod.modName} has been loaded")
            }
        }
    }
}