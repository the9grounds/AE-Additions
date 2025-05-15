package com.the9grounds.aeadditions.core.network.packet

import net.minecraft.world.entity.player.Player

interface ServerPacket {
    fun serverPacketData(player: Player)
}