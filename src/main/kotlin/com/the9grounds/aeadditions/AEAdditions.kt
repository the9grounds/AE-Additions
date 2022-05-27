package com.the9grounds.aeadditions

import appeng.block.crafting.AbstractCraftingUnitBlock
import appeng.items.storage.BasicStorageCell
import com.the9grounds.aeadditions.data.AEAdditionsDataGenerator
import com.the9grounds.aeadditions.integration.Integration
import com.the9grounds.aeadditions.integration.Mods
import com.the9grounds.aeadditions.integration.appeng.AppEng
import com.the9grounds.aeadditions.item.storage.DiskCell
import com.the9grounds.aeadditions.item.storage.StorageCell
import com.the9grounds.aeadditions.registries.Blocks
import com.the9grounds.aeadditions.registries.Cells
import com.the9grounds.aeadditions.registries.Items
import com.the9grounds.aeadditions.registries.client.Models
import io.github.projectet.ae2things.item.DISKDrive
import net.minecraft.client.renderer.ItemBlockRenderTypes
import net.minecraft.client.renderer.RenderType
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.ColorHandlerEvent
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Mod(AEAdditions.ID)
object AEAdditions {

    const val ID = "ae2additions"

    var craftingType1024k: AbstractCraftingUnitBlock.CraftingUnitType? = null
    var craftingType4096k: AbstractCraftingUnitBlock.CraftingUnitType? = null
    var craftingType16384k: AbstractCraftingUnitBlock.CraftingUnitType? = null
    var craftingType65536k: AbstractCraftingUnitBlock.CraftingUnitType? = null

    init {
        initCraftingUnitTypes()
        Items.init()
        Blocks.init()
        Items.REGISTRY.register(MOD_BUS)
        Blocks.REGISTRY.register(MOD_BUS)
        
        MOD_BUS.addListener(::commonSetup)
        MOD_BUS.addListener(AEAdditionsDataGenerator::onGatherData)

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT) { Runnable { initClient() } }

        Integration.init()
    }
    
    fun initClient() {
        MOD_BUS.addListener(::modelRegistryEvent)
    }

    private fun commonSetup(event: FMLCommonSetupEvent) {
        AppEng.initCellHandler()
        Cells.init()
    }
    
    private fun registerItemColors(event: ColorHandlerEvent.Item) {
        val itemColors = event.itemColors
        
        val storageCells = Items.ITEMS.filter { it is StorageCell }.toTypedArray()
        
        itemColors.register(BasicStorageCell::getColor, *storageCells)
        
        if (Mods.AE2THINGS.isEnabled) {
            val diskCells = Items.ITEMS.filter { it is DiskCell }.toTypedArray()
            itemColors.register(DISKDrive::getColor, *diskCells)
        }
    }
    
    private fun initCraftingUnitTypes() {
        for (value in AbstractCraftingUnitBlock.CraftingUnitType.values()) {
            when (value.name) {
                "STORAGE_1024k" -> craftingType1024k = value
                "STORAGE_4096k" -> craftingType4096k = value
                "STORAGE_16384k" -> craftingType16384k = value
                "STORAGE_65536k" -> craftingType65536k = value
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