package extracells.me.storage

import appeng.api.storage.ICellHandler
import appeng.api.storage.ICellInventoryHandler
import appeng.api.storage.ISaveProvider
import appeng.api.storage.IStorageChannel
import appeng.api.storage.data.IAEStack
import appeng.me.storage.BasicCellInventoryHandler
import net.minecraft.item.ItemStack

class ExtraCellsCellHandler: ICellHandler {
    override fun isCell(itemStack: ItemStack?): Boolean {
        return ExtraCellsCellInventory.isCell(itemStack)
    }

    override fun <T : IAEStack<T>?> getCellInventory(
        itemStack: ItemStack?,
        container: ISaveProvider?,
        channel: IStorageChannel<T>?
    ): ICellInventoryHandler<T>? {
        val inventory = ExtraCellsCellInventory.createInventory<T>(itemStack, container)

        if (inventory == null || inventory.channel != channel) {
            return null
        }

        return BasicCellInventoryHandler(inventory, channel)
    }
}