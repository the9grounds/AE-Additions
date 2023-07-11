package com.the9grounds.aeadditions.client.gui

import appeng.client.gui.style.Blitter
import appeng.client.gui.style.Color
import com.the9grounds.aeadditions.AEAdditions
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation

class ExtraDataPane(x: Int, y: Int, val screen: MEWirelessTransceiverScreen, val font: Font) : AbstractWidget(x, y, 128, 24, Component.empty()) {
    
    val texture = Blitter.texture(ResourceLocation(AEAdditions.ID, "textures/gui/extra-data-panel.png"))

    override fun renderWidget(guiGraphics: GuiGraphics, p_268034_: Int, p_268009_: Int, p_268085_: Float) {
        guiGraphics.pose().pushPose()
        guiGraphics.pose().translate(this.x.toDouble(), this.y.toDouble(), 0.0)

        texture.dest(0, 0, 128, 24).blit(guiGraphics)
        val usedChannels = screen.menu.ae2ChannelUsage
        val maxChannels = screen.menu.ae2MaxChannels

        font.drawInBatch(Component.literal("AE2 Channels: $usedChannels/$maxChannels"), 6.toFloat(), 2.toFloat(), Color(0, 0, 0, 255).toARGB(), false, guiGraphics.pose().last().pose(), guiGraphics.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880)

        guiGraphics.pose().popPose()
    }

    override fun updateWidgetNarration(p_259858_: NarrationElementOutput) {

    }
}