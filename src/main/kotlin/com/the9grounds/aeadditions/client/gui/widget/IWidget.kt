package com.the9grounds.aeadditions.client.gui.widget

import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.gui.FontRenderer
import net.minecraft.inventory.container.ClickType
import net.minecraft.util.text.ITextComponent

interface IWidget {
    
    fun drawWidgetBackground(matrixStack: MatrixStack, font: FontRenderer, mouseX: Double, mouseY: Double) {}
    
    fun drawWidgetForeground(matrixStack: MatrixStack, mouseX: Double, mouseY: Double) {}
    
    fun getTooltip(): List<ITextComponent> = listOf()
    
    fun mouseClicked(mouseButton: Int, clickType: ClickType) {}
    
    fun isMouseOver(mouseX: Double, mouseY: Double): Boolean = false
}