package com.the9grounds.aeadditions.core.network.packet

import com.the9grounds.aeadditions.core.network.NetworkManager
import com.the9grounds.aeadditions.menu.MEWirelessTransceiverMenu
import io.netty.buffer.Unpooled
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import java.util.function.Supplier

class RequestChannelsPacket : BasePacket {
    constructor(packet: FriendlyByteBuf) {
        
    }
    
    constructor() {}

    override fun encode(buf: FriendlyByteBuf) {}

    override fun apply(contextSupplier: Supplier<dev.architectury.networking.NetworkManager.PacketContext>) {
        val player = contextSupplier.get().player

        //        var containerMenu = player.containerMenu
//
//        if (containerMenu !is MEWirelessTransceiverMenu) {
//            return
//        }
//
//        val level = player.level() as ServerLevel
//
//        val channelHolder = level.getCapability(Capability.CHANNEL_HOLDER).resolve().get()
//
//        val filteredChannels = channelHolder.channels.filter {
//            it.value.hasAccessTo(player as ServerPlayer)
//        }
//        val filteredChannelInfos = channelHolder.channelInfos.filter {
//            it.hasAccessTo(player as ServerPlayer)
//        }
//
//        val packet = ChannelsPacket(filteredChannels.values.toList(), filteredChannelInfos)
//
//        NetworkManager.sendTo(packet, player as ServerPlayer)
//
//        val isASubscriber = containerMenu.blockEntity?.currentChannel?.broadcaster != containerMenu.blockEntity
//
//        val channelInfo = containerMenu.blockEntity?.currentChannel?.channelInfo ?: return
//
//        NetworkManager.sendTo(TransceiverDataChange(isASubscriber, player.level(), channelInfo), player)
    }
}