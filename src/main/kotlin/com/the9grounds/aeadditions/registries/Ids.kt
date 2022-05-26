package com.the9grounds.aeadditions.registries

import com.the9grounds.aeadditions.AEAdditions
import net.minecraft.resources.ResourceLocation

object Ids {

    // Components
    val CELL_COMPONENT_1024 = id("cell_component_1024")
    val CELL_COMPONENT_4096 = id("cell_component_4096")
    val CELL_COMPONENT_16384 = id("cell_component_16384")
    val CELL_COMPONENT_65536 = id("cell_component_65536")

    // Cells
    val ITEM_STORAGE_CELL_1024 = id("item_storage_cell_1024")
    val ITEM_STORAGE_CELL_4096 = id("item_storage_cell_4096")
    val ITEM_STORAGE_CELL_16384 = id("item_storage_cell_16384")
    val ITEM_STORAGE_CELL_65536 = id("item_storage_cell_65536")
    
    val FLUID_STORAGE_CELL_1024 = id("fluid_storage_cell_1024")
    val FLUID_STORAGE_CELL_4096 = id("fluid_storage_cell_4096")
    val FLUID_STORAGE_CELL_16384 = id("fluid_storage_cell_16384")
    
    val CHEMICAL_STORAGE_CELL_1024 = id("chemical_storage_cell_1024")
    val CHEMICAL_STORAGE_CELL_4096 = id("chemical_storage_cell_4096")
    val CHEMICAL_STORAGE_CELL_16384 = id("chemical_storage_cell_16384")
    

    private fun id(id: String): ResourceLocation = ResourceLocation(AEAdditions.ID, id)
}