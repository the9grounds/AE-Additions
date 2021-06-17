package com.the9grounds.aeadditions.container.fluid

import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import com.the9grounds.aeadditions.container.slot.SlotRespective
import com.the9grounds.aeadditions.container.ContainerUpgradeable
import com.the9grounds.aeadditions.tileentity.TileEntityFluidCrafter
import net.minecraft.inventory.Slot

class ContainerFluidCrafter(player: InventoryPlayer?, val tileEntity: TileEntityFluidCrafter) : ContainerUpgradeable() {
    override fun canInteractWith(entityplayer: EntityPlayer): Boolean {
        return true
    }

    override fun onContainerClosed(entityplayer: EntityPlayer) {
        super.onContainerClosed(entityplayer)
    }

    override fun transferStackInSlot(player: EntityPlayer, slotnumber: Int): ItemStack {
        var transferStack = ItemStack.EMPTY
        val slot = inventorySlots[slotnumber]
        if (slot != null && slot.hasStack) {
            val itemStack = slot.stack
            transferStack = itemStack.copy()
            if (slotnumber < 9) {
                if (!mergeItemStack(itemStack, 9, inventorySlots.size, false)) {
                    return ItemStack.EMPTY
                }
            } else if (slotnumber < 36) {
                if (!mergeItemStack(itemStack, 0, 9, false) &&
                    !mergeItemStack(itemStack, 36, inventorySlots.size, false)
                ) {
                    return ItemStack.EMPTY
                }
            } else if (!mergeItemStack(itemStack, 0, 9, false) &&
                !mergeItemStack(itemStack, 9, 36, false)
            ) {
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
        for (i in 0..2) {
            for (j in 0..2) {
                addSlotToContainer(
                    SlotRespective(
                        tileEntity.inventory, j + i * 3,
                        62 + j * 18, 27 + i * 18
                    )
                )
            }
        }
        bindPlayerInventory(player, 8, 100)
        bindUpgradeInventory(tileEntity)
        bindNetworkToolInventory(player, tileEntity, 185, 114)
    }
}