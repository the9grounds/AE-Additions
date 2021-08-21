package com.the9grounds.aeadditions.network.handler

import com.the9grounds.aeadditions.network.AEAPacketBuffer
import com.the9grounds.aeadditions.network.packets.*
import kotlin.reflect.KClass

abstract class BasePacketHandler {
    companion object {
        val lookup = mutableMapOf<KClass<out BasePacket>, Packets>()
    }
    
    enum class Packets(val clazz: KClass<out BasePacket>, val factory: (AEAPacketBuffer) -> BasePacket) {
        GUIDATASYNC(GuiDataSyncPacket::class, factory = { packetBuffer ->  GuiDataSyncPacket(packetBuffer)}),
        MEINVENTORYUPDATE(MEInventoryUpdatePacket::class, factory = { packetBuffer -> MEInventoryUpdatePacket(packetBuffer) }),
        MEINTERACTION(MEInteractionPacket::class, { packetBuffer -> MEInteractionPacket(packetBuffer) }),
        UPDATESLOTINHAND(UpdateSlotInHandPacket::class, { packetBuffer -> UpdateSlotInHandPacket(packetBuffer) }),
        CHEMICALCONFIG(ChemicalConfigPacket::class, { packetBuffer -> ChemicalConfigPacket(packetBuffer) }),
        CHEMICALCONFIGCHANGED(ChemicalConfigChangedPacket::class, { buffer -> ChemicalConfigChangedPacket(buffer) });
        
        init {
            lookup[clazz] = this
        }
        
        fun parsePacket(packet: AEAPacketBuffer) : BasePacket {
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