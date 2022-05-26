package com.the9grounds.aeadditions.data

import com.the9grounds.aeadditions.data.provider.StorageCellsRecipeProvider
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent

object AEAdditionsDataGenerator {
    fun onGatherData(event: GatherDataEvent) {
        event.generator.addProvider(StorageCellsRecipeProvider(event.generator))
    }
}