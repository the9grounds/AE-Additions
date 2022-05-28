package com.the9grounds.aeadditions.api

import appeng.api.stacks.AEKeyType

object AEAApi {
    val superCellTypes = mutableListOf(
        AEKeyType.items(),
        AEKeyType.fluids()
    )

    fun registerTypeForSuperCell(keyType: AEKeyType) {
        superCellTypes.add(keyType)
    }
}