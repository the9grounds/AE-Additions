package com.the9grounds.aeadditions.core.network.packet

import com.the9grounds.aeadditions.menu.MEWirelessTransceiverMenu
import com.the9grounds.aeadditions.util.ChannelInfo
import dev.architectury.networking.NetworkManager
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import java.util.*
import java.util.function.Supplier

class DeleteChannelPacket : BasePacket {
    var id: UUID
    constructor(packet: FriendlyByteBuf) {
        this.id = packet.readUUID()
    }
    
    constructor(channelInfo: ChannelInfo) {
        this.id = channelInfo.id
    }

    override fun encode(buf: FriendlyByteBuf) {
        buf.writeUUID(this.id)
    }

    override fun apply(contextSupplier: Supplier<NetworkManager.PacketContext>) {
        val player = contextSupplier.get().player as ServerPlayer

//        val level = player.level()
//
//        val channelHolder = level.getCapability(Capability.CHANNEL_HOLDER).resolve().get()
//
//        val channelInfo = channelHolder.getChannelById(UUID.fromString(id))
//
//        if (channelInfo == null || !channelInfo.hasAccessToDelete(player as ServerPlayer)) {
//            return
//        }
//
//        channelHolder.removeChannel(channelInfo)
//
//        val containerMenu = player.containerMenu
//
//        if (containerMenu !is MEWirelessTransceiverMenu) {
//            return
//        }
//
//        val server = ServerLifecycleHooks.getCurrentServer()
//
//        server?.playerList?.players?.forEach {
//            val innerContainerMenu = it.containerMenu
//            if (innerContainerMenu is MEWirelessTransceiverMenu && innerContainerMenu.blockEntity!! == containerMenu.blockEntity) {
//                innerContainerMenu.sendChannelStuffToClient()
//            }
//        }
    }
}