package com.the9grounds.aeadditions.network.packets

import appeng.api.storage.data.IItemList
import appeng.container.me.common.GridInventoryEntry
import appeng.container.me.common.IncrementalUpdateHelper
import appeng.container.me.common.MEMonitorableContainer
import com.the9grounds.aeadditions.Logger
import com.the9grounds.aeadditions.api.gas.IAEChemicalStack
import com.the9grounds.aeadditions.container.chemical.ChemicalTerminalContainer
import com.the9grounds.aeadditions.network.AEAPacketBuffer
import com.the9grounds.aeadditions.network.handler.BasePacketHandler
import io.netty.buffer.Unpooled
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.PacketBuffer
import java.util.function.Consumer
import kotlin.reflect.KFunction1

class MEInventoryUpdatePacket : BasePacket {
    
    private var fullUpdate = false
    
    private var list: MutableList<GridInventoryEntry<IAEChemicalStack>> = mutableListOf()
    
    private var windowId: Int = 0
    private var chemicalStackList: IItemList<IAEChemicalStack>? = null
    
    constructor(packetBuffer: AEAPacketBuffer) {
        windowId = packetBuffer.readVarInt()
        chemicalStackList = packetBuffer.readIAEChemicalStackList()
    }
    
    constructor(windowId: Int, chemicalStackList: IItemList<IAEChemicalStack>) {
        this.windowId = windowId
        this.chemicalStackList = chemicalStackList
        
        val packet = AEAPacketBuffer(Unpooled.buffer())
        
        packet.writeInt(getPacketId())
        packet.writeVarInt(windowId)
        packet.writeIAEChemicalStackList(chemicalStackList)
        configureWrite(packet)
    }

    private fun getContainer(): ChemicalTerminalContainer? {
        // This is slightly dangerous since it accesses the game thread from the network thread,
        // but reading the current container is atomic (reference field), and from then the window id
        // and storage channel are immutable.
        val player = Minecraft.getInstance().player
            ?: // Probably a race-condition when the player already has left the game
            return null
        val currentContainer = player.openContainer as? ChemicalTerminalContainer
            ?: // Ignore a packet for a screen that has already been closed
            return null

        // If the window id matches, this unsafe cast should actually be safe
        val meContainer = currentContainer
        return if (meContainer.windowId == windowId) {
            meContainer
        } else null
    }

    override fun clientPacketData(player: PlayerEntity?) {
        val container = getContainer()
        
        if (container == null) {
            Logger.info("Ignoring ME inventory update packet because the target container isn't open.")
            return
        }
        
        container.chemicalList = chemicalStackList
        
        container.gui?.onFluidListChange()
    }
}