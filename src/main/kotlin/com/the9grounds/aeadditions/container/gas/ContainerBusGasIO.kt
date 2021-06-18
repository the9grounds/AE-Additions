package com.the9grounds.aeadditions.container.gas

import com.the9grounds.aeadditions.container.ContainerUpgradeable
import com.the9grounds.aeadditions.gui.IFluidSlotGuiTransfer
import com.the9grounds.aeadditions.part.gas.PartGasIO
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack

class ContainerBusGasIO(private val part: PartGasIO, player: EntityPlayer) : ContainerUpgradeable() {
    private var guiBusFluidIO: IFluidSlotGuiTransfer? = null
    override fun canInteractWith(entityplayer: EntityPlayer): Boolean {
        return part.isValid
    }

    //	@Override
    //	protected void retrySlotClick(int par1, int par2, boolean par3, EntityPlayer par4EntityPlayer) {
    //		// NOPE
    //	}
    fun setGui(_guiBusFluidIO: IFluidSlotGuiTransfer?) {
        guiBusFluidIO = _guiBusFluidIO
    }

    override fun transferStackInSlot(player: EntityPlayer, slotnumber: Int): ItemStack {
        //TODO:remove the gui from this
        if (guiBusFluidIO != null && guiBusFluidIO!!.shiftClick(getSlot(slotnumber).stack)) {
            return ItemStack.EMPTY
        }
        var itemstack = ItemStack.EMPTY
        val slot = inventorySlots[slotnumber]
        if (slot != null && slot.hasStack) {
            val itemstack1 = slot.stack
            itemstack = itemstack1.copy()
            if (slotnumber < 36) {
                if (!mergeItemStack(itemstack1, 36, inventorySlots.size, true)) {
                    return ItemStack.EMPTY
                }
            } else if (!mergeItemStack(itemstack1, 0, 36, false)) {
                return itemstack1
            }
            if (itemstack1.count == 0) {
                slot.putStack(ItemStack.EMPTY)
            } else {
                slot.onSlotChanged()
            }
        }
        return itemstack
    }

    init {
        bindPlayerInventory(player.inventory, 8, 102)
        bindUpgradeInventory(part)
        bindNetworkToolInventory(player.inventory, part)
    }
}