package com.the9grounds.aeadditions.registries

import com.the9grounds.aeadditions.integration.appeng.AppEng
import net.minecraft.util.ResourceLocation

object Cells {
    fun init() {
        // Items
        AppEng.API!!.client().cells().registerModel(Items.ITEM_STORAGE_CELL_256k, ResourceLocation("aeadditions:block/drive/cells/item/256k"))
        AppEng.API!!.client().cells().registerModel(Items.ITEM_STORAGE_CELL_1024k, ResourceLocation("aeadditions:block/drive/cells/item/1024k"))
        AppEng.API!!.client().cells().registerModel(Items.ITEM_STORAGE_CELL_4096k, ResourceLocation("aeadditions:block/drive/cells/item/4096k"))
        AppEng.API!!.client().cells().registerModel(Items.ITEM_STORAGE_CELL_16384k, ResourceLocation("aeadditions:block/drive/cells/item/16384k"))

        // Fluids
        AppEng.API!!.client().cells().registerModel(Items.FLUID_STORAGE_CELL_256k, ResourceLocation("aeadditions:block/drive/cells/fluid/256k"))
        AppEng.API!!.client().cells().registerModel(Items.FLUID_STORAGE_CELL_1024k, ResourceLocation("aeadditions:block/drive/cells/fluid/1024k"))
        AppEng.API!!.client().cells().registerModel(Items.FLUID_STORAGE_CELL_4096k, ResourceLocation("aeadditions:block/drive/cells/fluid/4096k"))
        
        AppEng.API!!.client().cells().registerModel(Items.CHEMICAL_STORAGE_CELL_1k, ResourceLocation("aeadditions:block/drive/cells/chemical/1k"))
        AppEng.API!!.client().cells().registerModel(Items.CHEMICAL_STORAGE_CELL_4k, ResourceLocation("aeadditions:block/drive/cells/chemical/4k"))
        AppEng.API!!.client().cells().registerModel(Items.CHEMICAL_STORAGE_CELL_16k, ResourceLocation("aeadditions:block/drive/cells/chemical/16k"))
        AppEng.API!!.client().cells().registerModel(Items.CHEMICAL_STORAGE_CELL_64k, ResourceLocation("aeadditions:block/drive/cells/chemical/64k"))
        AppEng.API!!.client().cells().registerModel(Items.CHEMICAL_STORAGE_CELL_256k, ResourceLocation("aeadditions:block/drive/cells/chemical/256k"))
        AppEng.API!!.client().cells().registerModel(Items.CHEMICAL_STORAGE_CELL_1024k, ResourceLocation("aeadditions:block/drive/cells/chemical/1024k"))
        AppEng.API!!.client().cells().registerModel(Items.CHEMICAL_STORAGE_CELL_4096k, ResourceLocation("aeadditions:block/drive/cells/chemical/4096k"))
    }
}