package com.the9grounds.aeadditions

import appeng.api.IAEAddonEntrypoint
import appeng.api.stacks.AEKeyType
import appeng.items.storage.BasicStorageCell
import com.the9grounds.aeadditions.integration.Integration
import com.the9grounds.aeadditions.integration.Mods
import com.the9grounds.aeadditions.integration.appeng.AppEng
import com.the9grounds.aeadditions.integration.appeng.InitUpgrades
import com.the9grounds.aeadditions.item.storage.DiskCell
import com.the9grounds.aeadditions.item.storage.StorageCell
import com.the9grounds.aeadditions.registries.Blocks
import com.the9grounds.aeadditions.registries.Cells
import com.the9grounds.aeadditions.registries.Items
import com.the9grounds.aeadditions.registries.client.Models
import io.github.projectet.ae2things.item.DISKDrive
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import net.minecraft.client.renderer.RenderType

object AEAdditions : IAEAddonEntrypoint {

    const val MOD_ID = "ae2additions"

    override fun onAe2Initialized() {
        Items.init()
        Blocks.init()
        Integration.init()
        AppEng.initCellHandler()
        Cells.init()
        InitUpgrades.init()
    }
    
    fun registerItemColors() {
        val itemColorsRegister = ColorProviderRegistry.ITEM::register

        val storageCells = Items.ITEMS.filter { it is StorageCell }.toTypedArray()

        itemColorsRegister(BasicStorageCell::getColor, storageCells)

        if (Mods.AE2THINGS.isEnabled) {
            val diskCells = Items.ITEMS.filter { it is DiskCell && it.keyType == AEKeyType.fluids() }.toTypedArray()
            itemColorsRegister(DISKDrive::getColor, diskCells)
        }
    }

    @Environment(EnvType.CLIENT)
    fun registerModels() {
        Models.init()
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.BLOCK_CRAFTING_STORAGE_1024k.block, RenderType.cutout())
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.BLOCK_CRAFTING_STORAGE_4096k.block, RenderType.cutout())
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.BLOCK_CRAFTING_STORAGE_16384k.block, RenderType.cutout())
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.BLOCK_CRAFTING_STORAGE_65536k.block, RenderType.cutout())
    }
}