package com.the9grounds.aeadditions.core

import com.the9grounds.aeadditions.registries.Items
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack

class AEItemGroup(label: String) : ItemGroup(label) {

    override fun createIcon(): ItemStack = ItemStack(Items.ITEM_STORAGE_CELL_256k, 1)
}