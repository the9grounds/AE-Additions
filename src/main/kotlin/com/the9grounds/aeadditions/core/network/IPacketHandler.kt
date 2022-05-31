package com.the9grounds.aeadditions.core.network

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.PacketListener
import net.minecraft.world.entity.player.Player

interface IPacketHandler {
    fun onPacketData(handler: PacketListener, packet: FriendlyByteBuf, player: Player?);
}