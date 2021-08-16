package com.the9grounds.aeadditions.client.gui.widget

import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.systems.RenderSystem
import com.the9grounds.aeadditions.api.gas.IAEChemicalStack
import com.the9grounds.aeadditions.client.gui.me.chemical.ChemicalTerminalScreen
import com.the9grounds.aeadditions.integration.mekanism.Mekanism
import com.the9grounds.aeadditions.util.Utils
import mekanism.api.text.TextComponentUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.AbstractGui
import net.minecraft.client.gui.FontRenderer
import net.minecraft.inventory.container.ClickType
import net.minecraft.inventory.container.PlayerContainer
import net.minecraft.util.text.ITextComponent
import org.lwjgl.opengl.GL11

class ChemicalWidget(
    val guiTerminal: ChemicalTerminalScreen,
    val posX: Int,
    val posY: Int,
    val height: Int,
    val width: Int,
    val slot: Int,
    val chemical: IAEChemicalStack?
) : AbstractGui(), IWidget {
    override fun getTooltip(): List<ITextComponent> {
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

    override fun mouseClicked(mouseButton: Int, clickType: ClickType) {
        guiTerminal.onWidgetClicked(this, mouseButton, clickType)
    }

    override fun drawWidget(matrixStack: MatrixStack, font: FontRenderer) {
        Minecraft.getInstance().textureManager.bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE)
        RenderSystem.disableLighting()
        RenderSystem.enableBlend()
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        RenderSystem.color3f(1f, 1f, 1f)

        if (chemical != null) {
            val sprite = Mekanism.getChemicalTexture(chemical.getChemical())

            val tint = chemical.getChemical().getTint()

            RenderSystem.color3f(Mekanism.getRed(tint), Mekanism.getGreen(tint), Mekanism.getBlue(tint))

            blit(matrixStack, posX + 1, posY + 1, 0, height - 2, width - 2, sprite)
            
            val amount = Utils.getAmountTextForStack(chemical)
            
            matrixStack.push()
            matrixStack.translate(posX.toDouble(), posY.toDouble(), 0.0)
            val scale = .48f
            matrixStack.scale(scale, scale, scale)
            
            val x = (1 + 16f - font.getStringWidth(amount) * scale) * (1f / scale)
            
//            val x = max(18f, 18f - (18f - font.getStringWidth(amount) * scale))
            
            font.drawString(matrixStack, amount, x, 26f, 0xFFFFFF)
            matrixStack.pop()
        }

        RenderSystem.disableBlend()
        RenderSystem.enableLighting()
    }
}