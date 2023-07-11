package com.the9grounds.aeadditions.client.gui

import appeng.client.gui.style.Blitter
import appeng.client.gui.style.Color
import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.util.ChannelInfo
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation

class ChannelField(val font: Font, val channelInfo: ChannelInfo, val guiLeft: Int, val guiTop: Int): AbstractWidget(0, 0, 142, 12, Component.empty()) {
    val backgroundTexture = Blitter.texture(ResourceLocation(AEAdditions.ID, "textures/gui/text-background.png"))
    var shouldShow = false
    var isSelected = false
    var yValue = 0

    fun updateValues(shouldShow: Boolean, isSelected: Boolean, yValue: Int) {
        this.shouldShow = shouldShow
        this.isSelected = isSelected
        this.yValue = yValue
    }
    
    fun draw(guiGraphics: GuiGraphics, x: Int, y: Int, isSelected: Boolean = false) {

    }

    override fun renderWidget(guiGraphics: GuiGraphics, p_268034_: Int, p_268009_: Int, p_268085_: Float) {
        if (!shouldShow) {
            return
        }
        val x = 0
        this.x = guiLeft + 16 + 1 + x - 6
        this.y = guiTop + 70 + 1 + yValue - 6
        val text = Component.literal(channelInfo.name)

        if (isSelected) {
            backgroundTexture.opacity(.5f).dest(x, yValue, 171, 10).blit(guiGraphics)
        }

        this.font.drawInBatch(text, x.toFloat(), yValue.toFloat(), Color(58, 201, 178, 255).toARGB(), false, guiGraphics.pose().last().pose(), guiGraphics.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880)
    }

    override fun updateWidgetNarration(p_259858_: NarrationElementOutput) {

    }
}