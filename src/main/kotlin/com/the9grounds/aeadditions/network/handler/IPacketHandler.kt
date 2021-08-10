package com.the9grounds.aeadditions.network.handler

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.INetHandler
import net.minecraft.network.PacketBuffer

interface IPacketHandler {
    fun onPacketData(handler: INetHandler, packet: PacketBuffer, player: PlayerEntity?);
}