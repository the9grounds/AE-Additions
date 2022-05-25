package com.the9grounds.aeadditions.client.gui.widget

import com.mojang.blaze3d.matrix.MatrixStack
import com.the9grounds.aeadditions.client.helpers.Blit
import com.the9grounds.aeadditions.container.slot.AEASlot
import net.minecraft.client.gui.AbstractGui
import net.minecraft.client.gui.FontRenderer
import net.minecraft.inventory.container.Slot
import net.minecraft.util.text.ITextComponent
import kotlin.math.min

class UpgradePanel(
    val slots: List<Slot>,
    val posX: Int,
    val posY: Int,
    val tooltipFactory: () -> List<ITextComponent>
) : AbstractGui(), IWidget {
    
    val height: Int
    val width: Int
    
    companion object {
        const val SLOT_SIZE = 18
        const val PADDING = 7
        const val MAX_ROWS = 8
        
        val background = Blit("gui/extra_panels.png", 128, 128)
        val innerCorner = background.copy().src(12, 33, SLOT_SIZE, SLOT_SIZE)
    }
    
    init {
        val slotCount = upgradeSlotCount
        height = 2 * PADDING + min(MAX_ROWS, slotCount) * SLOT_SIZE
        width = 2 * PADDING + (slotCount + MAX_ROWS - 1) / MAX_ROWS * SLOT_SIZE
        
        val slotOriginX = posX + PADDING 
        var slotOriginY = posY + PADDING
        
        for (slot in slots) {
            if (!slot.isEnabled) {
                continue
            }
            
            slot.xPos = slotOriginX + 1
            slot.yPos = slotOriginY + 1
            slotOriginY += SLOT_SIZE
        }
    }
    
    override fun drawWidgetBackground(matrixStack: MatrixStack, font: FontRenderer, mouseX: Double, mouseY: Double) {
        val slotCount = upgradeSlotCount
        if (slotCount <= 0) {
            return
        }


        // This is the absolute x,y coord of the first slot within the panel
        val slotOriginX: Int = posX + PADDING
        val slotOriginY: Int = posY + PADDING
        
        for (i in 0 until slotCount) {

            // Unlike other UIs, this is drawn top-to-bottom,left-to-right
            val row = i % MAX_ROWS
            val col = i / MAX_ROWS

            val x = slotOriginX + col * SLOT_SIZE
            val y = slotOriginY + row * SLOT_SIZE

            val borderLeft = col == 0
            val borderTop = row == 0
            // The panel can have a "jagged" edge if the number of slots is not divisible by MAX_ROWS
            // The panel can have a "jagged" edge if the number of slots is not divisible by MAX_ROWS
            val lastSlot = i + 1 >= slotCount
            val lastRow = row + 1 >= MAX_ROWS
            val borderBottom = lastRow || lastSlot
            val borderRight = i >= slotCount - MAX_ROWS
            
            drawSlot(matrixStack, 0f, x, y, borderLeft, borderTop, borderRight, borderBottom)

            if (col > 0 && lastSlot && !lastRow) {
                innerCorner.dest(x, y + SLOT_SIZE).draw(matrixStack, 0f)
            }
        }
    }

    fun drawSlot(
        matrices: MatrixStack, zIndex: Float, x: Int, y: Int,
        borderLeft: Boolean, borderTop: Boolean, borderRight: Boolean, borderBottom: Boolean
    ) {
        var x = x
        var y = y
        var srcX = PADDING
        var srcY = PADDING
        var srcWidth = SLOT_SIZE
        var srcHeight = SLOT_SIZE
        if (borderLeft) {
            x -= PADDING
            srcX = 0
            srcWidth += PADDING
        }
        if (borderRight) {
            srcWidth += PADDING
        }
        if (borderTop) {
            y -= PADDING
            srcY = 0
            srcHeight += PADDING
        }
        if (borderBottom) {
            srcHeight += PADDING
        }
        background.src(srcX, srcY, srcWidth, srcHeight)
            .dest(x, y)
            .draw(matrices, zIndex)
    }

    override fun getTooltip(): List<ITextComponent> {
        if (upgradeSlotCount == 0) {
            return listOf()
        }

        return tooltipFactory()
    }
    
    val upgradeSlotCount: Int
    get() {
        var count = 0
        for (slot in slots) {
            if (slot is AEASlot && slot.isSlotEnabled) {
                count++
            }
        }
        
        return count
    }
}