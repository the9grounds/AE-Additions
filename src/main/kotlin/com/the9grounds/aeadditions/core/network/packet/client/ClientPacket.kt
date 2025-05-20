package com.the9grounds.aeadditions.core.network.packet.client

import com.the9grounds.aeadditions.core.network.packet.Packet
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.network.handling.IPayloadContext

interface ClientPacket: Packet {
    fun handle(player: Player)

    fun handle(context: IPayloadContext) {
        context.enqueueWork {
            handle(context.player())
        }
    }
}