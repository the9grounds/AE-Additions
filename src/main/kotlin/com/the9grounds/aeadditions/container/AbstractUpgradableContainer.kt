package com.the9grounds.aeadditions.container

import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.container.ContainerType

abstract class AbstractUpgradableContainer<T>(
    type: ContainerType<*>?,
    id: Int,
    playerInventory: PlayerInventory,
    bindInventory: Boolean,
    host: Any
) : AbstractContainer<T>(type, id, playerInventory, bindInventory, host) {
    
}