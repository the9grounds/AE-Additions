package com.the9grounds.aeadditions

import appeng.api.IAEAddonEntrypoint

object AEAdditionsClient : IAEAddonEntrypoint {
    override fun onAe2Initialized() {
        AEAdditions.registerItemColors()
        AEAdditions.registerModels()
    }
}