package com.the9grounds.aeadditions.core.network.packet

import com.the9grounds.aeadditions.core.network.NetworkManager
import com.the9grounds.aeadditions.menu.MEWirelessTransceiverMenu
import com.the9grounds.aeadditions.registries.Capability
import io.netty.buffer.Unpooled
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player

class RequestChannelsPacket : BasePacket {
    constructor(packet: FriendlyByteBuf) {
        
    }
    
    constructor() {
        val data = FriendlyByteBuf(Unpooled.buffer())
        data.writeInt(getPacketId())
        configureWrite(data)
    }

    override fun serverPacketData(player: Player) {
        
        var containerMenu = player.containerMenu
        
        if (containerMenu !is MEWirelessTransceiverMenu) {
            return
        }
        
        val level = player.level as ServerLevel
        
        val channelHolder = level.getCapability(Capability.CHANNEL_HOLDER).resolve().get()
        
        val filteredChannels = channelHolder.channels.filter { 
            it.value.hasAccessTo(player as ServerPlayer)
        }
        val filteredChannelInfos = channelHolder.channelInfos.filter { 
            it.hasAccessTo(player as ServerPlayer)
        }
        
        val packet = ChannelsPacket(filteredChannels.values.toList(), filteredChannelInfos)
        
        NetworkManager.sendTo(packet, player as ServerPlayer)
        
        val isASubscriber = containerMenu.blockEntity?.currentChannel?.broadcaster != containerMenu.blockEntity
        
        val channelInfo = containerMenu.blockEntity?.currentChannel?.channelInfo ?: return
        
        NetworkManager.sendTo(TransceiverDataChange(isASubscriber, player.level, channelInfo), player)
    }
}