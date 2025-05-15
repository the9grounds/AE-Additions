package com.the9grounds.aeadditions.forge.registries

import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.util.ChannelHolder
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraftforge.common.capabilities.*
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.event.AttachCapabilitiesEvent

object CapabilityRegistry {
    val CHANNEL_HOLDER = CapabilityManager.get(object : CapabilityToken<ChannelHolder>() {})
    
    fun registerCapabilities(event: RegisterCapabilitiesEvent) {
        event.register(ChannelHolder::class.java)
    }
    
    fun registerLevelCapability(event: AttachCapabilitiesEvent<Level>) {
        val eventObject = event.`object`
        if (eventObject !is ServerLevel) {
            return
        }
        
        event.addCapability(ResourceLocation(AEAdditions.ID), object : ICapabilitySerializable<CompoundTag> {
            val channelHolder = ChannelHolder(eventObject)
            
            override fun <T : Any> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T> {
                if (cap == CHANNEL_HOLDER && eventObject is ServerLevel) {
                    return LazyOptional.of({ channelHolder as T })
                }
                
                return LazyOptional.of(null)
            }

            override fun serializeNBT(): CompoundTag {
                return channelHolder.save(CompoundTag())
            }

            override fun deserializeNBT(nbt: CompoundTag?) {
                channelHolder.load(nbt!!)
            }
        })
    }
}