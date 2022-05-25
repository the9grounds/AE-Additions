package com.the9grounds.aeadditions.network.handler

import com.the9grounds.aeadditions.network.AEAPacketBuffer
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.INetHandler

interface IPacketHandler {
    fun onPacketData(handler: INetHandler, packet: AEAPacketBuffer, player: PlayerEntity?);
}