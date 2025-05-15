package com.the9grounds.aeadditions

import dev.architectury.injectables.annotations.ExpectPlatform
import net.minecraft.server.MinecraftServer

object Server {
    @ExpectPlatform
    @JvmStatic
    fun getMinecraftServer(): MinecraftServer {
        throw AssertionError()
    }
}