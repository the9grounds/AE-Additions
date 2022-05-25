package com.the9grounds.aeadditions.client.gui.widget

import com.mojang.blaze3d.matrix.MatrixStack
import com.the9grounds.aeadditions.client.gui.AEABaseScreen
import net.minecraft.client.renderer.Rectangle2d
import net.minecraft.util.text.ITextComponent

interface IDynamicWidget {
    fun setPosition(position: Rectangle2d)
    
    fun setSize(width: Int, height: Int)
    
    fun getBounds(): Rectangle2d

    fun isMouseOver(mouseX: Double, mouseY: Double): Boolean
    
    fun populateScreen(bounds: Rectangle2d, screen: AEABaseScreen<*>) {
        
    }
    
    fun tick() {
        
    }
    
    fun updateBeforeRender() {
        
    }
    
    fun drawBackgroundLayer(matrixStack: MatrixStack, zIndex: Int, bounds: Rectangle2d, mouseX: Double, mouseY: Double) {
        
    }
    
    fun drawForegroundLayer(matrixStack: MatrixStack, zIndex: Int, bounds: Rectangle2d, mouseX: Double, mouseY: Double) {
        
    }
    
    fun onMouseDown(mouseX: Double, mouseY: Double, button: Int): Boolean = false
    
    fun onMouseUp(mouseX: Double, mouseY: Double, button: Int): Boolean = false

    fun getTooltip(): List<ITextComponent> { return listOf() }
}