package com.the9grounds.aeadditions.api

import net.minecraftforge.items.IItemHandler

interface ISegmentedInventory {
    fun getInventoryByName(name: String): IItemHandler?
}