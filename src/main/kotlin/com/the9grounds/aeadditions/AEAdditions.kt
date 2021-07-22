package com.the9grounds.aeadditions

import com.the9grounds.aeadditions.debug.AEACommandRegistry
import com.the9grounds.aeadditions.integration.Integration
import com.the9grounds.aeadditions.registries.Items
import com.the9grounds.aeadditions.registries.Models
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.server.FMLServerStartingEvent
import thedarkcolour.kotlinforforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Mod(AEAdditions.ID)
object AEAdditions {

    const val ID = "aeadditions"

    init {
        Items.REGISTRY.register(MOD_BUS)

        MOD_BUS.addListener(::onClientSetup)
        MOD_BUS.addListener(::modelRegistryEvent)
        FORGE_BUS.addListener(::serverStarting)

        Integration.init()
    }

    private fun onClientSetup(event: FMLClientSetupEvent) {

    }
    
    private fun modelRegistryEvent(event: ModelRegistryEvent) {
        Models.init()
    }
    
    private fun serverStarting(event: FMLServerStartingEvent) {
        AEACommandRegistry.register(event.server.commandManager.dispatcher)
    }

}