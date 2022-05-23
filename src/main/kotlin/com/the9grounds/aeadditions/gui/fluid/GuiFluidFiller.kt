package com.the9grounds.aeadditions.gui.fluid

import com.the9grounds.aeadditions.Constants
import com.the9grounds.aeadditions.container.fluid.ContainerFluidFiller
import com.the9grounds.aeadditions.gui.GuiBase
import com.the9grounds.aeadditions.gui.ISlotRenderer
import com.the9grounds.aeadditions.gui.SlotUpgradeRenderer
import com.the9grounds.aeadditions.gui.widget.fluid.WidgetFluidSlot
import com.the9grounds.aeadditions.network.packet.PacketFluidFillerSyncClient
import com.the9grounds.aeadditions.tileentity.TileEntityFluidFiller
import com.the9grounds.aeadditions.util.FluidHelper
import com.the9grounds.aeadditions.util.NetworkUtil
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.translation.I18n
import net.minecraftforge.fluids.Fluid

class GuiFluidFiller(player: EntityPlayer, val tileEntity: TileEntityFluidFiller) : GuiBase<ContainerFluidFiller?>(
    ResourceLocation(
        Constants.MOD_ID, "textures/gui/fluidfiller.png"
    ), ContainerFluidFiller(player.inventory, tileEntity)
) {

    var hasNetworkTool = false

    override fun drawBackground() {
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, 175, 166)
        drawTexturedModalRect(guiLeft + 179, guiTop, 179, 0, 32, 68)

        if (hasNetworkTool) {
            drawTexturedModalRect(guiLeft + 177, guiTop + 70, 177, 70, 68, 68)
        }
    }

    override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {
        fontRenderer.drawString(
            I18n.translateToLocal("tile.com.the9grounds.aeadditions.block.fluidfiller.name").replace("ME ", ""),
            5,
            5,
            0x000000
        )
    }

    override fun getSlotRenderer(slot: Slot): ISlotRenderer? {

        val stack = slot.stack

        if (stack != null && !stack.isEmpty) {
            return null
        }

        val slotNumber = slot.slotNumber

        return when(true) {
            slotNumber > 38 -> SlotUpgradeRenderer.INSTANCE
            else -> null
        }
    }

    override fun hasSlotRenders(): Boolean = true

    fun updateSelectedFluid(fluid: Fluid?) {
        for (widget in widgetManager.widgets) {
            if (widget is WidgetFluidSlot) {
                if (fluid == null) {
                    widget.fluid = null
                    return
                }

                widget.fluid = fluid
            }
        }
    }

    fun shiftClick(itemStack: ItemStack): Boolean {
        val containerFluid = FluidHelper.getFluidFromContainer(itemStack)
        val fluid = containerFluid?.fluid
        for (widget in widgetManager.widgets) {
            if (widget is WidgetFluidSlot) {
                if (fluid != null && (widget.fluid == null || widget.fluid === fluid) && widget.isVisable) {
                    widget.handleContainer(itemStack)
                    return true
                }
            }
        }
        return false
    }

    init {
        hasNetworkTool = this.inventorySlots.getInventory().size > 39
        xSize = if (hasNetworkTool) 245 else 211
        ySize = 166
        widgetManager.add(WidgetFluidSlot(widgetManager, tileEntity, 79, 34))
        (inventorySlots as ContainerFluidFiller).gui = this
        NetworkUtil.sendToServer(PacketFluidFillerSyncClient(tileEntity))
//        widgetManager.add(WidgetSlotFluidContainer(tileentity, widgetManager, 80, 35))
    }
}