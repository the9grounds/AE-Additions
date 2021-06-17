package com.the9grounds.aeadditions.gui.fluid

import com.the9grounds.aeadditions.Constants
import com.the9grounds.aeadditions.container.fluid.ContainerFluidCrafter
import com.the9grounds.aeadditions.gui.GuiBase
import com.the9grounds.aeadditions.gui.ISlotRenderer
import com.the9grounds.aeadditions.gui.SlotUpgradeRenderer
import com.the9grounds.aeadditions.tileentity.TileEntityFluidCrafter
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.Slot
import net.minecraft.util.ResourceLocation

class GuiFluidCrafter(player: InventoryPlayer?, tileEntity: TileEntityFluidCrafter?) :
    GuiBase<ContainerFluidCrafter>(ResourceLocation(Constants.MOD_ID, "textures/gui/fluidcrafter.png"), ContainerFluidCrafter(player, tileEntity!!)) {

    var hasNetworkTool = false

    override fun drawBackground() {
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, 176, 182)
        drawTexturedModalRect(guiLeft + 179, guiTop, 179, 0, 32, 104)

        if (hasNetworkTool) {
            drawTexturedModalRect(guiLeft + 176, guiTop + 106, 176, 106, 68, 68)
        }
    }

    override fun getSlotRenderer(slot: Slot): ISlotRenderer? {
        return if ((slot.stack == null || slot.stack.isEmpty) && slot.slotNumber > 44) {
            SlotUpgradeRenderer.INSTANCE
        } else null
    }

    override fun hasSlotRenders(): Boolean = true

    init {
        hasNetworkTool = this.inventorySlots.getInventory().size > 50
        xSize = if (hasNetworkTool) 245 else 211
        ySize = 182
    }
}