package com.the9grounds.aeadditions.item.storage

import appeng.api.implementations.items.IStorageComponent
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

class StorageComponentItem(props: Properties, private val storageInKb: Int) : Item(props), IStorageComponent {

    override fun getBytes(`is`: ItemStack?): Int =  storageInKb * 1024

    override fun isStorageComponent(`is`: ItemStack?): Boolean = true
}