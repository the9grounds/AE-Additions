package com.the9grounds.aeadditions.tileentity

import appeng.api.networking.IGridHost
import appeng.api.networking.storage.IStorageGrid
import appeng.api.storage.IMEMonitor
import appeng.api.storage.data.IAEFluidStack
import appeng.api.storage.data.IAEItemStack
import appeng.api.util.AEPartLocation
import com.the9grounds.aeadditions.util.StorageChannels

interface INetworkStorage {
    fun getStorageGrid(side: AEPartLocation): IStorageGrid? {
        if (this !is IGridHost) {
            return null
        }

        val host = this as IGridHost

        if (host.getGridNode(side) == null) {
            return null
        }

        val grid = host.getGridNode(side)!!.grid

        if (grid == null) return null

        return grid.getCache(IStorageGrid::class.java)
    }

    fun getFluidInventory(side: AEPartLocation): IMEMonitor<IAEFluidStack>? {
        val storageGrid = getStorageGrid(side)
        if (storageGrid == null) {
            return null
        }

        return storageGrid.getInventory(StorageChannels.FLUID)
    }

    fun getItemInventory(side: AEPartLocation): IMEMonitor<IAEItemStack>? {
        val storageGrid = getStorageGrid(side)
        if (storageGrid == null) {
            return null
        }

        return storageGrid.getInventory(StorageChannels.ITEM)
    }
}