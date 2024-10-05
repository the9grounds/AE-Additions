package com.the9grounds.aeadditions.client.gui.button

import appeng.client.gui.style.Blitter
import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.client.gui.MEWirelessTransceiverScreen
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation

class AccessButton(var parent: MEWirelessTransceiverScreen, var isPrivate: Boolean = false) : Button(Builder(Component.empty(), parent::accessButtonPressed).size(16, 16)) {

    override fun render(guiGraphics: GuiGraphics, p_93658_: Int, p_93659_: Int, p_93660_: Float) {
        this.tooltip = parent.accessBtnTooltip(this)
        guiGraphics.pose().pushPose()
        guiGraphics.pose().translate(x.toDouble(), y.toDouble(), 0.0)
        val texture = Blitter.texture(ResourceLocation(AEAdditions.ID, "textures/gui/buttons/background.png"), 16, 16)

        texture.dest(0, 0, 20, 20).src(0, 0, 16, 16).blit(guiGraphics)
        val iconFile = if (isPrivate) {
            "textures/gui/buttons/private.png"
        } else {
            "textures/gui/buttons/public.png"
        }
        val icon = Blitter.texture(ResourceLocation(AEAdditions.ID, iconFile), 16, 16)
        icon.dest(4, 3, 12, 12).src(0, 0, 16, 16).blit(guiGraphics)
        guiGraphics.pose().popPose()
        isHovered = p_93658_ >= x && p_93659_ >= y && p_93658_ < x + width && p_93659_ < y + height
        super.updateTooltip();
    }
}