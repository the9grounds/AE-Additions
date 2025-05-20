package com.the9grounds.aeadditions.core.network.packet.client

import com.the9grounds.aeadditions.core.network.packet.Packet
import com.the9grounds.aeadditions.menu.MEWirelessTransceiverMenu
import com.the9grounds.aeadditions.util.ChannelInfo
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.world.entity.player.Player

class ChannelsPacket(val channelInfos: List<ChannelInfo>) : ClientPacket {

    companion object {
        val TYPE = Packet.createType<ChannelsPacket>("channels")

        val STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.collection(
                ::ArrayList,
                ChannelInfo.STREAM_CODEC
            ),
            ChannelsPacket::channelInfos,
            ::ChannelsPacket
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE

    override fun handle(player: Player) {
        val containerMenu = player.containerMenu
        if (containerMenu is MEWirelessTransceiverMenu) {
            containerMenu.receiveChannelData(channelInfos)
        }
    }
}