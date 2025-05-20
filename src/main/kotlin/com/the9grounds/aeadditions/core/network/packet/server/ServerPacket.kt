package com.the9grounds.aeadditions.core.network.packet.server

import com.the9grounds.aeadditions.core.network.packet.Packet
import net.minecraft.server.level.ServerPlayer
import net.neoforged.neoforge.network.handling.IPayloadContext

interface ServerPacket: Packet {
    fun handle(player: ServerPlayer)

    fun handle(context: IPayloadContext) {
        context.enqueueWork {
            if (context.player() is ServerPlayer) {
                handle(context.player() as ServerPlayer)
            }
        }
    }
}