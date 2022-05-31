package com.the9grounds.aeadditions.core

import com.the9grounds.aeadditions.AEAdditions
import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(modid = AEAdditions.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
object AEAConfig {
    
    val COMMON_SPEC: ForgeConfigSpec
    val COMMON_CONFIG: CommonConfig
    
    init {
        val commonPair = ForgeConfigSpec.Builder().configure(::CommonConfig)
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
    
    class CommonConfig(builder: ForgeConfigSpec.Builder) {
        val meWirelessTransceiverBasePower: ForgeConfigSpec.ConfigValue<Int>;
        val meWirelessTransceiverDistanceMultiplier: ForgeConfigSpec.ConfigValue<Double>
        
        init {
            builder.push("meWirelessTransceiver")
            meWirelessTransceiverBasePower = builder.define("basePower", 10)
            meWirelessTransceiverDistanceMultiplier = builder.define("distanceMultiplier", 1.0)
        }
    }
}