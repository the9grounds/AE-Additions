package com.the9grounds.aeadditions.core.network.packet.server

import com.the9grounds.aeadditions.core.network.NetworkManager
import com.the9grounds.aeadditions.core.network.packet.Packet
import com.the9grounds.aeadditions.core.network.packet.client.ChannelsPacket
import com.the9grounds.aeadditions.core.network.packet.client.TransceiverInfoPacket
import com.the9grounds.aeadditions.menu.MEWirelessTransceiverMenu
import com.the9grounds.aeadditions.util.Utils
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import java.util.Optional

class RequestChannelsPacket : ServerPacket {
    companion object {
        val TYPE = Packet.createType<RequestChannelsPacket>("request_channels")

        val STREAM_CODEC = StreamCodec.unit<ByteBuf, RequestChannelsPacket>(RequestChannelsPacket())
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE

    override fun equals(other: Any?): Boolean {
        return true
    }

    override fun hashCode(): Int {
        return 0
    }

    override fun handle(player: ServerPlayer) {
        
        val containerMenu = player.containerMenu
        
        if (containerMenu !is MEWirelessTransceiverMenu) {
            return
        }
        
        val level = player.level() as ServerLevel
        
        val channelHolder = Utils.getChannelHolderForLevel(level)

        if (null === channelHolder) {
            return
        }

        val filteredChannelInfos = channelHolder.channelInfos.filter { 
            it.hasAccessTo(player)
        }
        
        val packet = ChannelsPacket(filteredChannelInfos)
        
        NetworkManager.sendTo(packet, player)
        
        val isASubscriber = containerMenu.blockEntity?.currentChannel?.broadcaster != containerMenu.blockEntity
        
        val channelInfo = containerMenu.blockEntity?.currentChannel?.channelInfo ?: return
        val channel = containerMenu.blockEntity?.currentChannel
        val maxChannels = channel?.maxChannels ?: 0
        val usedChannels = channel?.usedChannels ?: 0
        
        NetworkManager.sendTo(TransceiverInfoPacket(Optional.of(channelInfo), isASubscriber, usedChannels, maxChannels), player)
    }
}