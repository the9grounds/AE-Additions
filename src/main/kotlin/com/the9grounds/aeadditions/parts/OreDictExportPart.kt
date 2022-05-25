package com.the9grounds.aeadditions.parts

import appeng.core.AppEng
import appeng.items.parts.PartModels
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation

class OreDictExportPart(itemStack: ItemStack) : AEABasePart(itemStack) {

    companion object {
        @PartModels
        val MODEL_BASE = ResourceLocation(AppEng.MOD_ID, "part/export_bus_base")

        @PartModels
        val MODEL_STATUS_OFF = ResourceLocation(AppEng.MOD_ID, "part/export_bus_off")

        @PartModels
        val MODEL_STATUS_ON = ResourceLocation(AppEng.MOD_ID, "part/export_bus_on")

        @PartModels
        val MODEL_STATUS_HAS_CHANNEL = ResourceLocation(AppEng.MOD_ID, "part/export_bus_has_channel")
    }
    
    var filter = ""
    
    var filterFunc: ((ItemStack) -> Boolean)? = null
    
    var oreDictFilteredItems = listOf<ItemStack>()
    
    fun updateFilter() {
        if (!filter.trim().isEmpty()) {
            
        }
    }
}