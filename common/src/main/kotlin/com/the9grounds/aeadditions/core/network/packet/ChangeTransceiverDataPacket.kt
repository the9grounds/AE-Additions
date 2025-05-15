package com.the9grounds.aeadditions.core.network.packet

import com.the9grounds.aeadditions.Server
import com.the9grounds.aeadditions.core.network.NetworkManager
import com.the9grounds.aeadditions.menu.MEWirelessTransceiverMenu
import com.the9grounds.aeadditions.util.ChannelInfo
import net.minecraft.client.Minecraft
import net.minecraft.core.registries.Registries
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import java.util.function.Supplier

class ChangeTransceiverDataPacket: BasePacket {
    
    var currentChannel : ChannelInfo
    var dimensionLocation: ResourceLocation
    var subscribe = false
    
    constructor(packet: FriendlyByteBuf) {
        dimensionLocation = packet.readResourceLocation()
        subscribe = packet.readBoolean()
        val nbt = packet.readNbt()
        val level = Server.getMinecraftServer().getLevel(ResourceKey.create(Registries.DIMENSION, dimensionLocation))
        currentChannel = ChannelInfo.readFromNbt(nbt!!, level!!)
    }
    
    constructor(subscribe: Boolean, level: Level, channelInfo: ChannelInfo) {
        
        this.subscribe = subscribe
        this.dimensionLocation = level.dimension().location()
        this.currentChannel = channelInfo
    }

    override fun encode(buf: FriendlyByteBuf) {
        buf.writeResourceLocation(this.dimensionLocation)
        buf.writeBoolean(subscribe)
        buf.writeNbt(currentChannel.saveToNbt())
    }

    override fun apply(contextSupplier: Supplier<dev.architectury.networking.NetworkManager.PacketContext>) {
        val player = contextSupplier.get().player
        val containerMenu = player.containerMenu

        if (containerMenu !is MEWirelessTransceiverMenu) {
            return
        }

        val blockEntity = containerMenu.blockEntity!!

//        var channelHolder = player.level().getCapability(Capability.CHANNEL_HOLDER).resolve().get()
//
//        if (!channelHolder.channelInfos.contains(currentChannel!!)) {
//            return
//        }
//
//        // Verify has access to channel
//
//        if (!currentChannel!!.hasAccessTo(player as ServerPlayer)) {
//            return
//        }
//
//        if (subscribe) {
//            blockEntity.subscribeToChannel(currentChannel!!)
//        } else {
//            blockEntity.broadcastToChannel(currentChannel!!)
//        }
//
//        val packet = TransceiverInfoPacket(subscribe, player.level(), currentChannel!!)
//
//        val server = Server.getMinecraftServer()
//
//        server?.playerList?.players?.forEach {
//            val containerMenu = it.containerMenu
//            if (containerMenu is MEWirelessTransceiverMenu && containerMenu.blockEntity!! == blockEntity) {
//                NetworkManager.sendTo(packet, it)
//            }
//        }
    }
}