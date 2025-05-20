package com.the9grounds.aeadditions.util

import com.the9grounds.aeadditions.blockentity.MEWirelessTransceiverBlockEntity
import net.minecraft.nbt.CompoundTag
import java.util.*

open class ChannelHolder {
    val channelInfos: MutableList<ChannelInfo> = mutableListOf()
    
    val channels: MutableMap<ChannelInfo, Channel> = mutableMapOf()
    
    fun findChannelForBlockEntity(blockEntity: MEWirelessTransceiverBlockEntity): Channel? {
        for (channel in channels) {
            if (channel.value.broadcaster === blockEntity) {
                return channel.value
            }
            
            val subscriber = channel.value.subscribers.find { it === blockEntity }
            
            if (subscriber != null) {
                return channel.value
            }
        }
        
        return null
    }
    
    fun getOrCreateChannel(channelInfo: ChannelInfo): Channel {
        var channel = channels.get(channelInfo)
        
        if (channel == null) {
            channel = Channel(channelInfo, null, mutableListOf())
            
            channels.put(channelInfo, channel)
            persist()
        }
        
        return channel
    }

    fun getChannel(channelInfo: ChannelInfo): Channel? {
        return channels[channelInfo]
    }
    
    fun getChannelInfoByName(name: String): ChannelInfo? {
        return channelInfos.find { it.name == name }
    }
    
    fun getChannelInfoById(id: UUID): ChannelInfo? {
        return channelInfos.find { it.id == id }
    }
    
    fun save(tag: CompoundTag): CompoundTag {
        val channelInfoSize = channelInfos.size
        
        channelInfos.forEachIndexed { index, channelInfo ->
            tag.put("$index", channelInfo.saveToNbt())
        }
        
        tag.putInt("size", channelInfoSize)
        
        return tag
    }
    
    fun load(tag: CompoundTag) {
        val size = tag.getInt("size")
        
        for (i in 0 until size) {
            val channelInfoTag = tag.getCompound("$i")
            if (!channelInfoTag.isEmpty) {
                channelInfos.add(i, ChannelInfo.readFromNbt(channelInfoTag))
            }
        }
    }

    open fun persist() {}

    fun removeChannel(channelInfo: ChannelInfo) {
        val channel = getOrCreateChannel(channelInfo)
        
        val blockEntity = channel.broadcaster
        val subscribers = channel.subscribers
        
        channel.broadcaster?.destroyConnections()
        
        channelInfos.removeIf { it.id == channelInfo.id }
        channels.remove(channelInfo)

        blockEntity?.setChanged()
        subscribers.forEach { it.removedFromChannel(channelInfo) }
        subscribers.forEach { it.setChanged() }
        channel.subscribers.clear()
        channel.broadcaster = null
        persist()
    }
}