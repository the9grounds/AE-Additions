package com.the9grounds.aeadditions.parts

import appeng.core.AppEng
import appeng.items.parts.PartModels
import com.the9grounds.aeadditions.container.ContainerOpener
import com.the9grounds.aeadditions.container.Locator
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.container.ContainerType
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.vector.Vector3d

abstract class AbstractDisplayPart(itemStack: ItemStack) : AbstractReportingPart(itemStack, true) {

    companion object {
        @PartModels val MODEL_BASE = ResourceLocation(AppEng.MOD_ID, "part/display_base")

        @PartModels val MODEL_STATUS_OFF = ResourceLocation(AppEng.MOD_ID, "part/display_status_off")

        @PartModels val MODEL_STATUS_ON = ResourceLocation(AppEng.MOD_ID, "part/display_status_on")

        @PartModels val MODEL_STATUS_HAS_CHANNEL = ResourceLocation(AppEng.MOD_ID, "part/display_status_has_channel")
    }

    override fun onPartActivate(player: PlayerEntity?, hand: Hand?, pos: Vector3d?): Boolean {
        if (!super.onPartActivate(player, hand, pos)) {
            ContainerOpener.openContainer(getContainerType(), player!!, Locator.forPart(this))
        }
        
        return true
    }
    
    abstract fun getContainerType(): ContainerType<*>

    override fun isLightSource(): Boolean = false
}