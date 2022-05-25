package com.the9grounds.aeadditions.util

import appeng.api.storage.IStorageChannel
import appeng.api.storage.channels.IFluidStorageChannel
import appeng.api.storage.channels.IItemStorageChannel
import com.the9grounds.aeadditions.api.chemical.IAEChemicalStack
import com.the9grounds.aeadditions.api.chemical.IChemicalStorageChannel
import com.the9grounds.aeadditions.integration.Mods
import com.the9grounds.aeadditions.integration.appeng.AppEng

object StorageChannels {

    val ITEM = AppEng.API!!.storage().getStorageChannel(IItemStorageChannel::class.java)

    val FLUID = AppEng.API!!.storage().getStorageChannel(IFluidStorageChannel::class.java)
    
    val CHEMICAL: IStorageChannel<IAEChemicalStack>?
    get() {
        if (Mods.MEKANISM.isEnabled) {
            return AppEng.API!!.storage().getStorageChannel(IChemicalStorageChannel::class.java)
        }
        
        return null
    }

}