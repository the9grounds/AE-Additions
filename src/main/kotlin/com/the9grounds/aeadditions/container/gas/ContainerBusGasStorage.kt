package com.the9grounds.aeadditions.container.gas

import com.the9grounds.aeadditions.container.ContainerUpgradeable
import com.the9grounds.aeadditions.part.gas.PartGasIO
import net.minecraft.entity.player.EntityPlayer
import com.the9grounds.aeadditions.gui.IFluidSlotGuiTransfer
import net.minecraft.item.ItemStack
import com.the9grounds.aeadditions.part.gas.PartGasStorage
import net.minecraft.inventory.IInventory
import com.the9grounds.aeadditions.container.slot.SlotRespective
import net.minecraft.inventory.Slot

class ContainerBusGasStorage(part: PartGasStorage, player: EntityPlayer) : ContainerUpgradeable() {
    private var guiBusFluidStorage: IFluidSlotGuiTransfer? = null
    var part: PartGasStorage
    protected fun bindPlayerInventory(inventoryPlayer: IInventory?) {
        for (i in 0..2) {
            for (j in 0..8) {
                addSlotToContainer(Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, i * 18 + 140))
            }
        }
        for (i in 0..8) {
            addSlotToContainer(Slot(inventoryPlayer, i, 8 + i * 18, 198))
        }
    }

    override fun canInteractWith(entityplayer: EntityPlayer): Boolean {
        return part.isValid
    }

    fun setGui(_guiBusFluidStorage: IFluidSlotGuiTransfer?) {
        guiBusFluidStorage = _guiBusFluidStorage
    }

    override fun transferStackInSlot(player: EntityPlayer, slotnumber: Int): ItemStack {
        if (guiBusFluidStorage != null) {
            guiBusFluidStorage!!.shiftClick(getSlot(slotnumber).stack)
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
                return ItemStack.EMPTY
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
        addSlotToContainer(SlotRespective(part.upgradeInventory, 0, 187, 8))
        this.part = part
        bindPlayerInventory(player.inventory, 8, 140)
        bindUpgradeInventory(part)
        bindNetworkToolInventory(player.inventory, part)
    }
}