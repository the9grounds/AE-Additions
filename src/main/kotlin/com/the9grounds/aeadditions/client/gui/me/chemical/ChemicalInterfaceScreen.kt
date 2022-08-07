package com.the9grounds.aeadditions.client.gui.me.chemical

import com.mojang.blaze3d.matrix.MatrixStack
import com.the9grounds.aeadditions.api.ICombinedChemicalContainer
import com.the9grounds.aeadditions.api.chemical.IAEChemicalStack
import com.the9grounds.aeadditions.client.gui.AEABaseScreen
import com.the9grounds.aeadditions.client.gui.widget.ChemicalWidgetConfig
import com.the9grounds.aeadditions.client.gui.widget.WidgetChemicalTank
import com.the9grounds.aeadditions.client.helpers.Blit
import com.the9grounds.aeadditions.container.SlotType
import com.the9grounds.aeadditions.container.chemical.ChemicalInterfaceContainer
import com.the9grounds.aeadditions.integration.mekanism.chemical.AEChemicalStack
import mekanism.api.chemical.Chemical
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.util.text.ITextComponent

class ChemicalInterfaceScreen(
    screenContainer: ChemicalInterfaceContainer,
    inv: PlayerInventory,
    titleIn: ITextComponent
) : AEABaseScreen<ChemicalInterfaceContainer>(screenContainer, inv, titleIn), ICombinedChemicalContainer {
    
    val texture = Blit("gui/chemical/interface.png")
    
    init {
        ySize = 231
        xSize = 176
        
        titleX = 7
        titleY = 7
        
        playerInventoryTitleX = 7
        playerInventoryTitleY = 137
        
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
            ChemicalWidgetConfig(this, 34, 34, 18, 18, 0, getAEChemicalStack(chemicalList[0]), 0),

            ChemicalWidgetConfig(this, 52, 34, 18, 18, 1, getAEChemicalStack(chemicalList[1]), 0),
            ChemicalWidgetConfig(this, 70, 34, 18, 18, 2, getAEChemicalStack(chemicalList[2]), 0),
            ChemicalWidgetConfig(this, 88, 34, 18, 18, 3, getAEChemicalStack(chemicalList[3]), 0),
            ChemicalWidgetConfig(this, 106, 34, 18, 18, 4, getAEChemicalStack(chemicalList[4]), 0),

            ChemicalWidgetConfig(this, 124, 34, 18, 18, 5, getAEChemicalStack(chemicalList[5]), 0),
        )
        
        widgetContainer.addAll(widgets, SlotType.Config)
    }
    
    override fun onChemicalListChange() {
        val chemicalTankList = container.chemicalTankList
        
        widgetContainer.reset(SlotType.Storage)
        
        chemicalTankList.chemicalTanks.forEachIndexed { index, chemicalTank ->
            val xPos = index * 18 + 35
            widgetContainer.add(WidgetChemicalTank(this, chemicalTank, xPos, 53, 68, 16, index), SlotType.Storage)
        }
    }
}