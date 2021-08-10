package com.the9grounds.aeadditions.network.packets

import io.netty.buffer.Unpooled
import net.minecraft.network.PacketBuffer
import java.util.function.Consumer

class GuiDataSyncPacket : BasePacket {
    var windowId: Int = 0
    
    var data: PacketBuffer? = null
    
    constructor(windowId: Int, writer: Consumer<PacketBuffer>) {
        this.windowId = 0
        val data = PacketBuffer(Unpooled.buffer());
        data.writeInt(getPacketId())
        data.writeVarInt(windowId)
        writer.accept(data)
        configureWrite(data)
    }
    
    constructor(data: PacketBuffer) {
        this.windowId = data.readVarInt()
        this.data = PacketBuffer(data.copy())
    }
}