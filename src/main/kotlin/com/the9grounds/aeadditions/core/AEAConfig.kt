package com.the9grounds.aeadditions.core

import net.neoforged.neoforge.common.ModConfigSpec

object AEAConfig {
    
    val COMMON_SPEC: ModConfigSpec
    val COMMON_CONFIG: CommonConfig
    
    init {
        val commonPair = ModConfigSpec.Builder().configure(::CommonConfig)
        COMMON_CONFIG = commonPair.left
        COMMON_SPEC = commonPair.right
    }
    
    val meWirelessTransceiverBasePower: Int
    get() = COMMON_CONFIG.meWirelessTransceiverBasePower.get()
    
    val meWirelessTransceiverDistanceMultiplier: Double
    get() = COMMON_CONFIG.meWirelessTransceiverDistanceMultiplier.get()
    
    fun save() {
        if (COMMON_SPEC.isLoaded) {
            COMMON_SPEC.save()
        }
    }
    
    class CommonConfig(builder: ModConfigSpec.Builder) {
        val meWirelessTransceiverBasePower: ModConfigSpec.ConfigValue<Int>;
        val meWirelessTransceiverDistanceMultiplier: ModConfigSpec.ConfigValue<Double>
        
        init {
            builder.push("meWirelessTransceiver")
            meWirelessTransceiverBasePower = builder.define("basePower", 10)
            meWirelessTransceiverDistanceMultiplier = builder.define("distanceMultiplier", 1.0)
        }
    }
}