package com.the9grounds.aeadditions.registries

import com.the9grounds.aeadditions.AEAdditions
import net.minecraft.util.ResourceLocation

object Ids {

    // Components
    val ITEM_STORAGE_COMPONENT_256 = id("item_storage_component_256")
    val ITEM_STORAGE_COMPONENT_1024 = id("item_storage_component_1024")
    val ITEM_STORAGE_COMPONENT_4096 = id("item_storage_component_4096")
    val ITEM_STORAGE_COMPONENT_16384 = id("item_storage_component_16384")
    val FLUID_STORAGE_COMPONENT_256 = id("fluid_storage_component_256")
    val FLUID_STORAGE_COMPONENT_1024 = id("fluid_storage_component_1024")
    val FLUID_STORAGE_COMPONENT_4096 = id("fluid_storage_component_4096")
    
    val CHEMICAL_STORAGE_COMPONENT_1 = id("chemical_storage_component_1")
    val CHEMICAL_STORAGE_COMPONENT_4 = id("chemical_storage_component_4")
    val CHEMICAL_STORAGE_COMPONENT_16 = id("chemical_storage_component_16")
    val CHEMICAL_STORAGE_COMPONENT_64 = id("chemical_storage_component_64")
    val CHEMICAL_STORAGE_COMPONENT_256 = id("chemical_storage_component_256")
    val CHEMICAL_STORAGE_COMPONENT_1024 = id("chemical_storage_component_1024")
    val CHEMICAL_STORAGE_COMPONENT_4096 = id("chemical_storage_component_4096")

    // Cells
    val ITEM_STORAGE_CELL_256 = id("item_storage_cell_256")
    val ITEM_STORAGE_CELL_1024 = id("item_storage_cell_1024")
    val ITEM_STORAGE_CELL_4096 = id("item_storage_cell_4096")
    val ITEM_STORAGE_CELL_16384 = id("item_storage_cell_16384")

    val FLUID_STORAGE_CELL_256 = id("fluid_storage_cell_256")
    val FLUID_STORAGE_CELL_1024 = id("fluid_storage_cell_1024")
    val FLUID_STORAGE_CELL_4096 = id("fluid_storage_cell_4096")
    
    val CHEMICAL_STORAGE_CELL_1 = id("chemical_storage_cell_1")
    val CHEMICAL_STORAGE_CELL_4 = id("chemical_storage_cell_4")
    val CHEMICAL_STORAGE_CELL_16 = id("chemical_storage_cell_16")
    val CHEMICAL_STORAGE_CELL_64 = id("chemical_storage_cell_64")
    val CHEMICAL_STORAGE_CELL_256 = id("chemical_storage_cell_256")
    val CHEMICAL_STORAGE_CELL_1024 = id("chemical_storage_cell_1024")
    val CHEMICAL_STORAGE_CELL_4096 = id("chemical_storage_cell_4096")
    
    val DUMMY_CHEMICAL_ITEM = id("dummy_chemical_item")


    
    // Parts
    
    val CHEMICAL_TERMINAL_PART = id("chemical_terminal")
    val CHEMICAL_STORAGE_MONITOR = id("chemical_storage_monitor")
    val CHEMICAL_CONVERSION_MONITOR = id("chemical_conversion_monitor")
    val CHEMICAL_IMPORT_BUS = id("chemical_import_bus")
    val CHEMICAL_EXPORT_BUS = id("chemical_export_bus")
    
    //Fluid
    val FLUID_STORAGE_MONITOR = id("fluid_storage_monitor")
    val FLUID_CONVERSION_MONITOR = id("fluid_conversion_monitor")
    
    //Blocks
    val CHEMICAL_INTERFACE = id("chemical_interface")

    private fun id(id: String): ResourceLocation = ResourceLocation(AEAdditions.ID, id)
}