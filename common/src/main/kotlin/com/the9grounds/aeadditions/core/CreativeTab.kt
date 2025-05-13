package com.the9grounds.aeadditions.core

import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.registries.Items
import dev.architectury.registry.CreativeTabRegistry
import dev.architectury.registry.registries.DeferredRegister
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.CreativeModeTab.ItemDisplayParameters
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack


object CreativeTab {
    val REGISTRY = DeferredRegister.create(AEAdditions.ID, Registries.CREATIVE_MODE_TAB)

    val tab = REGISTRY.register("main") {
        CreativeTabRegistry.create(Component.literal("AE Additions")) {
            ItemStack(Items.SUPER_CELL_COMPONENT_65M, 1)
        }
    }

    fun init() {

    }
}