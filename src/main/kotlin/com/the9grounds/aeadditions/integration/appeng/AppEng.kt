package com.the9grounds.aeadditions.integration.appeng

import appeng.api.storage.StorageCells
import com.the9grounds.aeadditions.me.storage.AEAdditionsCellHandler

class AppEng {

    companion object {

        fun initCellHandler() {
            StorageCells.addCellHandler(AEAdditionsCellHandler)
        }
    }
}