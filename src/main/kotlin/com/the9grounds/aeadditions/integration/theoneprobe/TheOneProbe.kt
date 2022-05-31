package com.the9grounds.aeadditions.integration.theoneprobe

import com.the9grounds.aeadditions.integration.Mods
import net.minecraftforge.fml.InterModComms
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent

object TheOneProbe {
    
    fun enqueueIMC(event: InterModEnqueueEvent) {
        if (Mods.TOP.isEnabled) {
            InterModComms.sendTo("theoneprobe", "getTheOneProbe") { TheOneProbeModule }
        }
    }
}