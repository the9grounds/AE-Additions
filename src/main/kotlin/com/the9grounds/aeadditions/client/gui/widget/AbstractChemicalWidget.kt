package com.the9grounds.aeadditions.client.gui.widget

import com.mojang.blaze3d.matrix.MatrixStack
import com.the9grounds.aeadditions.api.chemical.IAEChemicalStack
import com.the9grounds.aeadditions.client.gui.AEABaseScreen
import com.the9grounds.aeadditions.client.helpers.Blit
import com.the9grounds.aeadditions.integration.mekanism.Mekanism
import net.minecraft.client.gui.AbstractGui

abstract class AbstractChemicalWidget(
    val guiTerminal: AEABaseScreen<*>,
    val posX: Int,
    val posY: Int,
    val height: Int,
    val width: Int,
    val slot: Int,
    var chemical: IAEChemicalStack?
): AbstractGui(), IWidget {

    override fun isMouseOver(mouseX: Double, mouseY: Double): Boolean {
        return this.isPointInRegion(posX, posY, height, width, mouseX, mouseY)
    }

    fun isPointInRegion(x: Int, y: Int, height: Int, width: Int, mouseX: Double, mouseY: Double): Boolean {
        val i: Int = this.guiTerminal.guiLeft
        val j: Int = this.guiTerminal.guiTop
        val newMouseX = mouseX - i.toDouble()
        val newMouseY = mouseY - j.toDouble()
        return newMouseX >= (x - 1).toDouble() && newMouseX < (x + width + 1).toDouble() && newMouseY >= (y - 1).toDouble() && newMouseY < (y + height + 1).toDouble()
    }

    override fun drawWidgetForeground(matrixStack: MatrixStack, mouseX: Double, mouseY: Double) {
        
        if (chemical != null) {
            val sprite = Mekanism.getChemicalTexture(chemical!!.getChemical())

            val tint = chemical!!.getChemical().tint

            Blit(sprite!!).colorRgb(tint).dest(posX + 1, posY + 1, width - 2, height - 2).draw(matrixStack, 5f)
        }
        
        super.drawWidgetForeground(matrixStack, mouseX, mouseY)
    }
}