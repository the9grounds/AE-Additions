package com.the9grounds.aeadditions.gui.gas

import com.the9grounds.aeadditions.Constants
import com.the9grounds.aeadditions.container.gas.ContainerGasInterface
import com.the9grounds.aeadditions.gui.GuiBase
import com.the9grounds.aeadditions.gui.widget.WidgetGasTank
import com.the9grounds.aeadditions.gui.widget.fluid.WidgetFluidSlot
import com.the9grounds.aeadditions.network.packet.PacketGasInterfaceServer
import com.the9grounds.aeadditions.tileentity.TileEntityGasInterface
import com.the9grounds.aeadditions.util.NetworkUtil
import net.minecraft.client.gui.GuiButton
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.translation.I18n

class GuiGasInterface(val player: EntityPlayer, val gasInterface: TileEntityGasInterface) : GuiBase<ContainerGasInterface>(
    ResourceLocation(Constants.MOD_ID, "textures/gui/gasinterface.png"), ContainerGasInterface(player, gasInterface))
{
    var filter = arrayOfNulls<WidgetFluidSlot>(6)
    init {
        (this.inventorySlots as ContainerGasInterface).gui = this
        ySize = 231
        for (i in 0 until 6) {
            val xPos = i * 18 + 35

            widgetManager.add(WidgetGasTank(widgetManager, gasInterface.gasTanks[i], xPos, 53, i))

            var slot = WidgetFluidSlot(widgetManager, gasInterface, i, xPos - 1, 35)

            widgetManager.add(slot)

            filter[i] = slot
        }

        NetworkUtil.sendToServer(PacketGasInterfaceServer(gasInterface))
    }

    override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {
        fontRenderer.drawString(
            I18n.translateToLocal("tile.com.the9grounds.aeadditions.block.gas_interface.name").replace("ME ", ""),
            5,
            5,
            0x000000
        )
    }
}