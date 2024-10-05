package com.the9grounds.aeadditions.core

import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.registries.Items
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.CreativeModeTab.ItemDisplayParameters
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraftforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.forge.registerObject


object CreativeTab {
    val items = mutableListOf<Item>()
    val REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AEAdditions.ID)

    val tab by REGISTRY.registerObject("main") {
        CreativeModeTab.builder()
                .title(Component.literal("AE Additions"))
                .icon( { ItemStack(Items.SUPER_CELL_COMPONENT_65M, 1) })
                .displayItems { itemDisplayParameters: ItemDisplayParameters, output: CreativeModeTab.Output -> buildItems(itemDisplayParameters, output) }
                .build()
    }

    fun init() {

    }

    fun add(item: Item) {
        items.add(item)
    }

    fun buildItems(itemDisplayParameters: ItemDisplayParameters, output: CreativeModeTab.Output) {
        for (item in items) {
            output.accept(item)
        }
    }
}