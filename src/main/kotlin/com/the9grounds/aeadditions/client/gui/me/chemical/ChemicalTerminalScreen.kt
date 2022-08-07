package com.the9grounds.aeadditions.client.gui.me.chemical

import appeng.helpers.InventoryAction
import com.mojang.blaze3d.matrix.MatrixStack
import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.api.IChemicalListContainer
import com.the9grounds.aeadditions.api.chemical.IAEChemicalStack
import com.the9grounds.aeadditions.client.gui.AEABaseScreen
import com.the9grounds.aeadditions.client.gui.widget.IWidget
import com.the9grounds.aeadditions.client.gui.widget.TerminalChemicalWidget
import com.the9grounds.aeadditions.client.helpers.Blit
import com.the9grounds.aeadditions.container.SlotType
import com.the9grounds.aeadditions.container.chemical.ChemicalTerminalContainer
import com.the9grounds.aeadditions.integration.mekanism.chemical.ChemicalList
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.container.ClickType
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import org.lwjgl.glfw.GLFW

class ChemicalTerminalScreen(
    container: ChemicalTerminalContainer,
    playerInventory: PlayerInventory,
    title: ITextComponent
) : AEABaseScreen<ChemicalTerminalContainer>(container, playerInventory, StringTextComponent("Chemical Terminal")),
    IChemicalListContainer {
    
    private val texture = ResourceLocation(AEAdditions.ID, "textures/gui/chemical/terminal.png")
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
    
    override fun drawGuiContainerBackgroundLayer(matrixStack: MatrixStack, partialTicks: Float, x: Int, y: Int) {
        Blit(texture).dest(guiLeft, guiTop).src(0, 0, getXSize(), getYSize()).draw(matrixStack, 0f)
        searchBox!!.renderWidget(matrixStack, x, y, partialTicks)
        
        super.drawGuiContainerBackgroundLayer(matrixStack, partialTicks, x, y)
    }
    
    override fun onChemicalListChange() {
        val chemicalList = container.chemicalList as? ChemicalList ?: return

        widgetContainer.reset(SlotType.Storage)
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
        
        tempList = tempList.sortedWith(compareBy { it })
        
        for (i in 0 until 53) {

            val chemicalStack: IAEChemicalStack? = tempList.getOrNull(i)
            
            widgetContainer.add(TerminalChemicalWidget(this, startingX, startingY, 18, 18, i, chemicalStack), SlotType.Storage)
            startingX += 18
            if ((i + 1).rem(9) == 0) {
                startingX = 8
                startingY += 18
            }
        }
    }
    
    override fun onWidgetClicked(widget: IWidget, mouseButton: Int, clickType: ClickType) {
        
        if (widget !is TerminalChemicalWidget) {
            return
        }
        
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
        searchBox!!.mouseClicked(mouseX, mouseY, button)
        
        return super.mouseClicked(mouseX, mouseY, button)
    }
}