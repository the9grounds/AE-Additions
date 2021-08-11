package com.the9grounds.aeadditions.client.gui.me.chemical

import com.mojang.blaze3d.matrix.MatrixStack
import com.the9grounds.aeadditions.container.chemical.ChemicalTerminalContainer
import net.minecraft.client.gui.screen.inventory.ContainerScreen
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.fml.client.gui.GuiUtils

class ChemicalTerminalScreen(
    container: ChemicalTerminalContainer,
    playerInventory: PlayerInventory,
    title: ITextComponent
) : ContainerScreen<ChemicalTerminalContainer>(container, playerInventory, title) {
    override fun drawGuiContainerBackgroundLayer(matrixStack: MatrixStack, partialTicks: Float, x: Int, y: Int) {
        TODO("Not yet implemented")
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        
        
        
        return super.mouseClicked(mouseX, mouseY, button)
    }
}