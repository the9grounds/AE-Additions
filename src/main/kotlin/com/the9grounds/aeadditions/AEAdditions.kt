package com.the9grounds.aeadditions

import appeng.api.AECapabilities
import appeng.items.storage.BasicStorageCell
import com.the9grounds.aeadditions.core.AEAConfig
import com.the9grounds.aeadditions.core.network.NetworkManager
import com.the9grounds.aeadditions.data.AEAdditionsDataGenerator
import com.the9grounds.aeadditions.integration.Integration
import com.the9grounds.aeadditions.integration.appeng.AppEng
import com.the9grounds.aeadditions.integration.appeng.InitUpgrades
import com.the9grounds.aeadditions.item.storage.StorageCell
import com.the9grounds.aeadditions.menu.MenuHolder
import com.the9grounds.aeadditions.registries.*
import com.the9grounds.aeadditions.registries.client.Models
import com.the9grounds.aeadditions.registries.client.Screens
import net.minecraft.client.renderer.ItemBlockRenderTypes
import net.minecraft.client.renderer.RenderType
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.ModLoadingContext
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.runForDist

@Mod(AEAdditions.ID)
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
object AEAdditions {

    const val ID = "ae2additions"

    init {
        Items.REGISTRY.register(MOD_BUS)
        Blocks.REGISTRY.register(MOD_BUS)
        BlockEntities.REGISTRY.register(MOD_BUS)
        MenuHolder.REGISTRY.register(MOD_BUS)
        CreativeTab.REGISTRY.register(MOD_BUS)
        DataComponents.REGISTRY.register(MOD_BUS)

        MOD_BUS.register(NetworkManager)
        MOD_BUS.addListener(::registerScreens)
        MOD_BUS.addListener(::commonSetup)
        MOD_BUS.addListener(AEAdditionsDataGenerator::onGatherData)
//        MOD_BUS.addListener(TheOneProbe::enqueueIMC)
        MOD_BUS.addListener(::clientStuff)
        ModLoadingContext.get().activeContainer.registerConfig(ModConfig.Type.COMMON, AEAConfig.COMMON_SPEC)

        val obj = runForDist(
            clientTarget = {
                MOD_BUS.addListener(AEAdditions::clientStuff)
                initClient()
            },
            serverTarget = {

            })

        Integration.init()
    }
    
    fun initClient() {
        Models.init()
    }
    
    private fun clientStuff(event: FMLClientSetupEvent) {
        event.enqueueWork {
            ItemBlockRenderTypes.setRenderLayer(Blocks.BLOCK_CRAFTING_STORAGE_1024k.block.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(Blocks.BLOCK_CRAFTING_STORAGE_4096k.block.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(Blocks.BLOCK_CRAFTING_STORAGE_16384k.block.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(Blocks.BLOCK_CRAFTING_STORAGE_65536k.block.get(), RenderType.cutout())
        }
    }

    private fun registerScreens(event: RegisterMenuScreensEvent) {
        Screens.registerScreens(event)
    }

    private fun commonSetup(event: FMLCommonSetupEvent) {
        AppEng.initCellHandler()
        Cells.init()
        InitUpgrades.init()

        event.enqueueWork {
            AEAConfig.save()
        }
    }

    @SubscribeEvent
    fun registerCapabilities(event: RegisterCapabilitiesEvent) {
        event.registerBlockEntity(
            AECapabilities.IN_WORLD_GRID_NODE_HOST,
            BlockEntities.ME_WIRELESS_TRANSCEIVER) { be, void ->
            be
        }
    }
}