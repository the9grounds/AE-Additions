package com.the9grounds.aeadditions.client.gui

import appeng.client.gui.style.Blitter
import appeng.client.gui.style.Color
import com.mojang.blaze3d.vertex.PoseStack
import com.the9grounds.aeadditions.AEAdditions
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.TextComponent
import net.minecraft.resources.ResourceLocation

class ExtraDataPane(x: Int, y: Int, val screen: MEWirelessTransceiverScreen, val font: Font) : AbstractWidget(x, y, 128, 24, TextComponent.EMPTY) {
    
    val texture = Blitter.texture(ResourceLocation(AEAdditions.ID, "textures/gui/extra-data-panel.png"))

    override fun render(poseStack: PoseStack, p_93658_: Int, p_93659_: Int, p_93660_: Float) {
        poseStack.pushPose()
        poseStack.translate(this.x.toDouble(), this.y.toDouble(), 0.0)

        texture.dest(0, 0, 128, 24).blit(poseStack, 1)
        val usedChannels = screen.menu.ae2ChannelUsage
        val maxChannels = screen.menu.ae2MaxChannels
        
        font.draw(poseStack, TextComponent("AE2 Channels: $usedChannels/$maxChannels"), 6.toFloat(), 2.toFloat(), Color(0, 0, 0, 255).toARGB())
        
        poseStack.popPose()
    }
    
    override fun updateNarration(p_169152_: NarrationElementOutput) {
        
    }
}