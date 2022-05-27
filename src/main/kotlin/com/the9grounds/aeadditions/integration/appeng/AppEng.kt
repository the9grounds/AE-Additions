package com.the9grounds.aeadditions.integration.appeng

import appeng.api.storage.StorageCells
import com.the9grounds.aeadditions.integration.Mods
import com.the9grounds.aeadditions.me.storage.AEAdditionsCellHandler
import com.the9grounds.aeadditions.me.storage.ExtendedDiskCellHandler

class AppEng {

    companion object {

        fun initCellHandler() {
            StorageCells.addCellHandler(AEAdditionsCellHandler)
            if (Mods.AE2THINGS.isEnabled) {
                StorageCells.addCellHandler(ExtendedDiskCellHandler())
            }
        }
    }
}