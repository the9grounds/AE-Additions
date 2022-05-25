package com.the9grounds.aeadditions.debug

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.the9grounds.aeadditions.Logger
import net.minecraft.command.CommandSource

import net.minecraft.command.Commands.literal
import net.minecraftforge.fml.server.ServerLifecycleHooks
import java.util.*


object AEACommandRegistry {
    fun register(dispatcher: CommandDispatcher<CommandSource>) {
        val builder = literal("aea")
        
        for (command in Commands.values()) {
            Logger.info("Registering command ${command.name.toLowerCase()}")
            add(builder, command)
        }
        
        dispatcher.register(builder)
    }

    private fun add(builder: LiteralArgumentBuilder<CommandSource?>, subCommand: Commands) {
        val subCommandBuilder = literal(subCommand.name.toLowerCase(Locale.ROOT))
            .requires { src: CommandSource -> src.hasPermissionLevel(subCommand.level) }
        subCommand.command.addArguments(subCommandBuilder)
        subCommandBuilder.executes { ctx: CommandContext<CommandSource?> ->
            subCommand.command.call(ServerLifecycleHooks.getCurrentServer(), ctx, ctx.source)
            1
        }
        builder.then(subCommandBuilder)
    }
}