package com.the9grounds.aeadditions.data.provider

import com.mojang.datafixers.util.Pair
import net.minecraft.data.DataGenerator
import net.minecraft.data.loot.LootTableProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.ValidationContext
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.function.Supplier

class AEALootTableProvider(p_124437_: DataGenerator) : LootTableProvider(p_124437_) {

    override fun getTables(): MutableList<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> {
        return mutableListOf(
            Pair.of(
                Supplier { BlockDropProvider() }, LootContextParamSets.BLOCK
            )
        )
    }

    override fun validate(map: MutableMap<ResourceLocation, LootTable>, validationtracker: ValidationContext) {
        map.forEach { 
            it.value.validate(validationtracker)
        }
    }
}