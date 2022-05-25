package com.the9grounds.aeadditions.integration.appeng

import appeng.api.AEAddon
import appeng.api.IAEAddon
import appeng.api.IAppEngApi
import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.me.storage.AEAdditionsCellHandler

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

        AEAdditions.onAppEngReady(api)
    }
}