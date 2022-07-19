package com.the9grounds.aeadditions.core

import com.the9grounds.aeadditions.registries.Capability
import net.minecraft.world.level.chunk.LevelChunk
import net.minecraftforge.event.world.ChunkEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.util.thread.SidedThreadGroups

object EventHandler {
    
    @SubscribeEvent
    fun chunkLoadEvent(event: ChunkEvent.Load) {
        if (Thread.currentThread().threadGroup !== SidedThreadGroups.SERVER) {
            return
        }
        
        val chunk = event.chunk as LevelChunk
        val optional = chunk.level.getCapability(Capability.CHANNEL_HOLDER).resolve()
        
        if (!optional.isPresent) {
            return
        }
        
        val channelHolder = optional.get()
        
        for (channel in channelHolder.channels) {
            if (channel.value.broadcaster != null) {
                val filtered = chunk.blockEntities.filter { it.value == channel.value.broadcaster }
                
                if (filtered.size > 1) {
                    channel.value.broadcaster!!.setupLinks()
                }
            }
            
            channel.value.subscribers.forEach {
                val filtered = chunk.blockEntities.filter { innerIt -> innerIt.value == it }

                if (filtered.size > 1) {
                    channel.value.broadcaster?.setupLinks()
                }
            }
        }
    }
}