package com.the9grounds.aeadditions.client.gui.button

import appeng.client.gui.style.Blitter
import appeng.client.gui.style.Color
import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.client.gui.MEWirelessTransceiverScreen
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation

class DeleteChannelButton(parent: MEWirelessTransceiverScreen, val font: Font) : Button(Builder(Component.empty(), parent::deleteChannelButtonPressed).size(40, 16).pos(0, 0)) {
    override fun render(guiGraphics: GuiGraphics, p_93658_: Int, p_93659_: Int, p_93660_: Float) {
        val texture = Blitter.texture(ResourceLocation(AEAdditions.ID, "textures/gui/buttons/background.png"), 16, 16)

        texture.dest(this.x, this.y, 40, 16).src(0, 0, 16, 16).blit(guiGraphics)
        font.drawInBatch("Delete", this.x + 6f, this.y + 4f, Color(0, 0, 0, 255).toARGB(), false, guiGraphics.pose().last().pose(), guiGraphics.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880)
    }
}