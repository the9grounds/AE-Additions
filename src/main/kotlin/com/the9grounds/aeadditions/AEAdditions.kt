package com.the9grounds.aeadditions

import appeng.api.IAppEngApi
import com.the9grounds.aeadditions.api.chemical.IChemicalStorageChannel
import com.the9grounds.aeadditions.core.EventHandler
import com.the9grounds.aeadditions.debug.AEACommandRegistry
import com.the9grounds.aeadditions.integration.Integration
import com.the9grounds.aeadditions.integration.Mods
import com.the9grounds.aeadditions.integration.appeng.AppEng
import com.the9grounds.aeadditions.integration.mekanism.chemical.ChemicalStorageChannel
import com.the9grounds.aeadditions.network.NetworkManager
import com.the9grounds.aeadditions.registries.*
import com.the9grounds.aeadditions.registries.client.Models
import com.the9grounds.aeadditions.registries.client.PartModels
import com.the9grounds.aeadditions.registries.client.Screens
import net.minecraft.inventory.container.ContainerType
import net.minecraftforge.client.event.ColorHandlerEvent
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.RegistryEvent
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
        Items.init()
        Items.REGISTRY.register(MOD_BUS)
        Parts.init()
        Parts.REGISTRY.register(MOD_BUS)
        Blocks.init()
        Blocks.REGISTRY.register(MOD_BUS)
        Tiles.init()
        Tiles.REGISTRY.register(MOD_BUS)
        
        MOD_BUS.addListener(::onClientSetup)
        MOD_BUS.addListener(::modelRegistryEvent)
        MOD_BUS.addListener(::commonSetup)
        MinecraftForge.EVENT_BUS.register(EventHandler)
        MOD_BUS.addGenericListener(::registerContainerTypes)
        MOD_BUS.addListener(::registerItemColors)
        FORGE_BUS.addListener(::serverStarting)

        Integration.init()
    }
    
    private fun registerContainerTypes(event: RegistryEvent.Register<ContainerType<*>>) {
        ContainerTypes.init(event.registry)
    }

    private fun onClientSetup(event: FMLClientSetupEvent) {
        Screens.init()
    }
    
    private fun modelRegistryEvent(event: ModelRegistryEvent) {
        Models.init()
        PartModels.init()
    }
    
    private fun serverStarting(event: FMLServerStartingEvent) {
        AEACommandRegistry.register(event.server.commandManager.dispatcher)
    }
    
    private fun commonSetup(event: FMLCommonSetupEvent) {
        InitUpgrades.init()
        event.enqueueWork {
            NetworkManager.init()
        }
    }
    
    private fun registerItemColors(event: ColorHandlerEvent.Item) {
        ItemColors.init(event.itemColors)
    }

    internal fun onAppEngReady(api: IAppEngApi) {
        AppEng.initCellHandler()
        Cells.init()
        if (Mods.MEKANISM.isEnabled) {
            api.storage().registerStorageChannel(IChemicalStorageChannel::class.java, ChemicalStorageChannel())
        }
    }
}