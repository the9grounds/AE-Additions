package com.the9grounds.aeadditions.client.gui

import appeng.client.gui.style.Blitter
import com.mojang.blaze3d.vertex.PoseStack
import com.the9grounds.aeadditions.AEAdditions
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.TextComponent
import net.minecraft.resources.ResourceLocation
import kotlin.math.floor

class ScrollBar(x: Int, y: Int, val screen: MEWirelessTransceiverScreen) : AbstractWidget(x, y, 6, 4, TextComponent.EMPTY) {
    val originalY: Int = this.y
    val maxY = (originalY + 12 * 4) - 3
    val texture = Blitter.texture(ResourceLocation(AEAdditions.ID, "textures/gui/scrollbar.png"))
    
    var dragging = false

    override fun render(poseStack: PoseStack, p_93658_: Int, p_93659_: Int, p_93660_: Float) {
        texture.dest(this.x, this.y, 6, 4).blit(poseStack, 2)
    }
    override fun updateNarration(p_169152_: NarrationElementOutput) {
        
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, wheelDelta: Double): Boolean {
        
        val change = if (wheelDelta < 0) {
            1
        } else {
            -1
        }
        
        screen.currentCursor = (screen.currentCursor + change).coerceAtLeast(0).coerceAtMost(screen.scrollNumber())
        
        this.y = floor(this.y + (change * screen.scrollUnit())).toInt().coerceAtLeast(originalY).coerceAtMost(maxY)
        
        return super.mouseScrolled(mouseX, mouseY, wheelDelta)
    }

    override fun mouseDragged(
        p_93645_: Double,
        p_93646_: Double,
        p_93647_: Int,
        p_93648_: Double,
        p_93649_: Double
    ): Boolean {
        if (this.clicked(p_93645_, p_93646_) || dragging) {
            if (!dragging) {
                dragging = true
            }
            var y = p_93646_.toInt().coerceAtLeast(originalY)
            
            y = y.coerceAtMost(maxY)
            
            this.y = y
            
            val diff = this.y - originalY
            
            val unit = screen.scrollUnit()
            
            screen.currentCursor = floor(diff.toDouble() / unit).toInt().coerceAtMost(screen.scrollNumber())
            screen.currentCursor = screen.currentCursor.coerceAtLeast(0)
        }
        return super.mouseDragged(p_93645_, p_93646_, p_93647_, p_93648_, p_93649_)
    }

    override fun mouseReleased(p_93684_: Double, p_93685_: Double, p_93686_: Int): Boolean {
        if (dragging) {
            dragging = false
        }
        return super.mouseReleased(p_93684_, p_93685_, p_93686_)
    }
}