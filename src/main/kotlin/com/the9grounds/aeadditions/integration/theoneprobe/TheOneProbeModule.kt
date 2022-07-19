package com.the9grounds.aeadditions.integration.theoneprobe

import appeng.integration.IIntegrationModule
import mcjty.theoneprobe.api.ITheOneProbe
import java.util.function.Function

object TheOneProbeModule : IIntegrationModule, Function<ITheOneProbe, Void?> {
    override fun apply(t: ITheOneProbe): Void? {
        t.registerProvider(BlockEntityProvider)
        
        return null
    }
}