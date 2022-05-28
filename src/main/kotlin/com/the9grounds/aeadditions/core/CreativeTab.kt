package com.the9grounds.aeadditions.core

import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.registries.Items
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack

object CreativeTab {
    val group = FabricItemGroupBuilder.build(ResourceLocation(AEAdditions.MOD_ID, "item_group")) { ItemStack(Items.CELL_COMPONENT_65536k)}
}