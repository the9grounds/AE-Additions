package com.the9grounds.aeadditions.client.gui

import com.mojang.blaze3d.matrix.MatrixStack
import com.the9grounds.aeadditions.client.gui.widget.IWidget
import com.the9grounds.aeadditions.client.gui.widget.WidgetContainer
import com.the9grounds.aeadditions.container.AbstractContainer
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screen.inventory.ContainerScreen
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.container.ClickType
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.fml.client.gui.GuiUtils

abstract class AEABaseScreen<T: AbstractContainer<T>>(screenContainer: T, inv: PlayerInventory, titleIn: ITextComponent) :
    ContainerScreen<T>(screenContainer, inv, titleIn) {

    val widgetContainer = WidgetContainer()

    override fun init(minecraft: Minecraft, width: Int, height: Int) {
        super.init(minecraft, width, height)
    }

    override fun render(matrixStack: MatrixStack, mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.renderBackground(matrixStack)
        super.render(matrixStack, mouseX, mouseY, partialTicks)

        val tooltip = widgetContainer.drawTooltips(mouseX.toDouble(), mouseY.toDouble())
        if (tooltip.isNotEmpty()) {
            GuiUtils.drawHoveringText(matrixStack, tooltip, mouseX, mouseY, width, height, 200, font)
        }
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY)
    }

    override fun drawGuiContainerForegroundLayer(matrixStack: MatrixStack, x: Int, y: Int) {
        
        widgetContainer.drawForeground(matrixStack, x.toDouble(), y.toDouble())
        
        super.drawGuiContainerForegroundLayer(matrixStack, x, y)
    }

    override fun drawGuiContainerBackgroundLayer(matrixStack: MatrixStack, partialTicks: Float, x: Int, y: Int) {
        matrixStack.push()
        matrixStack.translate(guiLeft.toDouble(), guiTop.toDouble(), 0.0)

        widgetContainer.drawWidgets(matrixStack, font)

        matrixStack.pop()
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        widgetContainer.mouseClicked(mouseX, mouseY, button)
        
        return super.mouseClicked(mouseX, mouseY, button)
    }

    open fun onWidgetClicked(widget: IWidget, mouseButton: Int, clickType: ClickType) {
        
    }
}