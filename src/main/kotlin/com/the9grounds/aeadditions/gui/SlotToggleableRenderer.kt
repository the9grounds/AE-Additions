package com.the9grounds.aeadditions.gui

import com.the9grounds.aeadditions.container.slot.SlotDisabled
import com.the9grounds.aeadditions.container.slot.ToggleableSlot
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.inventory.Slot
import net.minecraft.util.ResourceLocation

object SlotToggleableRenderer : ISlotRenderer {

    private val TEXTURE_LOCATION = ResourceLocation("appliedenergistics2", "textures/guis/states.png")

    override fun renderBackground(slot: Slot?, gui: GuiBase<*>?, mouseX: Int, mouseY: Int) {
        GlStateManager.disableLighting()
        GlStateManager.enableBlend()
        if (slot is ToggleableSlot && !slot.isSlotEnabled()) {
//            GlStateManager.color(0f, 0f, 0f, .8f)
            Gui.drawRect(slot!!.xPos, slot!!.yPos, slot!!.xPos + 16, slot!!.yPos + 16, 0x99000000.toInt())
                // 0xCC000000
//            gui!!.drawTexturedModalRect()
//            gui!!.drawTexturedModalRect(slot!!.xPos, slot!!.yPos, 0, 0, 16, 16)
        } else {
            GlStateManager.color(1f, 1f, 1f, 0.5f)
            gui!!.mc.textureManager.bindTexture(TEXTURE_LOCATION)
            gui!!.drawTexturedModalRect(slot!!.xPos, slot!!.yPos, 240, 112, 16, 16)
        }
        GlStateManager.disableBlend()
        GlStateManager.enableLighting()
    }
}