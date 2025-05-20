package com.the9grounds.aeadditions.registries

import com.the9grounds.aeadditions.AEAdditions
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object CreativeTab {
    val REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AEAdditions.ID)

    val TAB by REGISTRY.register("ae2additions_tab") { ->
        CreativeModeTab.builder().title(Component.literal("AE Additions"))
            .icon { ->
                ItemStack(Items.SUPER_CELL_COMPONENT_65M, 1)
            }
            .displayItems { i, o ->
                for (item in Items.CREATIVE_TAB_ITEMS) {
                    o.accept(item)
                }
            }.build()
    }
}