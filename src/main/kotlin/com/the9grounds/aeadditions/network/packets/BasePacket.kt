package com.the9grounds.aeadditions.network.packets

import com.the9grounds.aeadditions.network.NetworkManager
import com.the9grounds.aeadditions.network.handler.BasePacketHandler
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.IPacket
import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.network.NetworkDirection
import org.apache.commons.lang3.tuple.Pair

abstract class BasePacket {
    var packet: PacketBuffer? = null
    
    companion object {
        const val MAX_STRING_LENGTH = 32767
    }
    
    fun getPacketId(): Int {
        return BasePacketHandler.Packets.getId(this::class)!!.ordinal
    }
    
    open fun serverPacketData(player: PlayerEntity?) {
        throw UnsupportedOperationException(
            "This packet ( " + this.getPacketId() + " does not implement a server side handler."
        )
    }
    
    open fun clientPacketData(player: PlayerEntity?) {
        throw UnsupportedOperationException(
            "This packet ( " + this.getPacketId() + " does not implement a server side handler."
        )
    }
    
    protected fun configureWrite(data: PacketBuffer) {
        data.capacity(data.readableBytes())
        packet = data
    }
    
    fun toPacket(direction: NetworkDirection): IPacket<*> {
        if (this.packet!!.array().size > 2 * 1024 * 1024) {
            // TODO: Give better error
            throw IllegalArgumentException("We fudged")
        }
        
        // AE Has packet log, not a bad idea for dev?
        
        return direction.buildPacket<IPacket<*>>(Pair.of(packet!!, 0), NetworkManager.channel).`this`
    }
}