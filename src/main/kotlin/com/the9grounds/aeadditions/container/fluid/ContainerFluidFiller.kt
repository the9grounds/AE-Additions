package com.the9grounds.aeadditions.container.fluid

import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import com.the9grounds.aeadditions.tileentity.TileEntityFluidFiller
import com.the9grounds.aeadditions.container.ContainerUpgradeable
import com.the9grounds.aeadditions.gui.fluid.GuiFluidFiller
import com.the9grounds.aeadditions.util.FluidHelper

class ContainerFluidFiller(
    player: InventoryPlayer?,
    var tileEntity: TileEntityFluidFiller
) : ContainerUpgradeable() {

    var gui: GuiFluidFiller? = null

    override fun canInteractWith(entityplayer: EntityPlayer): Boolean {
        return true
    }

    //	@Override
    //	protected void retrySlotClick(int par1, int par2, boolean par3,
    //		EntityPlayer par4EntityPlayer) {
    //		// DON'T DO ANYTHING, YOU SHITTY METHOD!
    //	}
    override fun transferStackInSlot(player: EntityPlayer, slotnumber: Int): ItemStack {

        if (gui != null && gui!!.shiftClick(getSlot(slotnumber).stack)) {
            return ItemStack.EMPTY
        }

        var transferStack = ItemStack.EMPTY
        val slot = inventorySlots[slotnumber]
        if (slot != null && slot.hasStack) {
            val itemStack = slot.stack
            transferStack = itemStack.copy()
            if (FluidHelper.isEmpty(itemStack)) {
                tileEntity.containerItem = itemStack.copy()
                tileEntity.markDirty()
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
        bindUpgradeInventory(tileEntity)
        bindNetworkToolInventory(player, tileEntity, 185, 78)
    }
}