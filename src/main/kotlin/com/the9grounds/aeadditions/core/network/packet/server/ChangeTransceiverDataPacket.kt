package com.the9grounds.aeadditions.core.network.packet.server

import com.the9grounds.aeadditions.Logger
import com.the9grounds.aeadditions.core.network.NetworkManager
import com.the9grounds.aeadditions.core.network.packet.Packet
import com.the9grounds.aeadditions.core.network.packet.client.TransceiverInfoPacket
import com.the9grounds.aeadditions.menu.MEWirelessTransceiverMenu
import com.the9grounds.aeadditions.util.ChannelInfo
import com.the9grounds.aeadditions.util.Utils
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.Level
import net.neoforged.neoforge.server.ServerLifecycleHooks
import java.util.*

class ChangeTransceiverDataPacket(val subscribe: Boolean, val channelInfo: ChannelInfo):
    ServerPacket {

    companion object {
        val TYPE = Packet.createType<ChangeTransceiverDataPacket>("change_transceiver_data")

        val STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            ChangeTransceiverDataPacket::subscribe,
            ChannelInfo.STREAM_CODEC,
            ChangeTransceiverDataPacket::channelInfo,
            ::ChangeTransceiverDataPacket
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE

    override fun handle(player: ServerPlayer) {
        val containerMenu = player.containerMenu

        if (containerMenu !is MEWirelessTransceiverMenu) {
            return
        }

        val blockEntity = containerMenu.blockEntity!!

        var channelHolder = Utils.getChannelHolderForLevel(player.level())

        if (channelHolder == null) {
            Logger.error("Channel holder null in change transceiver data packet")

            return
        }

        if (!channelHolder.channelInfos.contains(channelInfo)) {
            return
        }

        // Verify has access to channel

        if (!channelInfo.hasAccessTo(player)) {
            return
        }

        if (subscribe) {
            blockEntity.subscribeToChannel(channelInfo)
        } else {
            blockEntity.broadcastToChannel(channelInfo)
        }

        val channel = channelHolder.getOrCreateChannel(channelInfo)

        val packet = TransceiverInfoPacket(Optional.of(channelInfo), subscribe, channel.usedChannels, channel.maxChannels)

        val server = ServerLifecycleHooks.getCurrentServer()

        server?.playerList?.players?.forEach {
            val containerMenu = it.containerMenu
            if (containerMenu is MEWirelessTransceiverMenu && containerMenu.blockEntity!! == blockEntity) {
                NetworkManager.sendTo(packet, it)
            }
        }
    }
}