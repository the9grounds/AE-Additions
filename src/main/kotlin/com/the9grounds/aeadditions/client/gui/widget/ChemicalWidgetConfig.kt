package com.the9grounds.aeadditions.client.gui.widget

import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.systems.RenderSystem
import com.the9grounds.aeadditions.api.chemical.IAEChemicalStack
import com.the9grounds.aeadditions.client.gui.AEABaseScreen
import com.the9grounds.aeadditions.client.helpers.Blit
import com.the9grounds.aeadditions.container.AbstractUpgradableContainer
import com.the9grounds.aeadditions.integration.mekanism.Mekanism
import com.the9grounds.aeadditions.integration.mekanism.chemical.AEChemicalStack
import com.the9grounds.aeadditions.network.NetworkManager
import com.the9grounds.aeadditions.network.packets.ChemicalConfigChangedPacket
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer
import net.minecraft.inventory.container.ClickType
import net.minecraft.item.ItemStack

class ChemicalWidgetConfig(
    guiTerminal: AEABaseScreen<*>,
    posX: Int,
    posY: Int,
    height: Int,
    width: Int,
    slot: Int,
    chemical: IAEChemicalStack?,
    val group: Int
) : AbstractChemicalWidget(guiTerminal, posX, posY, height, width, slot, chemical) {
    
    val background = Blit("gui/states.png").src(0, 0, 18, 18)

    override fun drawWidgetBackground(matrixStack: MatrixStack, font: FontRenderer, mouseX: Double, mouseY: Double) {
        
        val alpha = if ((guiTerminal.container as AbstractUpgradableContainer).isConfigGroupEnabled(group)) {
            1.0f
        } else {
            .4f
        }
        
        background.dest(posX, posY, width, height).color(1f, 1f, 1f, alpha).draw(matrixStack, 1f)
        
        super.drawWidgetBackground(matrixStack, font, mouseX, mouseY)
    }

    override fun mouseClicked(mouseButton: Int, clickType: ClickType) {
        val mouseStack: ItemStack = Minecraft.getInstance().player!!.inventory.getItemStack()
        
        if (!mouseStack.isEmpty && Mekanism.isItemStackAChemicalContainer(mouseStack)) {
            val chemical = Mekanism.getStoredChemicalStackFromStack(mouseStack)
            
            if (chemical != null) {
                this.chemical = AEChemicalStack(chemical)
                
                NetworkManager.sendToServer(ChemicalConfigChangedPacket(chemical.getType(), slot))
            }
        } else if (mouseStack.isEmpty && this.chemical != null) {
            NetworkManager.sendToServer(ChemicalConfigChangedPacket(null, slot))
        }
        
        
        super.mouseClicked(mouseButton, clickType)
    }

    override fun drawWidgetForeground(matrixStack: MatrixStack, mouseX: Double, mouseY: Double) {

        super.drawWidgetForeground(matrixStack, mouseX, mouseY)
        
        if ((guiTerminal.container as AbstractUpgradableContainer<*>).isConfigGroupEnabled(group)) {
            if (isMouseOver(mouseX, mouseY)) {
                val mouseStack: ItemStack = Minecraft.getInstance().player!!.inventory.itemStack
                
                if (mouseStack.isEmpty || Mekanism.isItemStackAChemicalContainer(mouseStack)) {
                    RenderSystem.colorMask(true, true, true, false)
                    this.fillGradient(matrixStack, posX, posY, posX + width, posY + height, -2130706433, -2130706433)
                    RenderSystem.colorMask(true, true, true, true)
                }
            }
        }
    }
}