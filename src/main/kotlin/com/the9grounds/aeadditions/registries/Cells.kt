package com.the9grounds.aeadditions.registries

import appeng.api.client.StorageCellModels
import com.the9grounds.aeadditions.integration.Mods
import net.minecraft.resources.ResourceLocation

object Cells {
    fun init() {
        // Items
        StorageCellModels.registerModel(Items.ITEM_STORAGE_CELL_1024k, ResourceLocation("ae2additions:block/drive/cells/item/1024k"))
        StorageCellModels.registerModel(Items.ITEM_STORAGE_CELL_4096k, ResourceLocation("ae2additions:block/drive/cells/item/4096k"))
        StorageCellModels.registerModel(Items.ITEM_STORAGE_CELL_16384k, ResourceLocation("ae2additions:block/drive/cells/item/16384k"))
        StorageCellModels.registerModel(Items.ITEM_STORAGE_CELL_65536k, ResourceLocation("ae2additions:block/drive/cells/item/65536k"))

        // Fluids
        StorageCellModels.registerModel(Items.FLUID_STORAGE_CELL_1024k, ResourceLocation("ae2additions:block/drive/cells/fluid/1024k"))
        StorageCellModels.registerModel(Items.FLUID_STORAGE_CELL_4096k, ResourceLocation("ae2additions:block/drive/cells/fluid/4096k"))
        StorageCellModels.registerModel(Items.FLUID_STORAGE_CELL_16384k, ResourceLocation("ae2additions:block/drive/cells/fluid/16384k"))
        
        if (Mods.MEKANISM.isEnabled) {
            StorageCellModels.registerModel(Items.CHEMICAL_STORAGE_CELL_1024k, ResourceLocation("ae2additions:block/drive/cells/chemical/1024k"))
            StorageCellModels.registerModel(Items.CHEMICAL_STORAGE_CELL_4096k, ResourceLocation("ae2additions:block/drive/cells/chemical/4096k"))
            StorageCellModels.registerModel(Items.CHEMICAL_STORAGE_CELL_16384k, ResourceLocation("ae2additions:block/drive/cells/chemical/16384k"))
        }
    }
}