package com.the9grounds.aeadditions.core.network

import com.the9grounds.aeadditions.core.network.packet.client.ChannelsPacket
import com.the9grounds.aeadditions.core.network.packet.client.ClientPacket
import com.the9grounds.aeadditions.core.network.packet.client.TransceiverInfoPacket
import com.the9grounds.aeadditions.core.network.packet.server.*
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerPlayer
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import net.neoforged.neoforge.network.registration.PayloadRegistrar

object NetworkManager {
    @SubscribeEvent
    fun registerPayloadHandlers(event: RegisterPayloadHandlersEvent) {
        val registrar = event.registrar("1")

        clientBound(registrar, ChannelsPacket.TYPE, ChannelsPacket.STREAM_CODEC)
        clientBound(registrar, TransceiverInfoPacket.TYPE, TransceiverInfoPacket.STREAM_CODEC)

        serverBound(registrar, ChangeTransceiverDataPacket.TYPE, ChangeTransceiverDataPacket.STREAM_CODEC)
        serverBound(registrar, CreateChannelPacket.TYPE, CreateChannelPacket.STREAM_CODEC)
        serverBound(registrar, DeleteChannelPacket.TYPE, DeleteChannelPacket.STREAM_CODEC)
        serverBound(registrar, RequestChannelsPacket.TYPE, RequestChannelsPacket.STREAM_CODEC)
    }

    fun <T: ClientPacket> clientBound(registrar: PayloadRegistrar, type: CustomPacketPayload.Type<T>, codec: StreamCodec<ByteBuf, T>) {
        registrar.playToClient(type, codec, ClientPacket::handle)
    }

    fun <T: ServerPacket> serverBound(registrar: PayloadRegistrar, type: CustomPacketPayload.Type<T>, codec: StreamCodec<ByteBuf, T>) {
        registrar.playToServer(type, codec, ServerPacket::handle)
    }
    
    fun sendTo(message: ClientPacket, player: ServerPlayer) {
        PacketDistributor.sendToPlayer(player, message)
    }
    
    fun sendToServer(message: ServerPacket) {
        PacketDistributor.sendToServer(message)
    }

    fun sendToAllPlayers(message: ClientPacket) {
        PacketDistributor.sendToAllPlayers(message)
    }
}