package com.the9grounds.aeadditions.debug.commands

import com.mojang.brigadier.context.CommandContext
import com.the9grounds.aeadditions.blockentity.MEWirelessTransceiverBlockEntity
import com.the9grounds.aeadditions.registries.Capability
import net.minecraft.commands.CommandSourceStack
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer

object SubscribeToChannel : ICommand {
    override fun call(server: MinecraftServer, data: CommandContext<CommandSourceStack?>, sender: CommandSourceStack?) {
        val entity = sender!!.entity
        
        if (entity is ServerPlayer) {
            for (blockEntity in entity.level.getChunkAt(entity.onPos).blockEntities) {
                val value = blockEntity.value
                if (value is MEWirelessTransceiverBlockEntity) {
                    val currentLevel = entity.level
                    val channel = currentLevel.getCapability(Capability.CHANNEL_HOLDER).resolve().get().getChannelByName("test")
                    if (channel != null) {
                        value.subscribeToChannel(channel)
                        return
                    }
                }
            }
        }
    }
}