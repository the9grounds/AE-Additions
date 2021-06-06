package extracells.util

import appeng.api.AEApi
import appeng.api.storage.channels.IFluidStorageChannel
import appeng.api.storage.channels.IItemStorageChannel
import appeng.api.storage.data.IAEItemStack
import extracells.api.gas.IGasStorageChannel
import extracells.integration.Integration

object StorageChannels {
    @JvmField val ITEM = AEApi.instance().storage().getStorageChannel(IItemStorageChannel::class.java)

    @JvmField val FLUID = AEApi.instance().storage().getStorageChannel(IFluidStorageChannel::class.java)

    @JvmField val GAS = if (Integration.Mods.MEKANISMGAS.isEnabled) AEApi.instance().storage().getStorageChannel(IGasStorageChannel::class.java) else null
}