package com.the9grounds.aeadditions.integration.appeng

import appeng.api.AEAddon
import appeng.api.IAEAddon
import appeng.api.IAppEngApi
import com.the9grounds.aeadditions.api.gas.IChemicalStorageChannel
import com.the9grounds.aeadditions.integration.Mods
import com.the9grounds.aeadditions.integration.mekanism.chemical.ChemicalStorageChannel
import com.the9grounds.aeadditions.me.storage.AEAdditionsCellHandler
import com.the9grounds.aeadditions.registries.Cells

@AEAddon
class AppEng : IAEAddon {

    companion object {
        var API: IAppEngApi? = null

        fun initCellHandler() {
            API!!.registries().cell().addCellHandler(AEAdditionsCellHandler())
        }
    }

    override fun onAPIAvailable(api: IAppEngApi) {
        API = api

        initCellHandler()
        Cells.init()
        if (Mods.MEKANISM.isEnabled) {
            api.storage().registerStorageChannel(IChemicalStorageChannel::class.java, ChemicalStorageChannel())
        }
    }
}