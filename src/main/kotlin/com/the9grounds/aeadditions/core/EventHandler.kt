package com.the9grounds.aeadditions.core

import net.minecraftforge.event.world.ChunkEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

object EventHandler {
    
    @SubscribeEvent
    fun chunkLoadEvent(event: ChunkEvent.Load) {
        val channelHolder = event.world.
    }
}