package com.the9grounds.aeadditions.client.gui.widget

import com.mojang.blaze3d.matrix.MatrixStack
import com.the9grounds.aeadditions.api.chemical.IAEChemicalStack
import com.the9grounds.aeadditions.client.gui.AEABaseScreen
import com.the9grounds.aeadditions.util.Utils
import mekanism.api.text.TextComponentUtil
import net.minecraft.client.Minecraft
import net.minecraft.inventory.container.ClickType
import net.minecraft.util.text.ITextComponent

class TerminalChemicalWidget(
    guiTerminal: AEABaseScreen<*>,
    posX: Int,
    posY: Int,
    height: Int,
    width: Int,
    slot: Int,
    chemical: IAEChemicalStack?
) : AbstractChemicalWidget(guiTerminal, posX, posY, height, width, slot, chemical), IWidget {
    
    override fun getTooltip(): List<ITextComponent> {
        
        val chemical = this.chemical
        
        if (chemical == null) {
            return listOf()
        }

        var amountToText = java.lang.Long.toString(chemical.stackSize) + "mB"
        if (chemical.stackSize > 1000000000L) {
            amountToText = (java.lang.Long.toString(chemical.stackSize / 1000000000L)
                    + "MegaB")
        } else if (chemical.stackSize > 1000000L) {
            amountToText = java.lang.Long.toString(chemical.stackSize / 1000000L) + "KiloB"
        } else if (chemical.stackSize > 9999L) {
            amountToText = java.lang.Long.toString(chemical.stackSize / 1000L) + "B"
        }

        val toolTips = mutableListOf<ITextComponent>()

        toolTips.add(chemical.getChemical().getTextComponent())
        toolTips.add(TextComponentUtil.build(amountToText))
        return toolTips
    }

    override fun drawWidgetForeground(matrixStack: MatrixStack, mouseX: Double, mouseY: Double) {
        super<AbstractChemicalWidget>.drawWidgetForeground(matrixStack, mouseX, mouseY)
        
        if (chemical != null) {
            
            val font = Minecraft.getInstance().fontRenderer
            
            val amount = Utils.getAmountTextForStack(chemical!!)

            matrixStack.push()
            matrixStack.translate(posX.toDouble(), posY.toDouble(), 10.0)
            val scale = .48f
            matrixStack.scale(scale, scale, scale)

            val x = (1 + 16f - font.getStringWidth(amount) * scale) * (1f / scale)

            font.drawString(matrixStack, amount, x, 26f, 0xFFFFFF)
            matrixStack.pop()
        }
    }

    override fun mouseClicked(mouseButton: Int, clickType: ClickType) {
        guiTerminal.onWidgetClicked(this, mouseButton, clickType)
    }
}