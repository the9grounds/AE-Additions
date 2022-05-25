package com.the9grounds.aeadditions.me.storage

import appeng.api.config.Actionable
import appeng.api.exceptions.AppEngException
import appeng.api.networking.security.IActionSource
import appeng.api.storage.IStorageChannel
import appeng.api.storage.cells.ICellInventory
import appeng.api.storage.cells.ISaveProvider
import appeng.api.storage.data.IAEItemStack
import appeng.api.storage.data.IAEStack
import appeng.core.AELog
import com.the9grounds.aeadditions.Logger
import com.the9grounds.aeadditions.api.IAEAdditionsStorageCell
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT

class AEAdditionsCellInventory<T: IAEStack<T>>(val cell: IAEAdditionsStorageCell<T>, itemStack: ItemStack, container: ISaveProvider?) : AbstractAEAdditionsInventory<T>(cell, itemStack, container) {


    override fun loadCellItem(compoundTag: CompoundNBT?, stackSize: Long): Boolean {
        // Now load the item stack

        // Now load the item stack
        val t: T?
        try {
            t = this.channel.createFromNBT(compoundTag!!)
            if (t == null) {
                Logger.warn(
                    "Removing item " + compoundTag
                            + " from storage cell because the associated item type couldn't be found."
                )
                return false
            }
        } catch (ex: Throwable) {
//            if (AEConfig.instance().isRemoveCrashingItemsOnLoad) {
//                LOGGER.warn(
//                    ex,
//                    "Removing item $compoundTag from storage cell because loading the ItemStack crashed."
//                )
//                return false
//            }
            throw ex
        }

        t.stackSize = stackSize

        if (stackSize > 0) {
            cellItems!!.add(t)
        }

        return true
    }

    override fun injectItems(input: T, mode: Actionable, src: IActionSource): T? {
        if (input == null) {
            return null
        }
        if (input.stackSize == 0L) {
            return null
        }

        if (cellType!!.isBlackListed(this.itemStack, input)) {
            return input
        }
        // This is slightly hacky as it expects a read-only access, but fine for now.
        // TODO: Guarantee a read-only access. E.g. provide an isEmpty() method and
        // ensure CellInventory does not write
        // any NBT data for empty cells instead of relying on an empty IItemContainer
        // This is slightly hacky as it expects a read-only access, but fine for now.
        // TODO: Guarantee a read-only access. E.g. provide an isEmpty() method and
        // ensure CellInventory does not write
        // any NBT data for empty cells instead of relying on an empty IItemContainer
        if (this.isStorageCell(input)) {
            val meInventory: ICellInventory<T> =
                createInventory((input as IAEItemStack).createItemStack(), null)!!
            if (!isCellEmpty(meInventory)) {
                return input
            }
        }

        val l: T? = this.cellItems!!.findPrecise(input)
        if (l != null) {
            val remainingItemCount = this.remainingItemCount
            if (remainingItemCount <= 0) {
                return input
            }
            return if (input.stackSize > remainingItemCount) {
                val r = input.copy()
                r.stackSize = r.stackSize - remainingItemCount
                if (mode == Actionable.MODULATE) {
                    l.stackSize = l.stackSize + remainingItemCount
                    saveChanges()
                }
                r
            } else {
                if (mode == Actionable.MODULATE) {
                    l.stackSize = l.stackSize + input.stackSize
                    saveChanges()
                }
                null
            }
        }

        if (canHoldNewItem()) // room for new type, and for at least one item!
        {
            val remainingItemCount = (this.remainingItemCount
                    - this.bytesPerType * itemsPerByte)
            if (remainingItemCount > 0) {
                if (input.stackSize > remainingItemCount) {
                    val toReturn = input.copy()
                    toReturn.stackSize = input.stackSize - remainingItemCount
                    if (mode == Actionable.MODULATE) {
                        val toWrite = input.copy()
                        toWrite.stackSize = remainingItemCount.toLong()
                        cellItems!!.add(toWrite)
                        saveChanges()
                    }
                    return toReturn
                }
                if (mode == Actionable.MODULATE) {
                    cellItems!!.add(input)
                    saveChanges()
                }
                return null
            }
        }

        return input
    }

    override fun extractItems(request: T, mode: Actionable, src: IActionSource): T? {
        if (request == null) {
            return null
        }

        val size = Math.min(Int.MAX_VALUE.toLong(), request.stackSize)

        var results: T? = null

        val l: T? = this.cellItems!!.findPrecise(request)
        if (l != null) {
            results = l.copy()
            if (l.stackSize <= size) {
                results.stackSize = l.stackSize
                if (mode == Actionable.MODULATE) {
                    l.stackSize = 0
                    saveChanges()
                }
            } else {
                results.stackSize = size
                if (mode == Actionable.MODULATE) {
                    l.stackSize = l.stackSize - size
                    saveChanges()
                }
            }
        }

        return results
    }

    override fun getChannel(): IStorageChannel<T> = cell.getChannel()

    private fun isStorageCell(input: T): Boolean {
        if (input is IAEItemStack) {
            val stack = input as IAEItemStack
            val type = getStorageCell(stack.definition)
            return type != null && !type.storableInStorageCell()
        }
        return false
    }

    companion object {

        fun isCell(input: ItemStack?): Boolean {
            return getStorageCell(input) != null
        }

        private fun getStorageCell(input: ItemStack?): IAEAdditionsStorageCell<*>? {
            if (input != null) {
                val type = input.item
                if (type is IAEAdditionsStorageCell<*>) {
                    return type
                }
            }
            return null
        }

        private fun <T: IAEStack<T>> isCellEmpty(inv: ICellInventory<T>?): Boolean {
            return inv?.getAvailableItems(inv.getChannel().createList())?.isEmpty ?: true
        }

        fun <T: IAEStack<T>> createInventory(itemStack: ItemStack, container: ISaveProvider?): ICellInventory<T>? {
            return try {
                if (itemStack == null) {
                    throw AppEngException("ItemStack was used as a cell, but was not a cell!")
                }
                val type: Item = itemStack.getItem()
                val cellType: IAEAdditionsStorageCell<T>
                cellType = if (type is IAEAdditionsStorageCell<*>) {
                    type as IAEAdditionsStorageCell<T>
                } else {
                    throw AppEngException("ItemStack was used as a cell, but was not a cell!")
                }
                if (!cellType.isStorageCell(itemStack)) {
                    throw AppEngException("ItemStack was used as a cell, but was not a cell!")
                }
                AEAdditionsCellInventory(cellType, itemStack, container)
            } catch (e: AppEngException) {
                AELog.error(e)
                null
            }
        }
    }
}