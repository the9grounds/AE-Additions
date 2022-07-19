package com.the9grounds.aeadditions.client.gui.button

import appeng.client.gui.style.Blitter
import com.mojang.blaze3d.vertex.PoseStack
import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.client.gui.MEWirelessTransceiverScreen
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.TextComponent
import net.minecraft.resources.ResourceLocation

class CreateButton(parent: MEWirelessTransceiverScreen) : Button(0, 0, 14, 14, TextComponent.EMPTY, parent::createButtonPressed) {
    override fun renderButton(poseStack: PoseStack, p_93747_: Int, p_93748_: Int, p_93749_: Float) {
        poseStack.pushPose()
        poseStack.translate(x.toDouble(), y.toDouble(), 0.0)
        val texture = Blitter.texture(ResourceLocation(AEAdditions.ID, "textures/gui/buttons/background.png"), 16, 16)
        
        texture.dest(0, 0, 16, 16).src(0, 0, 16, 16).blit(poseStack, 1)
        val iconFile = "textures/gui/buttons/plus.png"
        val icon = Blitter.texture(ResourceLocation(AEAdditions.ID, iconFile), 16, 16)
        icon.dest(2, 2, 12, 12).src(0, 0, 16, 16).blit(poseStack, 2)
        poseStack.popPose()
    }
}