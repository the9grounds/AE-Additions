package com.the9grounds.aeadditions.debug.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.server.MinecraftServer

interface ICommand {
    fun call(server: MinecraftServer, data: CommandContext<CommandSourceStack?>, sender: CommandSourceStack?)

    fun addArguments(builder: LiteralArgumentBuilder<CommandSourceStack>) {
        // Default
    }
}