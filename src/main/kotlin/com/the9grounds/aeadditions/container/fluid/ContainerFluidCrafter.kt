package com.the9grounds.aeadditions.container.fluid

import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import com.the9grounds.aeadditions.container.slot.SlotRespective
import com.the9grounds.aeadditions.container.ContainerUpgradeable
import com.the9grounds.aeadditions.container.slot.SlotDisabled
import com.the9grounds.aeadditions.container.slot.ToggleableSlot
import com.the9grounds.aeadditions.tileentity.TileEntityFluidCrafter
import kotlin.math.ceil

class ContainerFluidCrafter(player: InventoryPlayer?, val tileEntity: TileEntityFluidCrafter) : ContainerUpgradeable() {
    override fun canInteractWith(entityplayer: EntityPlayer): Boolean {
        return true
    }

    override fun onContainerClosed(entityplayer: EntityPlayer) {
        super.onContainerClosed(entityplayer)
    }

    fun onCapacityChanged() {
//        bindInventory()
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

    private fun bindInventory() {
        tileEntity.filterOrder.forEach {
            val row = ceil((it + 1) / 3.0).toInt() - 1
            val column = it % 3

            addSlotToContainer(ToggleableSlot(tileEntity.inventory, it, 62 + column * 18, 27 + row * 18))
        }
    }

    init {

        bindInventory()
        bindPlayerInventory(player, 8, 100)
        bindUpgradeInventory(tileEntity)
        bindNetworkToolInventory(player, tileEntity, 185, 114)
    }
}