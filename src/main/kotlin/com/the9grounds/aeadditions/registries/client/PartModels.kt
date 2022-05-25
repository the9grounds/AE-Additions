package com.the9grounds.aeadditions.registries.client

import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.ModelLoader

object PartModels {

    private val models = mutableListOf<ResourceLocation>()
    
    fun init() {
        models.forEach { ModelLoader.addSpecialModel(it) }
    }
    
    fun registerModels(partModels: List<ResourceLocation>) {
        models.addAll(partModels)
    }
}