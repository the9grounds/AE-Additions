package com.the9grounds.aeadditions.data

import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.data.provider.AEAItemModelProvider
import com.the9grounds.aeadditions.data.provider.AEALootTableProvider
import com.the9grounds.aeadditions.data.provider.AEAdditionsRecipeProvider
import com.the9grounds.aeadditions.data.provider.BlockModelProvider
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent

object AEAdditionsDataGenerator {
    fun onGatherData(event: GatherDataEvent) {
        event.generator.addProvider(AEAdditionsRecipeProvider(event.generator))
        event.generator.addProvider(BlockModelProvider(event.generator, AEAdditions.ID, event.existingFileHelper))
        event.generator.addProvider(AEAItemModelProvider(event.generator, AEAdditions.ID, event.existingFileHelper))
        event.generator.addProvider(AEALootTableProvider(event.generator))
    }
}