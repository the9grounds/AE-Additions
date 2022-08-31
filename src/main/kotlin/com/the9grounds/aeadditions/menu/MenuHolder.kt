package com.the9grounds.aeadditions.menu

import com.the9grounds.aeadditions.AEAdditions
import net.minecraft.world.inventory.MenuType
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import thedarkcolour.kotlinforforge.forge.registerObject

object MenuHolder {
    
    val REGISTRY = KDeferredRegister.create(ForgeRegistries.MENU_TYPES, AEAdditions.ID)
    
    val menuMEWirelessTransceiver by REGISTRY.registerObject("me_wireless_transceiver") { MenuType(::MEWirelessTransceiverMenu) }
    
    fun init () {}
}