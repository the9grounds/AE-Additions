package com.the9grounds.aeadditions.network.handler

import com.the9grounds.aeadditions.Logger
import com.the9grounds.aeadditions.network.AEAPacketBuffer
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.INetHandler

class ServerPacketHandler : BasePacketHandler(), IPacketHandler {
    override fun onPacketData(handler: INetHandler, packet: AEAPacketBuffer, player: PlayerEntity?) {
        try {
            val packetType = packet.readInt()
            val pack = Packets.getPacket(packetType).parsePacket(packet)
            pack.serverPacketData(player)
        } catch (e: Throwable) {
            Logger.warn(e)
        }
    }
}