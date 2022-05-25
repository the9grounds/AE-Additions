package com.the9grounds.aeadditions.item.storage

import appeng.api.storage.IStorageChannel
import com.the9grounds.aeadditions.api.chemical.IAEChemicalStack
import com.the9grounds.aeadditions.util.StorageChannels
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

class ChemicalStorageCell(props: Item.Properties, component: Item, kiloBytes: Int, private val idleDrainProp: Double, private val bytesPerType: Int) : AbstractStorageCell<IAEChemicalStack>(props, component, kiloBytes, 5) {
    override fun getBytesPerType(cellItem: ItemStack): Int = bytesPerType

    override fun getIdleDrain(): Double = idleDrainProp

    override fun getChannel(): IStorageChannel<IAEChemicalStack> = StorageChannels.CHEMICAL!!
}