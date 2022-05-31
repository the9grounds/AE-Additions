package com.the9grounds.aeadditions.util

import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.TextComponent
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.Level
import java.util.*

class ChannelHolder(val level: Level) {
    val channelInfos: MutableList<ChannelInfo> = mutableListOf()
    
    val channels: MutableMap<ChannelInfo, Channel> = mutableMapOf()
    
    
    
    fun setupTestChannels(player: ServerPlayer, name: String) {
//        ServerLifecycleHooks.getCurrentServer().allLevels
        
        val level = player.level as ServerLevel
        
        val alreadyExists = channelInfos.find { it.name == name && ((it.isPrivate && it.creator == player.uuid) || !it.isPrivate) } !== null
        
        if (!alreadyExists) {
            val channelInfo = ChannelInfo(UUID.randomUUID(), level, name, false, null, null)

            channelInfos.add(channelInfo)
            
            player.sendMessage(TextComponent("Channel created"), player.uuid)
        } else {
            player.sendMessage(TextComponent("Channel already exists"), player.uuid)
        }
    }
    
    fun getOrCreateChannel(channelInfo: ChannelInfo): Channel {
        var channel = channels.get(channelInfo)
        
        if (channel == null) {
            channel = Channel(channelInfo, null, mutableListOf())
            
            channels.put(channelInfo, channel)
        }
        
        return channel
    }
    
    fun getChannelByName(name: String): ChannelInfo? {
        return channelInfos.find { it.name == name }
    }
    
    fun getChannelById(id: UUID): ChannelInfo? {
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
                channelInfos.add(i, ChannelInfo.readFromNbt(channelInfoTag, level))
            }
        }
    }
}