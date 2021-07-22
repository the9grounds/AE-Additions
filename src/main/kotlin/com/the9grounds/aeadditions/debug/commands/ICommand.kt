package com.the9grounds.aeadditions.debug.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.CommandSource
import net.minecraft.server.MinecraftServer

interface ICommand {
    fun call(server: MinecraftServer, data: CommandContext<CommandSource?>, sender: CommandSource?)
    
    fun addArguments(builder: LiteralArgumentBuilder<CommandSource>) {
        // Default
    }
}