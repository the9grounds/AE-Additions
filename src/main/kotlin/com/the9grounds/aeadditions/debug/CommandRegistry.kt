package com.the9grounds.aeadditions.debug

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.the9grounds.aeadditions.Logger
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands.literal
import net.minecraftforge.server.ServerLifecycleHooks
import java.util.*

object CommandRegistry {
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        val builder = literal("aea")

        for (command in Commands.values()) {
            Logger.info("Registering command ${command.name.toLowerCase()}")
            add(builder, command)
        }

        dispatcher.register(builder)
    }

    private fun add(builder: LiteralArgumentBuilder<CommandSourceStack?>, subCommand: Commands) {
        val subCommandBuilder = literal(subCommand.name.toLowerCase(Locale.ROOT))
            .requires { src: CommandSourceStack -> src.hasPermission(subCommand.level) }
        subCommand.command.addArguments(subCommandBuilder)
        subCommandBuilder.executes { ctx: CommandContext<CommandSourceStack?> ->
            subCommand.command.call(ServerLifecycleHooks.getCurrentServer(), ctx, ctx.source)
            1
        }
        builder.then(subCommandBuilder)
    }
}