package com.the9grounds.aeadditions.client.gui.widget

import appeng.core.localization.GuiText
import com.mojang.blaze3d.matrix.MatrixStack
import com.the9grounds.aeadditions.client.helpers.Blit
import net.minecraft.client.gui.AbstractGui
import net.minecraft.client.gui.FontRenderer
import net.minecraft.inventory.container.Slot
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextFormatting

class ToolboxPanel(val xPos: Int, val yPos: Int, val slots: List<Slot>, val toolboxName: ITextComponent) : AbstractGui(), IWidget {
    
    companion object {
        val background = Blit("gui/extra_panels.png", 128, 128).src(60, 60, 68, 68)
    }
    
    init {
        
        val padding = 7
        val size = 18

        var slotOriginX = xPos + padding
        var slotOriginY = yPos + padding
        
        slots.forEachIndexed { index, slot -> 
            slot.xPos = slotOriginX + 1
            slot.yPos = slotOriginY + 1
            
            slotOriginX += size
            
            if ((index + 1).rem(3) == 0) {
                slotOriginX = xPos + padding
                slotOriginY += size
            }
        }
        
    }
    
    override fun drawWidgetBackground(matrixStack: MatrixStack, font: FontRenderer, mouseX: Double, mouseY: Double) {
        background.dest(xPos, yPos).draw(matrixStack, 0f)
    }

    override fun getTooltip(): List<ITextComponent> {
        return listOf(
            toolboxName,
            GuiText.UpgradeToolbelt.text().copyRaw().mergeStyle(TextFormatting.GRAY)
        )
    }
}