package com.the9grounds.aeadditions.container.fluid

import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import com.the9grounds.aeadditions.container.slot.SlotRespective
import com.the9grounds.aeadditions.tileentity.TileEntityFluidFiller
import com.the9grounds.aeadditions.api.IFluidInterface
import net.minecraftforge.fml.relauncher.SideOnly
import com.the9grounds.aeadditions.gui.gas.GuiGasInterface
import appeng.api.util.AEPartLocation
import com.the9grounds.aeadditions.tileentity.TileEntityFluidInterface
import net.minecraftforge.fluids.FluidStack
import com.the9grounds.aeadditions.network.packet.part.PacketFluidInterface
import com.the9grounds.aeadditions.container.ContainerStorage
import net.minecraft.util.EnumHand
import appeng.api.storage.IMEMonitor
import appeng.api.storage.data.IAEFluidStack
import com.the9grounds.aeadditions.api.IPortableFluidStorageCell
import com.the9grounds.aeadditions.api.IWirelessFluidTermHandler
import com.the9grounds.aeadditions.util.AEUtils
import appeng.api.config.Actionable
import com.the9grounds.aeadditions.container.ContainerUpgradeable
import com.the9grounds.aeadditions.util.FluidHelper
import net.minecraft.inventory.Container
import net.minecraft.inventory.Slot

class ContainerFluidFiller(
    player: InventoryPlayer?,
    var tileentity: TileEntityFluidFiller
) : Container() {

    protected fun bindPlayerInventory(inventoryPlayer: InventoryPlayer?) {
        for (i in 0..2) {
            for (j in 0..8) {
                addSlotToContainer(
                    Slot(
                        inventoryPlayer, j + i * 9 + 9,
                        8 + j * 18, i * 18 + 84
                    )
                )
            }
        }
        for (i in 0..8) {
            addSlotToContainer(Slot(inventoryPlayer, i, 8 + i * 18, 142))
        }
    }

    override fun canInteractWith(entityplayer: EntityPlayer): Boolean {
        return true
    }

    //	@Override
    //	protected void retrySlotClick(int par1, int par2, boolean par3,
    //		EntityPlayer par4EntityPlayer) {
    //		// DON'T DO ANYTHING, YOU SHITTY METHOD!
    //	}
    override fun transferStackInSlot(player: EntityPlayer, slotnumber: Int): ItemStack {
        var transferStack = ItemStack.EMPTY
        val slot = inventorySlots[slotnumber]
        if (slot != null && slot.hasStack) {
            val itemStack = slot.stack
            transferStack = itemStack.copy()
            if (FluidHelper.isEmpty(itemStack)) {
                tileentity.containerItem = itemStack.copy()
                tileentity.markDirty()
                return ItemStack.EMPTY
            } else if (slotnumber < 27) {
                if (!mergeItemStack(itemStack, 27, inventorySlots.size, false)) {
                    return ItemStack.EMPTY
                }
            } else if (!mergeItemStack(itemStack, 0, 27, false)) {
                return ItemStack.EMPTY
            }
            if (itemStack.count == 0) {
                slot.putStack(ItemStack.EMPTY)
            } else {
                slot.onSlotChanged()
            }
        }
        return transferStack
    }

    init {
        bindPlayerInventory(player)
    }
}