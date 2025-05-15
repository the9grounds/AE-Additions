package com.the9grounds.aeadditions.client.gui.button

import appeng.client.gui.style.Blitter
import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.client.gui.MEWirelessTransceiverScreen
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation

class CreateButton(parent: MEWirelessTransceiverScreen) : Button(0, 0, 14, 14, Component.empty(), parent::createButtonPressed,
    DEFAULT_NARRATION) {
    override fun render(guiGraphics: GuiGraphics, p_93658_: Int, p_93659_: Int, p_93660_: Float) {
        guiGraphics.pose().pushPose()
        guiGraphics.pose().translate(x.toDouble(), y.toDouble(), 0.0)
        val texture = Blitter.texture(ResourceLocation(AEAdditions.ID, "textures/gui/buttons/background.png"), 16, 16)

        texture.dest(0, 0, 16, 16).src(0, 0, 16, 16).blit(guiGraphics)
        val iconFile = "textures/gui/buttons/plus.png"
        val icon = Blitter.texture(ResourceLocation(AEAdditions.ID, iconFile), 16, 16)
        icon.dest(2, 2, 12, 12).src(0, 0, 16, 16).blit(guiGraphics)
        guiGraphics.pose().popPose()
    }
}