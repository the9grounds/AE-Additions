package com.the9grounds.aeadditions.api

import appeng.api.stacks.AEItemKey
import appeng.api.stacks.AEKey
import appeng.api.stacks.AEKeyType
import appeng.api.storage.cells.IBasicCellItem
import appeng.api.storage.cells.ICellWorkbenchItem
import appeng.api.storage.cells.ISaveProvider
import appeng.me.cells.BasicCellHandler
import appeng.util.ConfigInventory
import com.google.common.base.Preconditions
import com.the9grounds.aeadditions.me.storage.ExtendedDiskCellHandler
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

interface IAEAdditionsDiskCell : ICellWorkbenchItem {
    open fun getKeyType(): AEKeyType

    open fun getBytes(var1: ItemStack?): Int

    open fun isBlackListed(cellItem: ItemStack?, requestedAddition: AEKey): Boolean {
        return if ((requestedAddition as AEItemKey).item is IBasicCellItem) {
            BasicCellHandler.INSTANCE.getCellInventory(
                (requestedAddition as AEItemKey).toStack(),
                null as ISaveProvider?
            )!!
                .usedBytes > 0L
        } else {
            false
        }
    }

    fun storableInStorageCell(): Boolean {
        return false
    }

    fun isStorageCell(i: ItemStack?): Boolean {
        return true
    }

    open fun getIdleDrain(): Double

    override fun getConfigInventory(var1: ItemStack?): ConfigInventory

    fun addCellInformationToTooltip(`is`: ItemStack, lines: MutableList<Component>) {
        Preconditions.checkArgument(`is`.item === this)
        ExtendedDiskCellHandler.addCellInformationToTooltip(`is`, lines)
    }
}