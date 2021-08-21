package com.the9grounds.aeadditions.client.gui.widget

import com.mojang.blaze3d.matrix.MatrixStack
import com.the9grounds.aeadditions.container.SlotType
import net.minecraft.client.gui.FontRenderer
import net.minecraft.inventory.container.ClickType
import net.minecraft.util.text.ITextComponent

class WidgetContainer {
    val widgets = mutableMapOf<SlotType, MutableList<IWidget>>()
    
    fun add(widget: IWidget, slotType: SlotType) {
        
        if (!widgets.containsKey(slotType)) {
            widgets[slotType] = mutableListOf()
        }
        
        widgets[slotType]?.add(widget)
    }
    
    fun addAll(widgets: List<IWidget>, slotType: SlotType) {
        if (!this.widgets.containsKey(slotType)) {
            this.widgets[slotType] = mutableListOf()
        }
        
        this.widgets[slotType]?.addAll(widgets)
    }
    
    fun resetAll() {
        widgets.clear()
    }
    
    fun reset(slotType: SlotType) {
        widgets[slotType]?.clear()
    }
    
    fun mouseClicked(mouseX: Double, mouseY: Double, button: Int) {
        
        widgets.forEach { t, u -> 
            u.forEach { widget ->
                if (widget.isMouseOver(mouseX, mouseY)) {
                    widget.mouseClicked(button, ClickType.PICKUP)
                }
            }
        }
    }
    
    fun drawWidgetBackground(matrixStack: MatrixStack, font: FontRenderer, mouseX: Double, mouseY: Double) {
        widgets.forEach { t, u ->
            u.forEach { widget ->
                widget.drawWidgetBackground(matrixStack, font, mouseX, mouseY)
            }
        }
    }
    
    fun drawForeground(matrixStack: MatrixStack, mouseX: Double, mouseY: Double) {
        widgets.forEach { t, u ->
            u.forEach { widget ->
                widget.drawWidgetForeground(matrixStack, mouseX, mouseY)
            }
        }
    }
    
    fun drawTooltips(mouseX: Double, mouseY: Double): List<ITextComponent> {
        for ((key, value) in widgets) {
            for (widget in value) {
                if (widget.isMouseOver(mouseX, mouseY)) {
                    return widget.getTooltip()
                }
            }
        }
        
        return listOf()
    }
}