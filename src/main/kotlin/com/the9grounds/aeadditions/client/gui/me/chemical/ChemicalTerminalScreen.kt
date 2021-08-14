package com.the9grounds.aeadditions.client.gui.me.chemical

import appeng.helpers.InventoryAction
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
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.container.ClickType
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.fml.client.gui.GuiUtils
import org.lwjgl.glfw.GLFW

class ChemicalTerminalScreen(
    container: ChemicalTerminalContainer,
    playerInventory: PlayerInventory,
    title: ITextComponent
) : ContainerScreen<ChemicalTerminalContainer>(container, playerInventory, title) {
    
    private val texture = ResourceLocation(AEAdditions.ID, "textures/gui/chemical_terminal.png")
    
    val widgetContainer = WidgetContainer()
    var searchBox: TextFieldWidget? = null
    
    init {
        ySize = 222
        xSize = 176
        
        titleX = 8
        titleY = 5
        
        playerInventoryTitleX = 8
        playerInventoryTitleY = 128

        font = Minecraft.getInstance().fontRenderer
        
        onChemicalListChange()
        container.gui = this
    }

    override fun init() {
        super.init()

        searchBox = TextFieldWidget(font, guiLeft + 100, guiTop + 5, 69, 10, title)
        searchBox!!.setTextColor(16777215)
        searchBox!!.setVisible(true)
        searchBox!!.setEnableBackgroundDrawing(false)
        searchBox!!.setMaxStringLength(15)
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            this.getMinecraft().player?.closeScreen()
        }
        
        if (searchBox!!.isFocused) {
            if (keyCode == GLFW.GLFW_KEY_ENTER) {
                searchBox!!.setFocused2(false)
                return true
            }
            
            if (searchBox!!.keyPressed(keyCode, scanCode, modifiers)) {
                onChemicalListChange()
            }
            
            return true
        }
        
        return super.keyPressed(keyCode, scanCode, modifiers)
    }

    override fun charTyped(codePoint: Char, modifiers: Int): Boolean {
        if (codePoint.code == GLFW.GLFW_KEY_ESCAPE) {
            this.getMinecraft().player?.closeScreen()
        }
        
        searchBox!!.charTyped(codePoint, modifiers)
        onChemicalListChange()
        
        return true
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
        RenderSystem.enableLighting()
        RenderSystem.enableRescaleNormal()
        RenderHelper.disableStandardItemLighting()
        searchBox!!.renderWidget(matrixStack, x, y, partialTicks)
        Minecraft.getInstance().textureManager.bindTexture(texture)
    }
    
    fun onChemicalListChange() {
        val chemicalList = container.chemicalList as? ChemicalList ?: return

        widgetContainer.reset()
        var startingX = 8
        var startingY = 17
        
        var tempList = chemicalList.records.values.toList()
        
        val searchTerm = searchBox!!.text.lowercase()
        
        val isEmpty = searchTerm.isEmpty()
        
        if (!isEmpty) {
            tempList = tempList.filter {
                it.getChemical().getName().lowercase().contains(searchTerm)
            }
        }
        
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
    
    fun onWidgetClicked(widget: ChemicalWidget, mouseButton: Int, clickType: ClickType) {
        val chemicalList = container.chemicalList as? ChemicalList ?: return
        
        var slot = -1
        
        if (widget.chemical == null) {
            clickedSlot(slot, mouseButton, null, clickType)
            
            return
        }
        
        chemicalList.records.values.forEachIndexed { index, chemical ->
            if (chemical.equals(widget.chemical)) {
                slot = index
            }
        }
        
        clickedSlot(slot, mouseButton, widget.chemical, clickType)
    }
    
    private fun clickedSlot(slot: Int, mouseButton: Int, chemical: IAEChemicalStack?, clickType: ClickType) {
        when (clickType) {
            ClickType.PICKUP -> {
                if (mouseButton == 0 && chemical != null) {
                    container.handleInteraction(slot, InventoryAction.FILL_ITEM)
                } else {
                    container.handleInteraction(-1, InventoryAction.EMPTY_ITEM)
                }
            }
        }
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        widgetContainer.mouseClicked(mouseX, mouseY, button)
        searchBox!!.mouseClicked(mouseX, mouseY, button)
        
        return super.mouseClicked(mouseX, mouseY, button)
    }
}