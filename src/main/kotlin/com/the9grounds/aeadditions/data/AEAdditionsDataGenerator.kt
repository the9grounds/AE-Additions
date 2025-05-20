package com.the9grounds.aeadditions.data

import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.data.provider.AEAItemModelProvider
import com.the9grounds.aeadditions.data.provider.AEAdditionsRecipeProvider
import com.the9grounds.aeadditions.data.provider.BlockModelProvider
import net.neoforged.neoforge.data.event.GatherDataEvent

object AEAdditionsDataGenerator {
    fun onGatherData(event: GatherDataEvent) {
        val pack = event.generator.getVanillaPack(true)
        val registries = event.lookupProvider;

        pack.addProvider {
            AEAdditionsRecipeProvider(it, registries)
        }
        pack.addProvider {
            BlockModelProvider(it, AEAdditions.ID, event.existingFileHelper)
        }
        pack.addProvider {
            AEAItemModelProvider(it, AEAdditions.ID, event.existingFileHelper)
        }
//        event.generator.addProvider(true, AEALootTableProvider(event.generator))
    }
}