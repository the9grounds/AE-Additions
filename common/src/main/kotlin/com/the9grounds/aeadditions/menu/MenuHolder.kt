package com.the9grounds.aeadditions.menu

import com.the9grounds.aeadditions.AEAdditions
import dev.architectury.registry.menu.MenuRegistry
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.inventory.MenuType
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.registerObject

object MenuHolder {
    
    val menuMEWirelessTransceiver = MenuRegistry.reg("me_wireless_transceiver") { MenuType(::MEWirelessTransceiverMenu, FeatureFlags.DEFAULT_FLAGS) }
    
    fun init () {}
}