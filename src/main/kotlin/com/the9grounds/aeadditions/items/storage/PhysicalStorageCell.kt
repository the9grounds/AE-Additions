package com.the9grounds.aeadditions.items.storage

import appeng.api.storage.IStorageChannel
import appeng.api.storage.data.IAEItemStack
import com.the9grounds.aeadditions.util.StorageChannels
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

class PhysicalStorageCell(props: Properties, component: Item, kiloBytes: Int, private val idleDrainProp: Double, private val bytesPerType: Int ) : AbstractStorageCell<IAEItemStack>(props, component, kiloBytes, 63) {
    override fun getBytesPerType(cellItem: ItemStack): Int = bytesPerType

    override fun getIdleDrain(): Double = idleDrainProp

    override fun getChannel(): IStorageChannel<IAEItemStack> {
        return StorageChannels.ITEM
    }
}