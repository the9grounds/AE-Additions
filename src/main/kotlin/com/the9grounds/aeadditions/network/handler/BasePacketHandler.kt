package com.the9grounds.aeadditions.network.handler

import com.the9grounds.aeadditions.network.packets.BasePacket
import com.the9grounds.aeadditions.network.packets.GuiDataSyncPacket
import com.the9grounds.aeadditions.network.packets.MEInteractionPacket
import com.the9grounds.aeadditions.network.packets.MEInventoryUpdatePacket
import net.minecraft.network.PacketBuffer
import kotlin.reflect.KClass

abstract class BasePacketHandler {
    companion object {
        val lookup = mutableMapOf<KClass<out BasePacket>, Packets>()
    }
    
    enum class Packets(val clazz: KClass<out BasePacket>, val factory: (PacketBuffer) -> BasePacket) {
        GUIDATASYNC(GuiDataSyncPacket::class, factory = { packetBuffer ->  GuiDataSyncPacket(packetBuffer)}),
        MEINVENTORYUPDATE(MEInventoryUpdatePacket::class, factory = { packetBuffer -> MEInventoryUpdatePacket(packetBuffer) }),
        MEINTERACTION(MEInteractionPacket::class, { packetBuffer -> MEInteractionPacket(packetBuffer) });
        
        init {
            lookup[clazz] = this
        }
        
        fun parsePacket(packet: PacketBuffer) : BasePacket {
            return factory(packet)
        }
        
        companion object {
            fun getPacket(id: Int): Packets {
                return values()[id]
            }
            
            fun getId(clazz: KClass<out BasePacket>) : Packets? {
                return lookup[clazz]
            }
        }
    }
}