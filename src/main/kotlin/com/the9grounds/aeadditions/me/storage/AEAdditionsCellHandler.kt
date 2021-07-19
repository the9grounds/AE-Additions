package com.the9grounds.aeadditions.me.storage

import appeng.api.storage.IStorageChannel
import appeng.api.storage.cells.ICellHandler
import appeng.api.storage.cells.ICellInventoryHandler
import appeng.api.storage.cells.ISaveProvider
import appeng.api.storage.data.IAEStack
import appeng.me.storage.BasicCellInventoryHandler
import net.minecraft.item.ItemStack

class AEAdditionsCellHandler : ICellHandler {
    override fun isCell(`is`: ItemStack?): Boolean = AEAdditionsCellInventory.isCell(`is`)

    override fun <T : IAEStack<T>> getCellInventory(
        `is`: ItemStack,
        container: ISaveProvider?,
        channel: IStorageChannel<T>
    ): ICellInventoryHandler<T>? {
        val inv = AEAdditionsCellInventory.createInventory<T>(`is`, container)

        if (inv == null || inv.channel != channel) {
            return null
        }

        return BasicCellInventoryHandler(inv, channel)
    }
}