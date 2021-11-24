package com.the9grounds.aeadditions.api

import appeng.api.implementations.items.IStorageCell
import appeng.api.storage.ICellWorkbenchItem
import appeng.api.storage.IStorageChannel
import appeng.api.storage.data.IAEStack
import net.minecraft.item.ItemStack

interface IAEAdditionsStorageCell<T: IAEStack<T>?> : IStorageCell<T>, ICellWorkbenchItem {
    
}