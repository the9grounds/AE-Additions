package com.the9grounds.aeadditions.core.network.packet.server

import com.the9grounds.aeadditions.Logger
import com.the9grounds.aeadditions.core.network.packet.Packet
import com.the9grounds.aeadditions.menu.MEWirelessTransceiverMenu
import com.the9grounds.aeadditions.util.Utils
import net.minecraft.core.UUIDUtil
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerPlayer
import net.neoforged.neoforge.server.ServerLifecycleHooks
import java.util.*

class DeleteChannelPacket(val id: UUID) : ServerPacket {
    companion object {
        val TYPE = Packet.createType<DeleteChannelPacket>("delete_channel")

        val STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            DeleteChannelPacket::id,
            ::DeleteChannelPacket
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE

    override fun handle(player: ServerPlayer) {
        
        val level = player.level()

        val channelHolder = Utils.getChannelHolderForLevel(level)

        if (channelHolder == null) {
            Logger.error("Channel Holder not found")

            return
        }
        
        val channelInfo = channelHolder.getChannelInfoById(id)
        
        if (channelInfo == null || !channelInfo.hasAccessToDelete(player as ServerPlayer)) {
            return
        }
        
        channelHolder.removeChannel(channelInfo)
        
        val containerMenu = player.containerMenu
        
        if (containerMenu !is MEWirelessTransceiverMenu) {
            return
        }
        
        val server = ServerLifecycleHooks.getCurrentServer()

        server?.playerList?.players?.forEach {
            val innerContainerMenu = it.containerMenu
            if (innerContainerMenu is MEWirelessTransceiverMenu) {
                if (innerContainerMenu.blockEntity!! == containerMenu.blockEntity) {
                    innerContainerMenu.sendTransceiverInfoToClient()
                }

                if (it.level() === level) {
                    innerContainerMenu.sendChannelsToClient()
                }
            }
        }
    }
}