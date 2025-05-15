package com.the9grounds.aeadditions.core.network

import appeng.core.sync.network.TargetPoint
import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.Server
import com.the9grounds.aeadditions.core.network.packet.*
import dev.architectury.networking.NetworkChannel
import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.RunningOnDifferentThreadException
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import java.util.function.Supplier

object NetworkManager {
    
    var channel: NetworkChannel = NetworkChannel.create(ResourceLocation(AEAdditions.ID, "network_channel"))
    
    init {
        // Server->Client
        channel.register(ChannelsPacket::class.java, ChannelsPacket::encode, ::ChannelsPacket, ChannelsPacket::apply)
        channel.register(TransceiverInfoPacket::class.java, TransceiverInfoPacket::encode, ::TransceiverInfoPacket, TransceiverInfoPacket::apply)

        // Client->Server
        channel.register(CreateChannelPacket::class.java, CreateChannelPacket::encode, ::CreateChannelPacket, CreateChannelPacket::apply)
        channel.register(DeleteChannelPacket::class.java, DeleteChannelPacket::encode, ::DeleteChannelPacket, DeleteChannelPacket::apply)
        channel.register(RequestChannelsPacket::class.java, RequestChannelsPacket::encode, ::RequestChannelsPacket, RequestChannelsPacket::apply)
        channel.register(ChangeTransceiverDataPacket::class.java, ChangeTransceiverDataPacket::encode, ::ChangeTransceiverDataPacket, ChangeTransceiverDataPacket::apply)
    }
    
    fun init() {
        
    }
    
//    fun sendToAllAround(message: BasePacket, point: TargetPoint) {
//        val server = Server.getMinecraftServer()
//
//        if (server != null) {
//            val packet = message.toPacket(NetworkDirection.PLAY_TO_CLIENT)
//            server.playerList.broadcast(point.excluded, point.x, point.y, point.z, point.r2, point.level.dimension(), packet)
//        }
//    }
    
    fun sendTo(message: BasePacket, player: ServerPlayer) {
        channel.sendToPlayer(player, message)
    }
    
    fun sendToServer(message: BasePacket) {
        channel.sendToServer(message)
    }
}