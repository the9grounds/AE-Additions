package com.the9grounds.aeadditions.me.storage

import appeng.api.storage.cells.CellState
import appeng.api.storage.cells.ISaveProvider
import io.github.projectet.ae2things.storage.DISKCellInventory
import io.github.projectet.ae2things.storage.IDISKCellItem
import net.minecraft.world.item.ItemStack

class FluidDISKCellInventory(val cellType: IDISKCellItem?, val stack: ItemStack, val saveProvider: ISaveProvider?) : DISKCellInventory (cellType, stack, saveProvider){
    companion object {
        const val BUCKET = 81000
    }

    override fun getFreeBytes(): Long {
        return super.getTotalBytes() * BUCKET - super.getStoredItemCount()
    }

    override fun canHoldNewItem(): Boolean {
        return (getTrueAmount() > 0 && getTrueAmount() != super.getTotalBytes() * BUCKET)
    }

    override fun getNbtItemCount(): Long {
        if (super.hasDiskUUID()) {
            return if (stack.tag!!.getLong(ITEM_COUNT_TAG) == 0L) {
                0
            } else {
                Math.floorDiv(stack.tag!!.getLong(ITEM_COUNT_TAG) - 1, BUCKET + 1) // Actually a ceiling.
            }
            // A bucket has 81000 droplets. 1 byte = 1 bucket.
        }
        return 0
    }

    override fun getClientStatus(): CellState {
        if (getTrueAmount() == 0L) {
            return CellState.EMPTY
        }
        if ((getTrueAmount() > 0L) && getTrueAmount() != totalBytes * BUCKET) {
            return CellState.NOT_EMPTY
        }
        return CellState.FULL
    }

    private fun getTrueAmount(): Long {
        if (super.hasDiskUUID()) {
            return stack.tag!!.getLong(ITEM_COUNT_TAG)
        }
        return 0
    }
}