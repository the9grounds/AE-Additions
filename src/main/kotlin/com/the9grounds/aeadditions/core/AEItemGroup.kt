package com.the9grounds.aeadditions.core

import com.the9grounds.aeadditions.registries.Items
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack

class AEItemGroup(label: String) : CreativeModeTab(label) {

    override fun makeIcon(): ItemStack = ItemStack(Items.SUPER_CELL_COMPONENT_65M, 1)
}