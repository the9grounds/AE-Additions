package com.the9grounds.aeadditions.registries.client

import com.the9grounds.aeadditions.client.gui.MEWirelessTransceiverScreen
import com.the9grounds.aeadditions.menu.MenuHolder
import net.minecraft.client.gui.screens.MenuScreens
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType

object Screens {
    
    fun init() {
        register(MenuHolder.menuMEWirelessTransceiver, ::MEWirelessTransceiverScreen)
    }

    private fun <T: AbstractContainerMenu>register(type: MenuType<T>, factory: (T, Inventory, Component) -> AbstractContainerScreen<T>) {
        MenuScreens.register(type) { container, playerInventory, title ->
            factory(container, playerInventory, title)
        }
    }
}