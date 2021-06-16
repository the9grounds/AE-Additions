package com.the9grounds.aeadditions.me.storage

import appeng.api.config.Actionable
import appeng.api.exceptions.AppEngException
import appeng.api.networking.security.IActionSource
import appeng.api.storage.ICellInventory
import appeng.api.storage.ISaveProvider
import appeng.api.storage.IStorageChannel
import appeng.api.storage.data.IAEStack
import appeng.core.AEConfig
import appeng.core.AELog
import com.the9grounds.aeadditions.api.IAEAdditionsStorageCell
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound

// This is mostly a copy of AE's BasicCellInventory, with changes for the long -> int conversion issue
class AEAdditionsCellInventory<T: IAEStack<T>?>(cellType: IAEAdditionsStorageCell<T>, itemStack: ItemStack, container: ISaveProvider?) : AbstractAEAdditionsInventory<T>(cellType, itemStack, container) {

    private var channel: IStorageChannel<T>? = null

    init {
        this.channel = cellType.getChannel()
    }

    override fun injectItems(input: T, mode: Actionable?, src: IActionSource?): T? {
        if (input == null) {
            return null
        }
        if (input.getStackSize() == 0L) {
            return null
        }

        if (cellType!!.isBlackListed(this.itemStack!!, input)) {
            return input
        }
        // Don't think I have to worry about this since you can't inject a fluid cell into a fluid cell
//        if (this.isStorageCell(input)) {
//            val meInventory: ICellInventory<*> =
//                BasicCellInventory.createInventory((input as IAEItemStack).createItemStack(), null)
//            if (!BasicCellInventory.isCellEmpty(meInventory)) {
//                return input
//            }
//        }

        val l = cellItems!!.findPrecise(input)
        if (l != null) {
            val remainingItemCount = this.remainingItemCount
            if (remainingItemCount <= 0) {
                return input
            }
            return if (input.getStackSize() > remainingItemCount) {
                val r = input.copy()
                r!!.setStackSize(r!!.getStackSize() - remainingItemCount)
                if (mode == Actionable.MODULATE) {
                    l.setStackSize(l.getStackSize() + remainingItemCount)
                    saveChanges()
                }
                r
            } else {
                if (mode == Actionable.MODULATE) {
                    l.setStackSize(l.getStackSize() + input.getStackSize())
                    saveChanges()
                }
                null
            }
        }

        if (canHoldNewItem()) // room for new type, and for at least one item!
        {
            val remainingItemCount = this.remainingItemCount - this.bytesPerType * itemsPerByte
            if (remainingItemCount > 0) {
                if (input.getStackSize() > remainingItemCount) {
                    val toReturn = input.copy()
                    toReturn!!.setStackSize(input.getStackSize() - remainingItemCount)
                    if (mode == Actionable.MODULATE) {
                        val toWrite = input.copy()
                        toWrite!!.setStackSize(remainingItemCount)
                        cellItems!!.add(toWrite)
                        saveChanges()
                    }
                    return toReturn
                }
                if (mode == Actionable.MODULATE) {
                    val copy = input.copy()
                    cellItems!!.add(copy)
                    saveChanges()
                }
                return null
            }
        }

        return input
    }

    override fun extractItems(request: T?, mode: Actionable?, src: IActionSource?): T? {
        if (request == null) {
            return null
        }

        val size = Math.min(Int.MAX_VALUE.toLong(), request.getStackSize())

        var Results: T? = null

        val l = cellItems!!.findPrecise(request)
        if (l != null) {
            Results = l.copy()
            if (l.getStackSize() <= size) {
                Results!!.setStackSize(l.getStackSize())
                if (mode == Actionable.MODULATE) {
                    l.setStackSize(0)
                    saveChanges()
                }
            } else {
                Results!!.setStackSize(size)
                if (mode == Actionable.MODULATE) {
                    l.setStackSize(l.getStackSize() - size)
                    saveChanges()
                }
            }
        }

        return Results
    }

    override fun getChannel(): IStorageChannel<T>? = channel

    override fun loadCellItem(compoundTag: NBTTagCompound?, stackSize: Long): Boolean {

        // Now load the item stack
        val t: T?
        try {
            t = getChannel()!!.createFromNBT(compoundTag!!)
            if (t == null) {
                AELog.warn("Removing item $compoundTag from storage cell because the associated item type couldn't be found.")
                return false
            }
        } catch (ex: Throwable) {
            if (AEConfig.instance().isRemoveCrashingItemsOnLoad) {
                AELog.warn(ex, "Removing item $compoundTag from storage cell because loading the ItemStack crashed.")
                return false
            }
            throw ex
        }

        t.setStackSize(stackSize)

        if (stackSize > 0) {
            cellItems!!.add(t)
        }

        return true
    }

    companion object {

        fun isCell(itemStack: ItemStack?): Boolean {
            return getStorageCell(itemStack) != null
        }

        private fun getStorageCell(itemStack: ItemStack?): IAEAdditionsStorageCell<*>? {
            if (itemStack != null) {
                val type = itemStack.item

                if (type is IAEAdditionsStorageCell<*>) {
                    return type
                }
            }

            return null
        }

        @JvmStatic fun <T : IAEStack<T>?> createInventory(itemStack: ItemStack?, container: ISaveProvider?): ICellInventory<T>? {
            try {
                if (itemStack == null) {
                    throw AppEngException("ItemStack was used as a cell, but was not a cell!")
                }

                val type = itemStack.item
                val cellType: IAEAdditionsStorageCell<T>

                if (type is IAEAdditionsStorageCell<*>) {
                    cellType = type as IAEAdditionsStorageCell<T>
                } else {
                    throw AppEngException("ItemStack was used as a cell, but was not a cell!")
                }

                if (!cellType.isStorageCell(itemStack)) {
                    throw AppEngException("ItemStack was used as a cell, but was not a cell")
                }

                return AEAdditionsCellInventory(cellType, itemStack, container)

            } catch (e: AppEngException) {
                AELog.error(e)

                return null
            }
        }


    }
}