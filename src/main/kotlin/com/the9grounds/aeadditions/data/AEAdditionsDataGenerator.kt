package com.the9grounds.aeadditions.data

import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.data.provider.AEAdditionsRecipeProvider
import com.the9grounds.aeadditions.data.provider.BlockModelProvider
import net.minecraftforge.data.event.GatherDataEvent

object AEAdditionsDataGenerator {
    fun onGatherData(event: GatherDataEvent) {
        val pack = event.generator.getVanillaPack(true)

        pack.addProvider {
            AEAdditionsRecipeProvider(it)
        }
        pack.addProvider {
            BlockModelProvider(it, AEAdditions.ID, event.existingFileHelper)
        }
//        pack.addProvider {
//            AEAItemModelProvider(it, AEAdditions.ID, event.existingFileHelper)
//        }
//        event.generator.addProvider(true, AEALootTableProvider(event.generator))
    }
}