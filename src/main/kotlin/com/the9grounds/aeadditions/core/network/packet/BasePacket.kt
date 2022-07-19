package com.the9grounds.aeadditions.core.network.packet

import com.the9grounds.aeadditions.core.network.NetworkManager
import com.the9grounds.aeadditions.core.network.Packets
import io.netty.buffer.Unpooled
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.entity.player.Player
import net.minecraftforge.network.NetworkDirection
import org.apache.commons.lang3.tuple.Pair

abstract class BasePacket {
    private var packet: FriendlyByteBuf? = null
    
    open fun serverPacketData(player: Player) {
        throw NotImplementedError("Server packet not implemented")
    }

    open fun clientPacketData(player: Player?) {
        throw NotImplementedError("Server packet not implemented")
    }
    
    fun getPacketId(): Int {
        return Packets.Packet.getID(this::class).ordinal
    }
    
    fun getInitialData(): FriendlyByteBuf {
        val data = FriendlyByteBuf(Unpooled.buffer())

        data.writeInt(getPacketId())
        
        return data
    }

    protected fun configureWrite(data: FriendlyByteBuf) {
        data.capacity(data.readableBytes())
        this.packet = data
    }
    
    fun toPacket(direction: NetworkDirection): net.minecraft.network.protocol.Packet<*> {
        return direction.buildPacket<net.minecraft.network.protocol.Packet<*>>(Pair.of(packet, 0), NetworkManager.channel).getThis()
    }
}