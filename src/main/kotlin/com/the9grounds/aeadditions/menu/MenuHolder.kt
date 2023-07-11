package com.the9grounds.aeadditions.menu

import com.the9grounds.aeadditions.AEAdditions
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.inventory.MenuType
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.registerObject

object MenuHolder {
    
    val REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, AEAdditions.ID)
    
    val menuMEWirelessTransceiver by REGISTRY.registerObject("me_wireless_transceiver") { MenuType(::MEWirelessTransceiverMenu, FeatureFlags.DEFAULT_FLAGS) }
    
    fun init () {}
}