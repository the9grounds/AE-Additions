package com.the9grounds.aeadditions

import appeng.items.storage.BasicStorageCell
import com.the9grounds.aeadditions.core.AEAConfig
import com.the9grounds.aeadditions.core.CreativeTab
import com.the9grounds.aeadditions.core.network.NetworkManager
import com.the9grounds.aeadditions.data.AEAdditionsDataGenerator
import com.the9grounds.aeadditions.debug.CommandRegistry
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
import net.minecraft.world.level.Level
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.RegisterColorHandlersEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.server.ServerStartingEvent
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Mod(AEAdditions.ID)
object AEAdditions {

    const val ID = "ae2additions"

    init {
        Items.REGISTRY.register(MOD_BUS)
        Blocks.REGISTRY.register(MOD_BUS)
        BlockEntities.REGISTRY.register(MOD_BUS)
        MenuHolder.REGISTRY.register(MOD_BUS)
        CreativeTab.REGISTRY.register(MOD_BUS)
        Items.init()
        Blocks.init()
        BlockEntities.init()
        MenuHolder.init()


        
        MOD_BUS.addListener(::commonSetup)
        MOD_BUS.addListener(AEAdditionsDataGenerator::onGatherData)
        MOD_BUS.addListener(Capability::registerCapabilities)
//        MOD_BUS.addListener(TheOneProbe::enqueueIMC)
        MOD_BUS.addListener(::clientStuff)
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, AEAConfig.COMMON_SPEC)
        
        MinecraftForge.EVENT_BUS.addListener(::serverStarting)
        MinecraftForge.EVENT_BUS.addGenericListener(Level::class.java, Capability::registerLevelCapability)

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT) { Runnable { initClient() } }

        Integration.init()
    }
    
    fun initClient() {
        MOD_BUS.addListener(::registerItemColors)
        Models.init()
    }
    
    private fun clientStuff(event: FMLClientSetupEvent) {
        event.enqueueWork {
            Screens.init()
            ItemBlockRenderTypes.setRenderLayer(Blocks.BLOCK_CRAFTING_STORAGE_1024k.block, RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(Blocks.BLOCK_CRAFTING_STORAGE_4096k.block, RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(Blocks.BLOCK_CRAFTING_STORAGE_16384k.block, RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(Blocks.BLOCK_CRAFTING_STORAGE_65536k.block, RenderType.cutout())
        }
    }

    private fun serverStarting(event: ServerStartingEvent) {
        CommandRegistry.register(event.server.commands.dispatcher)
    }

    private fun commonSetup(event: FMLCommonSetupEvent) {
        AppEng.initCellHandler()
        Cells.init()
        InitUpgrades.init()

        event.enqueueWork {
            AEAConfig.save()
            NetworkManager.init()
        }
    }
    
    private fun registerItemColors(event: RegisterColorHandlersEvent.Item) {
        val itemColors = event.itemColors
        
        val storageCells = Items.ITEMS.filter { it is StorageCell }.toTypedArray()
        
        itemColors.register(BasicStorageCell::getColor, *storageCells)
        
//        if (Mods.AE2THINGS.isEnabled) {
//            val diskCells = Items.ITEMS.filter { it is DiskCell && it.keyType == AEKeyType.fluids() }.toTypedArray()
//            itemColors.register(DISKDrive::getColor, *diskCells)
//
//            if (Mods.APPMEK.isEnabled) {
//                val chemicalDiskCells = Items.ITEMS.filter { it is DiskCell && it.keyType == MekanismKeyType.TYPE }.toTypedArray()
//                itemColors.register(DISKDrive::getColor, *chemicalDiskCells)
//            }
//        }
    }
}