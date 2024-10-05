package com.the9grounds.aeadditions.core.network

import com.the9grounds.aeadditions.Logger
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.entity.player.Player

object ClientPacketHandler : IPacketHandler {
    override fun onPacketData(packet: FriendlyByteBuf, player: Player) {
        try {
            val packetType = packet.readInt()
            val aePacket = Packets.Packet.values()[packetType].parsePacket(packet)
            aePacket.clientPacketData(player)
        } catch (e: Throwable) {
            Logger.warn(e)
        }
    }
}