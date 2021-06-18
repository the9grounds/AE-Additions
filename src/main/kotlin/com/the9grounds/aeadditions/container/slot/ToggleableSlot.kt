package com.the9grounds.aeadditions.container.slot

import com.the9grounds.aeadditions.api.inventory.IToggleableSlotsInventory
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack

class ToggleableSlot(val toggleAbleSlotInventory: IToggleableSlotsInventory, slotIndex: Int, x: Int, y: Int) : Slot(toggleAbleSlotInventory, slotIndex, x, y) {

    fun isSlotEnabled(): Boolean {
        return toggleAbleSlotInventory.enabledSlots[slotIndex] ?: false
    }

    override fun canTakeStack(playerIn: EntityPlayer): Boolean {
        return isSlotEnabled()
    }

    override fun isItemValid(itemstack: ItemStack?): Boolean {
        return if (isSlotEnabled()) inventory.isItemValidForSlot(slotNumber, itemstack) else false
    }
}