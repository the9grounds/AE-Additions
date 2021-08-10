package com.the9grounds.aeadditions.network.packets

import appeng.container.me.common.IMEInteractionHandler
import appeng.helpers.InventoryAction
import com.the9grounds.aeadditions.container.chemical.ChemicalTerminalContainer
import io.netty.buffer.Unpooled
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.PacketBuffer

class MEInteractionPacket : BasePacket {

    private var windowId: Int = 0
    private var serial: Long = 0L
    private var action: InventoryAction? = null
    
    constructor(packetBuffer: PacketBuffer) {
        windowId = packetBuffer.readInt()
        serial = packetBuffer.readVarLong()
        action = packetBuffer.readEnumValue(InventoryAction::class.java)
    }
    
    constructor(windowId: Int, serial: Long, action: InventoryAction) {
        this.windowId = windowId
        this.serial = serial
        this.action = action
        
        val packet = PacketBuffer(Unpooled.buffer())
        
        packet.writeInt(getPacketId())
        packet.writeInt(windowId)
        packet.writeVarLong(serial)
        packet.writeEnumValue(action)
        configureWrite(packet)
    }

    override fun serverClientData(player: PlayerEntity?) {
        val container = player!!.openContainer
        if (container is IMEInteractionHandler) {
            if (container.windowId != windowId) {
                return
            }
            
            container.handleInteraction(serial, action)
        }
    }
}