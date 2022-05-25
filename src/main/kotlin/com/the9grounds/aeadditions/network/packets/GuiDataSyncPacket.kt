package com.the9grounds.aeadditions.network.packets

import com.the9grounds.aeadditions.container.AbstractContainer
import com.the9grounds.aeadditions.network.AEAPacketBuffer
import io.netty.buffer.Unpooled
import net.minecraft.entity.player.PlayerEntity
import java.util.function.Consumer

class GuiDataSyncPacket : BasePacket {
    var windowId: Int = 0
    
    var data: AEAPacketBuffer? = null
    
    constructor(windowId: Int, writer: Consumer<AEAPacketBuffer>) {
        this.windowId = 0
        val data = AEAPacketBuffer(Unpooled.buffer());
        data.writeInt(getPacketId())
        data.writeVarInt(windowId)
        writer.accept(data)
        configureWrite(data)
    }
    
    constructor(data: AEAPacketBuffer) {
        this.windowId = data.readVarInt()
        this.data = AEAPacketBuffer(data.copy())
    }

    override fun clientPacketData(player: PlayerEntity?) {
        val openContainer = player!!.openContainer
        if (openContainer is AbstractContainer<*> && this.windowId == openContainer.windowId) {
            openContainer.receiveServerDataSync(this)
        }
    }

    override fun serverPacketData(player: PlayerEntity?) {
        val openContainer = player!!.openContainer
        if (openContainer is AbstractContainer<*> && this.windowId == openContainer.windowId) {
            openContainer.receiveServerDataSync(this)
        }
    }
}