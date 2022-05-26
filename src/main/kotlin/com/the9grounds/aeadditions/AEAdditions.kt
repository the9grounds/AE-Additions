package com.the9grounds.aeadditions

import com.the9grounds.aeadditions.data.AEAdditionsDataGenerator
import com.the9grounds.aeadditions.integration.Integration
import com.the9grounds.aeadditions.integration.appeng.AppEng
import com.the9grounds.aeadditions.registries.Cells
import com.the9grounds.aeadditions.registries.Items
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Mod(AEAdditions.ID)
object AEAdditions {

    const val ID = "ae2additions"

    init {
        Items.init()
        Items.REGISTRY.register(MOD_BUS)
        
        MOD_BUS.addListener(::commonSetup)
        MOD_BUS.addListener(AEAdditionsDataGenerator::onGatherData)

        Integration.init()
    }

    private fun commonSetup(event: FMLCommonSetupEvent) {
        AppEng.initCellHandler()
        Cells.init()
    }
}