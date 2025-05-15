package com.the9grounds.aeadditions.forge

import net.minecraft.server.MinecraftServer
import net.minecraftforge.server.ServerLifecycleHooks

object ServerImpl {
    @JvmStatic
    fun getMinecraftServer(): MinecraftServer {
        return ServerLifecycleHooks.getCurrentServer()
    }
}