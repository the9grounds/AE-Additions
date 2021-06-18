package com.the9grounds.aeadditions.api.inventory

import net.minecraft.inventory.IInventory

interface IToggleableSlotsInventory : IInventory {

    val enabledSlots: MutableMap<Int, Boolean>
}