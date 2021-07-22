package com.the9grounds.aeadditions.util

import appeng.api.storage.channels.IFluidStorageChannel
import appeng.api.storage.channels.IItemStorageChannel
import appeng.util.item.AEItemStack
import com.the9grounds.aeadditions.api.gas.IChemicalStorageChannel
import com.the9grounds.aeadditions.integration.appeng.AppEng

object StorageChannels {

    val ITEM = AppEng.API!!.storage().getStorageChannel(IItemStorageChannel::class.java)

    val FLUID = AppEng.API!!.storage().getStorageChannel(IFluidStorageChannel::class.java)
    
    val CHEMICAL = AppEng.API!!.storage().getStorageChannel(IChemicalStorageChannel::class.java)

}