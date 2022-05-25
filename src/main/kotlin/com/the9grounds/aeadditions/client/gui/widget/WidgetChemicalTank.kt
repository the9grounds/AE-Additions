package com.the9grounds.aeadditions.client.gui.widget

import com.mojang.blaze3d.matrix.MatrixStack
import com.the9grounds.aeadditions.client.gui.AEABaseScreen
import com.the9grounds.aeadditions.client.helpers.Blit
import com.the9grounds.aeadditions.container.chemical.ChemicalInterfaceContainer
import com.the9grounds.aeadditions.integration.mekanism.Mekanism
import net.minecraft.client.gui.AbstractGui
import net.minecraft.util.Direction
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent

class WidgetChemicalTank(
    val gui: AEABaseScreen<*>,
    val tank: ChemicalInterfaceContainer.ChemicalTank?, val posX: Int,
    val posY: Int, 
    val height: Int,
    val width: Int,
    val slot: Int,
) : AbstractGui(), IWidget {

    override fun getTooltip(): List<ITextComponent> {
        if (tank == null || tank.chemical === null) {
            return listOf(
                StringTextComponent("Side: ${Direction.byIndex(slot).name}")
            )
        }

        return listOf(
            tank.chemical!!.textComponent,
            StringTextComponent(tank.amount.toString() + "mB"),
            StringTextComponent("Side: ${Direction.byIndex(slot).name}")
        )
    }
    
    override fun isMouseOver(mouseX: Double, mouseY: Double): Boolean {
        return this.isPointInRegion(posX, posY, height, width, mouseX, mouseY)
    }

    fun isPointInRegion(x: Int, y: Int, height: Int, width: Int, mouseX: Double, mouseY: Double): Boolean {
        val i: Int = this.gui.guiLeft
        val j: Int = this.gui.guiTop
        val newMouseX = mouseX - i.toDouble()
        val newMouseY = mouseY - j.toDouble()
        return newMouseX >= (x - 1).toDouble() && newMouseX < (x + width + 1).toDouble() && newMouseY >= (y - 1).toDouble() && newMouseY < (y + height + 1).toDouble()
    }
    
    override fun drawWidgetForeground(matrixStack: MatrixStack, mouseX: Double, mouseY: Double) {
        if (tank != null && tank.chemical !== null) {
            val sprite = Mekanism.getChemicalTexture(tank.chemical!!)

            val tint = tank.chemical!!.tint

            val scaledHeight = (this.height * (tank.amount.toFloat() / tank.capacity.toFloat() )).toInt()

            val iconHeightRemainder = scaledHeight % 16
            
            if (iconHeightRemainder > 0) {
                Blit(sprite!!).colorRgb(tint).dest(posX, posY + height - iconHeightRemainder, 16, iconHeightRemainder).draw(matrixStack, 0f)
            }
            
            for (i in 0 until scaledHeight / 16) {
                Blit(sprite!!).colorRgb(tint).dest(posX, posY + height - iconHeightRemainder - ( i + 1 ) * 16, 16, 16).draw(matrixStack, 0f)
            }
        }
    }
}