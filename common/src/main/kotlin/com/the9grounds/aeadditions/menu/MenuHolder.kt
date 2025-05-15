package com.the9grounds.aeadditions.menu

import com.the9grounds.aeadditions.AEAdditions
import dev.architectury.registry.menu.MenuRegistry
import dev.architectury.registry.registries.DeferredRegister
import net.minecraft.core.registries.Registries
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.inventory.MenuType

object MenuHolder {

    val MENUS = DeferredRegister.create(AEAdditions.ID, Registries.MENU)

    val menuMEWirelessTransceiver = MENUS.register("me_wireless_transceiver") { MenuType(::MEWirelessTransceiverMenu, FeatureFlags.DEFAULT_FLAGS) }.get()
    
    fun init () {}
}