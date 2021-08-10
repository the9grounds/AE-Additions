package com.the9grounds.aeadditions

import appeng.api.IAppEngApi
import com.the9grounds.aeadditions.api.gas.IChemicalStorageChannel
import com.the9grounds.aeadditions.debug.AEACommandRegistry
import com.the9grounds.aeadditions.integration.Integration
import com.the9grounds.aeadditions.integration.Mods
import com.the9grounds.aeadditions.integration.appeng.AppEng
import com.the9grounds.aeadditions.integration.mekanism.chemical.ChemicalStorageChannel
import com.the9grounds.aeadditions.network.NetworkManager
import com.the9grounds.aeadditions.registries.Cells
import com.the9grounds.aeadditions.registries.Items
import com.the9grounds.aeadditions.registries.Models
import com.the9grounds.aeadditions.registries.Parts
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
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
        MOD_BUS.addListener(::commonSetup)
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
    
    private fun commonSetup(event: FMLCommonSetupEvent) {
        NetworkManager.init()
    }

    internal fun onAppEngReady(api: IAppEngApi) {
        AppEng.initCellHandler()
        Cells.init()
        if (Mods.MEKANISM.isEnabled) {
            api.storage().registerStorageChannel(IChemicalStorageChannel::class.java, ChemicalStorageChannel())
        }

//        Parts.REGISTRY.register(MOD_BUS)
    }
}