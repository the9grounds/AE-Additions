package com.the9grounds.aeadditions.registries

import appeng.api.client.StorageCellModels
import com.the9grounds.aeadditions.integration.Mods
import net.minecraft.resources.ResourceLocation

object Cells {
    fun init() {
        // Items
        StorageCellModels.registerModel(Items.ITEM_STORAGE_CELL_1024k, ResourceLocation.parse("ae2additions:block/drive/cells/item/1024k"))
        StorageCellModels.registerModel(Items.ITEM_STORAGE_CELL_4096k, ResourceLocation.parse("ae2additions:block/drive/cells/item/4096k"))
        StorageCellModels.registerModel(Items.ITEM_STORAGE_CELL_16384k, ResourceLocation.parse("ae2additions:block/drive/cells/item/16384k"))
        StorageCellModels.registerModel(Items.ITEM_STORAGE_CELL_65536k, ResourceLocation.parse("ae2additions:block/drive/cells/item/65536k"))

        // Fluids
        StorageCellModels.registerModel(Items.FLUID_STORAGE_CELL_1024k, ResourceLocation.parse("ae2additions:block/drive/cells/fluid/1024k"))
        StorageCellModels.registerModel(Items.FLUID_STORAGE_CELL_4096k, ResourceLocation.parse("ae2additions:block/drive/cells/fluid/4096k"))
        StorageCellModels.registerModel(Items.FLUID_STORAGE_CELL_16384k, ResourceLocation.parse("ae2additions:block/drive/cells/fluid/16384k"))
        
        StorageCellModels.registerModel(Items.SUPER_CELL_1k, ResourceLocation.parse("ae2additions:block/drive/cells/super/1k"))
        StorageCellModels.registerModel(Items.SUPER_CELL_4k, ResourceLocation.parse("ae2additions:block/drive/cells/super/4k"))
        StorageCellModels.registerModel(Items.SUPER_CELL_16k, ResourceLocation.parse("ae2additions:block/drive/cells/super/16k"))
        StorageCellModels.registerModel(Items.SUPER_CELL_64k, ResourceLocation.parse("ae2additions:block/drive/cells/super/64k"))
        StorageCellModels.registerModel(Items.SUPER_CELL_256k, ResourceLocation.parse("ae2additions:block/drive/cells/super/256k"))
        StorageCellModels.registerModel(Items.SUPER_CELL_1024k, ResourceLocation.parse("ae2additions:block/drive/cells/super/1024k"))
        StorageCellModels.registerModel(Items.SUPER_CELL_4096k, ResourceLocation.parse("ae2additions:block/drive/cells/super/4096k"))
        StorageCellModels.registerModel(Items.SUPER_CELL_16M, ResourceLocation.parse("ae2additions:block/drive/cells/super/16m"))
        StorageCellModels.registerModel(Items.SUPER_CELL_65M, ResourceLocation.parse("ae2additions:block/drive/cells/super/65m"))
        
        if (Mods.APPMEK.isEnabled) {
            StorageCellModels.registerModel(Items.CHEMICAL_STORAGE_CELL_1024k, ResourceLocation.parse("ae2additions:block/drive/cells/chemical/1024k"))
            StorageCellModels.registerModel(Items.CHEMICAL_STORAGE_CELL_4096k, ResourceLocation.parse("ae2additions:block/drive/cells/chemical/4096k"))
            StorageCellModels.registerModel(Items.CHEMICAL_STORAGE_CELL_16384k, ResourceLocation.parse("ae2additions:block/drive/cells/chemical/16384k"))
        }
//        if (Mods.AE2THINGS.isEnabled) {
//            StorageCellModels.registerModel(Items.DISK_256k, ResourceLocation.parse("ae2additions:block/drive/cells/item/disk_256k"))
//            StorageCellModels.registerModel(Items.DISK_1024k, ResourceLocation.parse("ae2additions:block/drive/cells/item/disk_1024k"))
//            StorageCellModels.registerModel(Items.DISK_4096k, ResourceLocation.parse("ae2additions:block/drive/cells/item/disk_4096k"))
//            StorageCellModels.registerModel(Items.DISK_16384k, ResourceLocation.parse("ae2additions:block/drive/cells/item/disk_16384k"))
//            StorageCellModels.registerModel(Items.DISK_65536k, ResourceLocation.parse("ae2additions:block/drive/cells/item/disk_65536k"))
//            
//            StorageCellModels.registerModel(Items.DISK_FLUID_1k, ResourceLocation.parse("ae2additions:block/drive/cells/fluid/disk_1k"))
//            StorageCellModels.registerModel(Items.DISK_FLUID_4k, ResourceLocation.parse("ae2additions:block/drive/cells/fluid/disk_4k"))
//            StorageCellModels.registerModel(Items.DISK_FLUID_16k, ResourceLocation.parse("ae2additions:block/drive/cells/fluid/disk_16k"))
//            StorageCellModels.registerModel(Items.DISK_FLUID_64k, ResourceLocation.parse("ae2additions:block/drive/cells/fluid/disk_64k"))
//            StorageCellModels.registerModel(Items.DISK_FLUID_256k, ResourceLocation.parse("ae2additions:block/drive/cells/fluid/disk_256k"))
//            StorageCellModels.registerModel(Items.DISK_FLUID_1024k, ResourceLocation.parse("ae2additions:block/drive/cells/fluid/disk_1024k"))
//            StorageCellModels.registerModel(Items.DISK_FLUID_4096k, ResourceLocation.parse("ae2additions:block/drive/cells/fluid/disk_4096k"))
//            StorageCellModels.registerModel(Items.DISK_FLUID_16384k, ResourceLocation.parse("ae2additions:block/drive/cells/fluid/disk_16384k"))
//            StorageCellModels.registerModel(Items.DISK_FLUID_65536k, ResourceLocation.parse("ae2additions:block/drive/cells/fluid/disk_65536k"))
//            
//            if (Mods.APPMEK.isEnabled) {
//                StorageCellModels.registerModel(Items.DISK_CHEMICAL_1k, ResourceLocation.parse("ae2additions:block/drive/cells/chemical/disk_1k"))
//                StorageCellModels.registerModel(Items.DISK_CHEMICAL_4k, ResourceLocation.parse("ae2additions:block/drive/cells/chemical/disk_4k"))
//                StorageCellModels.registerModel(Items.DISK_CHEMICAL_16k, ResourceLocation.parse("ae2additions:block/drive/cells/chemical/disk_16k"))
//                StorageCellModels.registerModel(Items.DISK_CHEMICAL_64k, ResourceLocation.parse("ae2additions:block/drive/cells/chemical/disk_64k"))
//                StorageCellModels.registerModel(Items.DISK_CHEMICAL_256k, ResourceLocation.parse("ae2additions:block/drive/cells/chemical/disk_256k"))
//                StorageCellModels.registerModel(Items.DISK_CHEMICAL_1024k, ResourceLocation.parse("ae2additions:block/drive/cells/chemical/disk_1024k"))
//                StorageCellModels.registerModel(Items.DISK_CHEMICAL_4096k, ResourceLocation.parse("ae2additions:block/drive/cells/chemical/disk_4096k"))
//                StorageCellModels.registerModel(Items.DISK_CHEMICAL_16384k, ResourceLocation.parse("ae2additions:block/drive/cells/chemical/disk_16384k"))
//                StorageCellModels.registerModel(Items.DISK_CHEMICAL_65536k, ResourceLocation.parse("ae2additions:block/drive/cells/chemical/disk_65536k"))
//            }
//        }
    }
}