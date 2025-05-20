package com.the9grounds.aeadditions.core.network.packet.server

import com.the9grounds.aeadditions.Logger
import com.the9grounds.aeadditions.core.network.NetworkManager
import com.the9grounds.aeadditions.core.network.packet.Packet
import com.the9grounds.aeadditions.core.network.packet.client.ChannelsPacket
import com.the9grounds.aeadditions.core.network.packet.client.TransceiverInfoPacket
import com.the9grounds.aeadditions.menu.MEWirelessTransceiverMenu
import com.the9grounds.aeadditions.util.ChannelInfo
import com.the9grounds.aeadditions.util.Utils
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.neoforged.neoforge.server.ServerLifecycleHooks
import java.util.*

class CreateChannelPacket(val isPrivate: Boolean, val channelName: String, val subscribe: Boolean) : ServerPacket {
    companion object {
        val TYPE = Packet.createType<CreateChannelPacket>("create_channel")

        val STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            CreateChannelPacket::isPrivate,
            ByteBufCodecs.STRING_UTF8,
            CreateChannelPacket::channelName,
            ByteBufCodecs.BOOL,
            CreateChannelPacket::subscribe,
            ::CreateChannelPacket
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> {
        return TYPE
    }

    override fun handle(player: ServerPlayer) {
        val containerMenu = player.containerMenu

        if (containerMenu !is MEWirelessTransceiverMenu) {
            return
        }
        
        val level = player.level() as ServerLevel

        val channelHolder = Utils.getChannelHolderForLevel(level)

        if (channelHolder == null) {
            Logger.warn("Channel Holder was null in CreateChannelPacket")
            return
        }

        val alreadyExists = channelHolder.channelInfos.find { it.name == channelName && ((it.isPrivate && it.hasAccessTo(player as ServerPlayer)) || !it.isPrivate) } !== null

        if (alreadyExists) {
            return
        }
        
        val channelInfo = ChannelInfo(UUID.randomUUID(), channelName, isPrivate, player.uuid, player.name.toString())
        
        channelHolder.channelInfos.add(channelInfo)

        if (subscribe) {
            containerMenu.blockEntity!!.subscribeToChannel(channelInfo)
        } else {
            containerMenu.blockEntity!!.broadcastToChannel(channelInfo)
        }

        val channel = channelHolder.getOrCreateChannel(channelInfo)
        val transceiverDataChange = TransceiverInfoPacket(Optional.of(channelInfo), subscribe, channel.usedChannels, channel.maxChannels)

        val server = ServerLifecycleHooks.getCurrentServer()

        server?.playerList?.players?.forEach {
            val innerContainerMenu = it.containerMenu
            if (innerContainerMenu is MEWirelessTransceiverMenu) {
                if (innerContainerMenu.blockEntity == containerMenu.blockEntity) {
                    NetworkManager.sendTo(transceiverDataChange, it)
                }
                
                if (it.level() == player.level()) {
                    innerContainerMenu.sendChannelsToClient()
                }
            }
        }
    }
}