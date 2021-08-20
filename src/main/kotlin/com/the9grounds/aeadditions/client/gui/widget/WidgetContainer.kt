package com.the9grounds.aeadditions.client.gui.widget

import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.gui.FontRenderer
import net.minecraft.inventory.container.ClickType
import net.minecraft.util.text.ITextComponent

class WidgetContainer {
    val widgets = mutableListOf<IWidget>()
    val dynamicWidgets = mutableListOf<IDynamicWidget>()
    
    fun add(widget: IWidget) {
        widgets.add(widget)
    }
    
    fun addDynamicWidget(widget: IDynamicWidget) {
        dynamicWidgets.add(widget)
    }
    
    fun reset() {
        widgets.clear()
        dynamicWidgets.clear()
    }
    
    fun mouseClicked(mouseX: Double, mouseY: Double, button: Int) {
        for (widget in widgets) {
            if (widget.isMouseOver(mouseX, mouseY)) {
                widget.mouseClicked(button, ClickType.PICKUP)
            }
        }
        
        for (widget in dynamicWidgets) {
            if (widget.isMouseOver(mouseX, mouseY)) {
                widget.onMouseDown(mouseX, mouseY, button)
            }
        }
    }
    
    fun drawWidgets(matrixStack: MatrixStack, font: FontRenderer) {
        for (widget in widgets) {
            widget.drawWidget(matrixStack, font)
        }
    }
    
    fun drawForeground(matrixStack: MatrixStack, mouseX: Double, mouseY: Double) {
        for (widget in widgets) {
            widget.drawForeground(matrixStack, mouseX, mouseY)
        }
    }
    
    fun drawTooltips(mouseX: Double, mouseY: Double): List<ITextComponent> {
        for (widget in widgets) {
            if (widget.isMouseOver(mouseX, mouseY)) {
                return widget.getTooltip()
            }
        }
        
        return listOf()
    }
}