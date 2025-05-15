package com.the9grounds.aeadditions.forge

import com.the9grounds.aeadditions.AEAdditions
import dev.architectury.platform.forge.EventBuses
import net.minecraftforge.fml.common.Mod
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Mod(AEAdditions.ID)
object AEAdditionsForge {
    init {
        EventBuses.registerModEventBus(AEAdditions.ID, MOD_BUS)
        AEAdditions.init()
    }
}