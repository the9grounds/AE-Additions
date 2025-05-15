package com.the9grounds.aeadditions.core.network.packet

import com.the9grounds.aeadditions.core.network.NetworkManager
import com.the9grounds.aeadditions.core.network.Packets
import dev.architectury.networking.NetworkManager.PacketContext
import io.netty.buffer.Unpooled
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.entity.player.Player
import org.apache.commons.lang3.tuple.Pair
import java.util.function.Supplier

abstract class BasePacket {
    abstract fun encode(buf: FriendlyByteBuf)

    abstract fun apply(contextSupplier: Supplier<PacketContext>)
}