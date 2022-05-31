package com.the9grounds.aeadditions.core.network

import com.the9grounds.aeadditions.core.network.packet.BasePacket
import com.the9grounds.aeadditions.core.network.packet.ChannelsPacket
import com.the9grounds.aeadditions.core.network.packet.RequestChannelsPacket
import net.minecraft.network.FriendlyByteBuf
import kotlin.reflect.KClass

object Packets {
    val packetLookupMap: MutableMap<KClass<*>, Packet> = mutableMapOf()
    
    enum class Packet(val clazz: KClass<*>, val factory: (FriendlyByteBuf) -> BasePacket) {
        REQUESTCHANNELS(RequestChannelsPacket::class, ::RequestChannelsPacket),
        CHANNELS(ChannelsPacket::class, ::ChannelsPacket);

        companion object {

            fun getID(givenClazz: KClass<*>): Packet {
                return packetLookupMap.get(givenClazz)!!
            }
        }

        init {
            packetLookupMap.put(clazz, this)
        }

        fun parsePacket(inboundPacket: FriendlyByteBuf): BasePacket {
            return factory(inboundPacket)
        }
    }
}