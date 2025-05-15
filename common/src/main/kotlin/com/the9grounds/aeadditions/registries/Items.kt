package com.the9grounds.aeadditions.registries

import com.the9grounds.aeadditions.AEAdditions
import dev.architectury.registry.registries.DeferredRegister
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item

object Items {
    val REGISTRY = DeferredRegister.create(AEAdditions.ID, Registries.ITEM)
}