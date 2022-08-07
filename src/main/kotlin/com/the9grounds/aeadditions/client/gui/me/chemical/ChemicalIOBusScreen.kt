package com.the9grounds.aeadditions.client.gui.me.chemical

import com.mojang.blaze3d.matrix.MatrixStack
import com.the9grounds.aeadditions.api.IChemicalConfigContainer
import com.the9grounds.aeadditions.api.chemical.IAEChemicalStack
import com.the9grounds.aeadditions.client.gui.AbstractUpgradableScreen
import com.the9grounds.aeadditions.client.gui.widget.ChemicalWidgetConfig
import com.the9grounds.aeadditions.client.helpers.Blit
import com.the9grounds.aeadditions.container.SlotType
import com.the9grounds.aeadditions.container.chemical.ChemicalIOContainer
import com.the9grounds.aeadditions.integration.mekanism.chemical.AEChemicalStack
import mekanism.api.chemical.Chemical
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.util.text.ITextComponent

class ChemicalIOBusScreen(
    screenContainer: ChemicalIOContainer,
    inv: PlayerInventory,
    titleIn: ITextComponent
) : AbstractUpgradableScreen<ChemicalIOContainer>(
    screenContainer,
    inv,
    titleIn
), IChemicalConfigContainer {
    val texture = Blit("gui/chemical/bus.png")
    init {
        ySize = 184
        xSize = 176
        
        titleX = 5
        titleY = 5

        playerInventoryTitleX = 7
        playerInventoryTitleY = 92

        font = Minecraft.getInstance().fontRenderer
        screenContainer.gui = this
    }
    
    fun getAEChemicalStack(chemical: Chemical<*>?): IAEChemicalStack? = if (chemical == null) null else AEChemicalStack(chemical)

    override fun drawGuiContainerBackgroundLayer(matrixStack: MatrixStack, partialTicks: Float, x: Int, y: Int) {
        
        texture.dest(guiLeft, guiTop).src(0, 0, getXSize(), getYSize()).draw(matrixStack, 0f)
        
        super.drawGuiContainerBackgroundLayer(matrixStack, partialTicks, x, y)
    }
    
    override fun onChemicalConfigChange() {
        val chemicalList = container.chemicalList
        
        widgetContainer.reset(SlotType.Config)
        
        val widgets = listOf(
            ChemicalWidgetConfig(this, 79, 39, 18, 18, 0, getAEChemicalStack(chemicalList[0]), 0),
            
            ChemicalWidgetConfig(this, 79, 57, 18, 18, 1, getAEChemicalStack(chemicalList[1]), 1),
            ChemicalWidgetConfig(this, 79, 21, 18, 18, 2, getAEChemicalStack(chemicalList[2]), 1),
            ChemicalWidgetConfig(this, 61, 39, 18, 18, 3, getAEChemicalStack(chemicalList[3]), 1),
            ChemicalWidgetConfig(this, 97, 39, 18, 18, 4, getAEChemicalStack(chemicalList[4]), 1),
            
            ChemicalWidgetConfig(this, 61, 21, 18, 18, 5, getAEChemicalStack(chemicalList[5]), 2),
            ChemicalWidgetConfig(this, 97, 21, 18, 18, 6, getAEChemicalStack(chemicalList[6]), 2),
            ChemicalWidgetConfig(this, 61, 57, 18, 18, 7, getAEChemicalStack(chemicalList[7]), 2),
            ChemicalWidgetConfig(this, 97, 57, 18, 18, 8, getAEChemicalStack(chemicalList[8]), 2),
        )
        
        widgetContainer.addAll(widgets, SlotType.Config)
    }
}