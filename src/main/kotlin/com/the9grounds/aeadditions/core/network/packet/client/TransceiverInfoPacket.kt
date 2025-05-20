package com.the9grounds.aeadditions.core.network.packet.client

import com.the9grounds.aeadditions.core.network.packet.Packet
import com.the9grounds.aeadditions.menu.MEWirelessTransceiverMenu
import com.the9grounds.aeadditions.util.ChannelInfo
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.world.entity.player.Player
import java.util.*
import kotlin.jvm.optionals.getOrNull

class TransceiverInfoPacket(val currentChannel: Optional<ChannelInfo>, val subscribe: Boolean, val channelUsage: Int = 0, val maxChannels: Int = 0) :
    ClientPacket {

    companion object {
        val TYPE = Packet.createType<TransceiverInfoPacket>("transceiver_info")

        val STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.optional(ChannelInfo.STREAM_CODEC),
            TransceiverInfoPacket::currentChannel,
            ByteBufCodecs.BOOL,
            TransceiverInfoPacket::subscribe,
            ByteBufCodecs.INT,
            TransceiverInfoPacket::channelUsage,
            ByteBufCodecs.INT,
            TransceiverInfoPacket::maxChannels,
            ::TransceiverInfoPacket
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE

    override fun handle(player: Player) {
        val containerMenu = player.containerMenu
        if (containerMenu is MEWirelessTransceiverMenu) {
            containerMenu.isCurrentlySubscribing = subscribe

            if (!containerMenu.isCurrentlySubscribing) {
                containerMenu.isSubscriber = false
                containerMenu.screen?.typeButton?.isSubscriber = false
            }

            containerMenu.currentChannel = this.currentChannel.getOrNull()
            containerMenu.isOnChannel = currentChannel.isPresent
            containerMenu.ae2ChannelUsage = channelUsage
            containerMenu.ae2MaxChannels = maxChannels

            if (this.currentChannel.getOrNull()?.isPrivate == true) {
                containerMenu.isPrivate = true
                containerMenu.screen?.accessBtn?.isPrivate = true
                containerMenu.screen?.onChannelListChanged()
            }
        }
    }
}