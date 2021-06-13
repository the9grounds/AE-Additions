package com.the9grounds.aeadditions.util

import appeng.api.networking.security.IActionSource
import com.the9grounds.aeadditions.api.IExternalGasStorageHandler
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing

object GasStorageRegistry {
    private val handler = mutableListOf<IExternalGasStorageHandler>()

    @JvmStatic fun addExternalStorageInterface(gasHandler: IExternalGasStorageHandler) {
        handler += gasHandler
    }

    @JvmStatic fun getHandler(te: TileEntity, opposite: EnumFacing, mySrc: IActionSource): IExternalGasStorageHandler? {
        for (x in handler) {
            if (x.canHandle(te, opposite, mySrc)) {
                return x
            }
        }
        return null
    }
}