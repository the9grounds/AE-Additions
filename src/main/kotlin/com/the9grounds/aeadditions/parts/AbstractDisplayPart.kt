package com.the9grounds.aeadditions.parts

import appeng.core.AppEng
import appeng.items.parts.PartModels
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation

abstract class AbstractDisplayPart(itemStack: ItemStack) : AbstractReportingPart(itemStack, true) {

    companion object {
        @PartModels
        val MODEL_BASE = ResourceLocation(AppEng.MOD_ID, "part/display_base")

        @PartModels
        val MODEL_STATUS_OFF = ResourceLocation(AppEng.MOD_ID, "part/display_status_off")

        @PartModels
        val MODEL_STATUS_ON = ResourceLocation(AppEng.MOD_ID, "part/display_status_on")

        @PartModels
        val MODEL_STATUS_HAS_CHANNEL = ResourceLocation(AppEng.MOD_ID, "part/display_status_has_channel")
    }

    override fun isLightSource(): Boolean = false
}