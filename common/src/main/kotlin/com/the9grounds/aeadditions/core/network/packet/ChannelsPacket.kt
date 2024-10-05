package com.the9grounds.aeadditions.core.network.packet

import com.the9grounds.aeadditions.Logger
import com.the9grounds.aeadditions.menu.MEWirelessTransceiverMenu
import com.the9grounds.aeadditions.util.Channel
import com.the9grounds.aeadditions.util.ChannelInfo
import net.minecraft.client.Minecraft
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level

class ChannelsPacket : BasePacket {
    
    var channelInfos: MutableList<ChannelInfo> = mutableListOf()
    var channels: MutableList<Channel> = mutableListOf()
    
    constructor(packet: FriendlyByteBuf) {
        val tag = packet.readNbt()
        
        if (tag == null) {
            Logger.warn("Tag was null in ChannelsPacket")
            
            return
        }
        
        val channelInfosTag = tag.getCompound("channelInfos")
        val channelInfosSize = channelInfosTag.getInt("size")
        for (i in 0 until channelInfosSize) {
            val channelInfo = ChannelInfo.readFromNbt(channelInfosTag.getCompound("$i"), Minecraft.getInstance().level as Level)
            channelInfos.add(i, channelInfo)
        }
        
        val channelsTag = tag.getCompound("channels") 
        val channelsSize = channelsTag.getInt("size")
        for (i in 0 until channelsSize) {
            val channel = Channel.loadFromNbt(channelsTag.getCompound("$i"), channelInfos, Minecraft.getInstance().level as Level)
            if (channel != null) {
                channels.add(i, channel)
            }
        }
    }
    
    constructor(channels: List<Channel>, channelInfos: List<ChannelInfo>) {
        
        val data = getInitialData()
        
        val channelsSize = channels.size
        val channelInfoSize = channelInfos.size
        
        val nbt = CompoundTag()
        val channelsTag = CompoundTag()
        channelsTag.putInt("size", channelsSize)
        
        channels.forEachIndexed { index, channel -> 
            val tag = channel.saveToNbt()
            channelsTag.put("$index", tag)
        }
        
        val channelInfoTag = CompoundTag()
        channelInfoTag.putInt("size", channelInfoSize)
        
        channelInfos.forEachIndexed { index, channelInfo -> 
            val tag = channelInfo.saveToNbt()
            channelInfoTag.put("$index", tag)
        }
        
        nbt.put("channels", channelsTag)
        nbt.put("channelInfos", channelInfoTag)
        
        data.writeNbt(nbt)
        
        configureWrite(data)
    }

    override fun clientPacketData(player: Player?) {
        val containerMenu = player!!.containerMenu
        if (containerMenu is MEWirelessTransceiverMenu) {
            containerMenu.receiveChannelData(channelInfos, channels)
        }
    }
}