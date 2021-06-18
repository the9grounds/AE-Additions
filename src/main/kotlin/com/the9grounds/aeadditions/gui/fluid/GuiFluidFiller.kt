package com.the9grounds.aeadditions.gui.fluid

import com.the9grounds.aeadditions.Constants
import net.minecraft.util.ResourceLocation
import net.minecraft.entity.player.EntityPlayer
import com.the9grounds.aeadditions.tileentity.TileEntityFluidFiller
import com.the9grounds.aeadditions.gui.GuiBase
import com.the9grounds.aeadditions.container.fluid.ContainerFluidFiller
import com.the9grounds.aeadditions.gui.widget.WidgetSlotFluidContainer
import net.minecraft.util.text.translation.I18n

class GuiFluidFiller(player: EntityPlayer, tileentity: TileEntityFluidFiller?) : GuiBase<ContainerFluidFiller?>(
    ResourceLocation(
        Constants.MOD_ID, "textures/gui/fluidfiller.png"
    ), ContainerFluidFiller(player.inventory, tileentity!!)
) {
    override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {
        fontRenderer.drawString(
            I18n.translateToLocal("tile.com.the9grounds.aeadditions.block.fluidfiller.name").replace("ME ", ""),
            5,
            5,
            0x000000
        )
    }

    companion object {
        const val xSize = 176
        const val ySize = 166
    }

    init {
        widgetManager.add(WidgetSlotFluidContainer(tileentity, widgetManager, 80, 35))
    }
}