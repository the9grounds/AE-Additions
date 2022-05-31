package com.the9grounds.aeadditions.debug.commands

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.the9grounds.aeadditions.registries.Capability
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer

object TestChannelCreate : ICommand {
    override fun call(server: MinecraftServer, data: CommandContext<CommandSourceStack?>, sender: CommandSourceStack?) {
        
    }

    override fun addArguments(builder: LiteralArgumentBuilder<CommandSourceStack>) {
        builder.then(Commands.argument("name", StringArgumentType.string()).executes {
            val source = it.source
            val name = StringArgumentType.getString(it, "name")
            
            if (source.entity is ServerPlayer) {
                val currentLevel = source!!.entity!!.getLevel()
                val channelHolder = currentLevel.getCapability(Capability.CHANNEL_HOLDER).resolve().get()
                channelHolder.setupTestChannels(source.entity as ServerPlayer, name)
            }
            
            1
        })
    }
}