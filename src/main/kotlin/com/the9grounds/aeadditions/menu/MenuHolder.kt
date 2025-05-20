package com.the9grounds.aeadditions.menu

import com.the9grounds.aeadditions.AEAdditions
import net.minecraft.core.registries.Registries
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.inventory.MenuType
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object MenuHolder {
    
    val REGISTRY = DeferredRegister.create(Registries.MENU, AEAdditions.ID)
    
    val menuMEWirelessTransceiver by REGISTRY.register("me_wireless_transceiver") { -> MenuType(::MEWirelessTransceiverMenu, FeatureFlags.DEFAULT_FLAGS) }
    
    fun init () {}
}