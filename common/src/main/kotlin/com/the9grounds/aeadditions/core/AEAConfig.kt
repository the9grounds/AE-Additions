package com.the9grounds.aeadditions.core

import com.the9grounds.aeadditions.AEAdditions

interface BaseAEAConfig {
    val meWirelessTransceiverBasePower: Double
    val meWirelessTransceiverDistanceMultiplier: Double

    fun save()
}