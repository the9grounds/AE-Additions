package com.the9grounds.aeadditions.util

import appeng.api.AEApi
import appeng.api.storage.channels.IFluidStorageChannel
import appeng.api.storage.channels.IItemStorageChannel
import appeng.api.storage.data.IAEItemStack
import com.the9grounds.aeadditions.api.gas.IGasStorageChannel
import com.the9grounds.aeadditions.integration.Integration

object StorageChannels {
    @JvmField val ITEM = AEApi.instance().storage().getStorageChannel(IItemStorageChannel::class.java)

    @JvmField val FLUID = AEApi.instance().storage().getStorageChannel(IFluidStorageChannel::class.java)

    @JvmField val GAS = if (Integration.Mods.MEKANISMGAS.isEnabled) AEApi.instance().storage().getStorageChannel(IGasStorageChannel::class.java) else null
}