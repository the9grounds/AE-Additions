package com.the9grounds.aeadditions.data

import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.data.provider.StorageCellsRecipeProvider
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent

@Mod.EventBusSubscriber(modid = AEAdditions.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
object AEAdditionsDataGenerator {
    @SubscribeEvent
    fun data(event: GatherDataEvent) {
        if (event.includeServer()) {
            event.generator.addProvider(StorageCellsRecipeProvider(event.generator))
        }
    }
}