package com.the9grounds.aeadditions.registries.client

import com.the9grounds.aeadditions.client.gui.MEWirelessTransceiverScreen
import com.the9grounds.aeadditions.menu.MenuHolder
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent

@OnlyIn(Dist.CLIENT)
object Screens {

    fun registerScreens(event: RegisterMenuScreensEvent) {
        event.register(MenuHolder.menuMEWirelessTransceiver, ::MEWirelessTransceiverScreen)
    }
}