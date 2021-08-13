package com.the9grounds.aeadditions.client.gui.me.chemical

import appeng.api.storage.data.IItemList
import appeng.fluids.util.AEFluidStack
import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.systems.RenderSystem
import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.api.gas.IAEChemicalStack
import com.the9grounds.aeadditions.client.gui.widget.ChemicalWidget
import com.the9grounds.aeadditions.client.gui.widget.WidgetContainer
import com.the9grounds.aeadditions.container.chemical.ChemicalTerminalContainer
import com.the9grounds.aeadditions.integration.mekanism.chemical.ChemicalList
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screen.inventory.ContainerScreen
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.fml.client.gui.GuiUtils

class ChemicalTerminalScreen(
    container: ChemicalTerminalContainer,
    playerInventory: PlayerInventory,
    title: ITextComponent
) : ContainerScreen<ChemicalTerminalContainer>(container, playerInventory, title) {
    
    private val texture = ResourceLocation(AEAdditions.ID, "textures/gui/chemical_terminal.png")
    
    val widgetContainer = WidgetContainer()
    
    init {
        ySize = 222
        xSize = 176
        
        titleX = 8
        titleY = 5
        
        playerInventoryTitleX = 8
        playerInventoryTitleY = 128
        
        onFluidListChange()
        container.gui = this
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

    override fun drawGuiContainerForegroundLayer(matrixStack: MatrixStack, mouseX: Int, mouseY: Int) {
        super.drawGuiContainerForegroundLayer(matrixStack, mouseX, mouseY)
    }
    
    override fun drawGuiContainerBackgroundLayer(matrixStack: MatrixStack, partialTicks: Float, x: Int, y: Int) {
        RenderSystem.color4f(1f, 1f, 1f, 1f)
        Minecraft.getInstance().textureManager.bindTexture(texture)
        
        blit(matrixStack, guiLeft, guiTop, 0, 0, getXSize(), getYSize())
        
        RenderHelper.enableStandardItemLighting()
        RenderSystem.disableLighting()
        RenderSystem.enableRescaleNormal()
        
        RenderSystem.color4f(1f, 1f, 1f, 1f)
        matrixStack.push()
        matrixStack.translate(guiLeft.toDouble(), guiTop.toDouble(), 0.0)
        
        widgetContainer.drawWidgets(matrixStack)
        
        matrixStack.pop()
        Minecraft.getInstance().textureManager.bindTexture(texture)
    }
    
    fun onFluidListChange() {
        val chemicalList = container.chemicalList as? ChemicalList ?: return

        widgetContainer.reset()
        var startingX = 8
        var startingY = 17
        
        val tempList = chemicalList.records.values.toList()
        
        for (i in 0..53) {

            val chemicalStack: IAEChemicalStack? = tempList.getOrNull(i)
            
            widgetContainer.add(ChemicalWidget(this, startingX, startingY, 18, 18, i, chemicalStack))
            startingX += 18
            if ((i + 1).rem(9) == 0) {
                startingX = 8
                startingY += 18
            }
        }
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        widgetContainer.mouseClicked(mouseX, mouseY, button)
        
        return super.mouseClicked(mouseX, mouseY, button)
    }
}