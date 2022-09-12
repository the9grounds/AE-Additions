package com.the9grounds.aeadditions

import appeng.api.stacks.AEKeyType
import appeng.items.storage.BasicStorageCell
import com.the9grounds.aeadditions.core.AEAConfig
import com.the9grounds.aeadditions.core.network.NetworkManager
import com.the9grounds.aeadditions.data.AEAdditionsDataGenerator
import com.the9grounds.aeadditions.debug.CommandRegistry
import com.the9grounds.aeadditions.integration.Integration
import com.the9grounds.aeadditions.integration.Mods
import com.the9grounds.aeadditions.integration.appeng.AppEng
import com.the9grounds.aeadditions.integration.appeng.InitUpgrades
import com.the9grounds.aeadditions.integration.theoneprobe.TheOneProbe
import com.the9grounds.aeadditions.item.storage.DiskCell
import com.the9grounds.aeadditions.item.storage.StorageCell
import com.the9grounds.aeadditions.menu.MenuHolder
import com.the9grounds.aeadditions.registries.*
import com.the9grounds.aeadditions.registries.client.Models
import com.the9grounds.aeadditions.registries.client.Screens
import io.github.projectet.ae2things.item.DISKDrive
import me.ramidzkh.mekae2.ae2.MekanismKeyType
import net.minecraft.client.renderer.ItemBlockRenderTypes
import net.minecraft.client.renderer.RenderType
import net.minecraft.world.level.Level
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.ColorHandlerEvent
import net.minecraftforge.client.event.ModelRegistryEvent
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
        Items.init()
        Blocks.init()
        BlockEntities.init()
        MenuHolder.init()
        
        MOD_BUS.addListener(::commonSetup)
        MOD_BUS.addListener(AEAdditionsDataGenerator::onGatherData)
        MOD_BUS.addListener(Capability::registerCapabilities)
        MOD_BUS.addListener(TheOneProbe::enqueueIMC)
        MOD_BUS.addListener(::clientStuff)
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, AEAConfig.COMMON_SPEC)
        
        MinecraftForge.EVENT_BUS.addListener(::serverStarting)
        MinecraftForge.EVENT_BUS.addGenericListener(Level::class.java, Capability::registerLevelCapability)

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT) { Runnable { initClient() } }

        Integration.init()
    }
    
    fun initClient() {
        MOD_BUS.addListener(::modelRegistryEvent)
        MOD_BUS.addListener(::registerItemColors)
    }
    
    private fun clientStuff(event: FMLClientSetupEvent) {
        event.enqueueWork {
            Screens.init()
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
        
        if (Mods.MEGAAE2.isEnabled) {
            for (item in listOf(
                Items.CELL_COMPONENT_1024k,
                Items.CELL_COMPONENT_4096k,
                Items.CELL_COMPONENT_16384k,
                Items.CELL_COMPONENT_65536k,
                Items.ITEM_STORAGE_CELL_1024k,
                Items.ITEM_STORAGE_CELL_4096k,
                Items.ITEM_STORAGE_CELL_16384k,
                Items.ITEM_STORAGE_CELL_65536k,
                Items.FLUID_STORAGE_CELL_1024k,
                Items.FLUID_STORAGE_CELL_4096k,
                Items.FLUID_STORAGE_CELL_16384k,
                Items.CHEMICAL_STORAGE_CELL_1024k,
                Items.CHEMICAL_STORAGE_CELL_4096k,
                Items.CHEMICAL_STORAGE_CELL_16384k,
            )) {
                item.category = null;
            }
        }
    }
    
    private fun registerItemColors(event: ColorHandlerEvent.Item) {
        val itemColors = event.itemColors
        
        val storageCells = Items.ITEMS.filter { it is StorageCell }.toTypedArray()
        
        itemColors.register(BasicStorageCell::getColor, *storageCells)
        
        if (Mods.AE2THINGS.isEnabled) {
            val diskCells = Items.ITEMS.filter { it is DiskCell && it.keyType == AEKeyType.fluids() }.toTypedArray()
            itemColors.register(DISKDrive::getColor, *diskCells)

            if (Mods.APPMEK.isEnabled) {
                val chemicalDiskCells = Items.ITEMS.filter { it is DiskCell && it.keyType == MekanismKeyType.TYPE }.toTypedArray()
                itemColors.register(DISKDrive::getColor, *chemicalDiskCells)
            }
        }
    }

    private fun modelRegistryEvent(event: ModelRegistryEvent) {
        Models.init()
        ItemBlockRenderTypes.setRenderLayer(Blocks.BLOCK_CRAFTING_STORAGE_1024k.block, RenderType.cutout())
        ItemBlockRenderTypes.setRenderLayer(Blocks.BLOCK_CRAFTING_STORAGE_4096k.block, RenderType.cutout())
        ItemBlockRenderTypes.setRenderLayer(Blocks.BLOCK_CRAFTING_STORAGE_16384k.block, RenderType.cutout())
        ItemBlockRenderTypes.setRenderLayer(Blocks.BLOCK_CRAFTING_STORAGE_65536k.block, RenderType.cutout())
    }
}