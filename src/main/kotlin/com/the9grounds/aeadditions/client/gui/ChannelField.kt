package com.the9grounds.aeadditions.client.gui

import appeng.client.gui.style.Blitter
import appeng.client.gui.style.Color
import com.mojang.blaze3d.vertex.PoseStack
import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.util.ChannelInfo
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation

class ChannelField(val font: Font, val channelInfo: ChannelInfo, val guiLeft: Int, val guiTop: Int): AbstractWidget(0, 0, 142, 12, Component.empty()) {
    val backgroundTexture = Blitter.texture(ResourceLocation(AEAdditions.ID, "textures/gui/text-background.png"))
    
    fun draw(poseStack: PoseStack, x: Int, y: Int, isSelected: Boolean = false) {
        this.x = guiLeft + 16 + 1 + x - 6
        this.y = guiTop + 70 + 1 + y - 6
        val text = Component.literal(channelInfo.name)
        
        if (isSelected) {
            backgroundTexture.opacity(.5f).dest(x, y, 171, 10).blit(poseStack, 2)
        }

        this.font.draw(poseStack, text, x.toFloat(), y.toFloat(), Color(58, 201, 178, 255).toARGB())
    }

    override fun updateNarration(p_169152_: NarrationElementOutput) {
        
    }
}