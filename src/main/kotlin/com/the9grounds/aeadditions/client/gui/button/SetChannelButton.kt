package com.the9grounds.aeadditions.client.gui.button

import appeng.client.gui.style.Blitter
import appeng.client.gui.style.Color
import com.mojang.blaze3d.vertex.PoseStack
import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.client.gui.MEWirelessTransceiverScreen
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.TextComponent
import net.minecraft.resources.ResourceLocation

class SetChannelButton(parent: MEWirelessTransceiverScreen, val font: Font) : Button(0, 0, 14, 14, TextComponent.EMPTY, parent::setChannelButtonPressed) {
    override fun renderButton(poseStack: PoseStack, p_93747_: Int, p_93748_: Int, p_93749_: Float) {
        poseStack.pushPose()
        poseStack.translate(x.toDouble(), y.toDouble(), 0.0)
        val texture = Blitter.texture(ResourceLocation(AEAdditions.ID, "textures/gui/buttons/background.png"), 16, 16)
        
        texture.dest(0, 0, 40, 16).src(0, 0, 16, 16).blit(poseStack, 1)
        font.draw(poseStack, "Set", 12f, 4f, Color(0, 0, 0, 1).toARGB())
        poseStack.popPose()
    }
}