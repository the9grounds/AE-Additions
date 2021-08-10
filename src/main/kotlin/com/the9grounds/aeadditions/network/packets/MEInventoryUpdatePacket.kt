package com.the9grounds.aeadditions.network.packets

import appeng.api.storage.data.IItemList
import appeng.container.me.common.GridInventoryEntry
import appeng.container.me.common.IncrementalUpdateHelper
import appeng.container.me.common.MEMonitorableContainer
import com.the9grounds.aeadditions.Logger
import com.the9grounds.aeadditions.api.gas.IAEChemicalStack
import com.the9grounds.aeadditions.container.chemical.ChemicalTerminalContainer
import com.the9grounds.aeadditions.network.handler.BasePacketHandler
import io.netty.buffer.Unpooled
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.PacketBuffer
import java.util.function.Consumer
import kotlin.reflect.KFunction1

class MEInventoryUpdatePacket : BasePacket {
    
    companion object {
        const val UNCOMPRESSED_PACKET_BYTE_LIMIT = 512 * 1024 * 1024
        
        const val INITIAL_BUFFER_CAPACITY = 2 * 1024
        
        const val ITEM_COUNT_FIELD_OFFSET = 4
    }
    
    private var fullUpdate = false
    
    private var list: MutableList<GridInventoryEntry<IAEChemicalStack>> = mutableListOf()
    
    private var windowId: Int = 0
    
    constructor(packetBuffer: PacketBuffer) {
        val itemCount = packetBuffer.readShort()
        windowId = packetBuffer.readVarInt()
        fullUpdate = packetBuffer.readBoolean()
        
        val container = getContainer()
        
        if (container != null) {
            val storageChannel = container.storageChannel
            
            for (i in 0 until itemCount) {
                list.add(GridInventoryEntry.read(storageChannel, packetBuffer))
            }
        }
    }
    
    private constructor() {
        
    }

    private fun getContainer(): ChemicalTerminalContainer? {
        // This is slightly dangerous since it accesses the game thread from the network thread,
        // but reading the current container is atomic (reference field), and from then the window id
        // and storage channel are immutable.
        val player = Minecraft.getInstance().player
            ?: // Probably a race-condition when the player already has left the game
            return null
        val currentContainer = player.openContainer as? MEMonitorableContainer<*>
            ?: // Ignore a packet for a screen that has already been closed
            return null

        // If the window id matches, this unsafe cast should actually be safe
        val meContainer = currentContainer
        return if (meContainer.windowId == windowId) {
            meContainer as ChemicalTerminalContainer
        } else null
    }
    
    class Builder(private val windowId: Int, private val fullUpdate: Boolean) {
        private val packets: MutableList<MEInventoryUpdatePacket> = mutableListOf()

        private var data: PacketBuffer? = null

        private var itemCount = 0
        
        init {
            data = if (fullUpdate) {
                createPacketHeader(fullUpdate)
            } else {
                null
            }
        }

        fun addFull(
            updateHelper: IncrementalUpdateHelper<IAEChemicalStack>,
            stacks: IItemList<IAEChemicalStack>
        ) {
            for (item in stacks) {
                val serial = updateHelper.getOrAssignSerial(item)
                add(
                    GridInventoryEntry<IAEChemicalStack>(
                        serial, item, item.getStackSize(), item.getCountRequestable(),
                        item.isCraftable()
                    )
                )
            }
        }

        fun addChanges(updateHelper: IncrementalUpdateHelper<IAEChemicalStack>, stacks: IItemList<IAEChemicalStack>) {
            for (key in updateHelper) {
                var sendKey: IAEChemicalStack?
                var serial = updateHelper.getSerial(key)

                // Try to serialize the item into the buffer
                if (serial == null) {
                    // This is a new key, not sent to the client
                    sendKey = key
                    serial = updateHelper.getOrAssignSerial(key)
                } else {
                    // This is an incremental update referring back to the serial
                    sendKey = null
                }

                // The queued changes are actual differences, but we need to send the real stored properties
                // to the client.
                val stored: IAEChemicalStack? = stacks.findPrecise(key)
                if (stored == null || !stored.isMeaningful()) {
                    // This happens when an update is queued but the item is no longer stored
                    add(GridInventoryEntry(serial, sendKey, 0, 0, false))
                    key.reset() // Ensure it is deleted on commit, since the client will also clear it
                } else {
                    add(
                        GridInventoryEntry(
                            serial, sendKey, stored.getStackSize(), stored.getCountRequestable(),
                            stored.isCraftable()
                        )
                    )
                }
            }
            updateHelper.commitChanges()
        }


        private fun ensureData(): PacketBuffer {
            if (data == null) {
                data = createPacketHeader(false)
            }
            return data!!
        }
        
        fun add(entry: GridInventoryEntry<IAEChemicalStack>) {
            val data: PacketBuffer = ensureData()

            // This should only error out if the entire packet exceeds about 2 megabytes of memory,
            // if any item writes that much junk to a share tag, it's acceptable to crash.
            // We'll normaly flush much much earlier (32k)
            entry.write(data)
            ++itemCount
            if (data.writerIndex() >= UNCOMPRESSED_PACKET_BYTE_LIMIT || itemCount >= Short.MAX_VALUE) {
                flushData()
            }
        }

        private fun flushData() {
            if (data != null) {
                // Jump back and fill in the number of items contained in the packet
                data!!.markWriterIndex()
                data!!.writerIndex(ITEM_COUNT_FIELD_OFFSET)
                data!!.writeShort(itemCount)
                data!!.resetWriterIndex()

                // Build a packet and queue it
                val packet: MEInventoryUpdatePacket =
                    MEInventoryUpdatePacket()
                packet.configureWrite(data!!)
                packets.add(packet)

                // Reset
                data = null
                itemCount = 0
            }
        }
        
        private fun createPacketHeader(fullUpdate: Boolean): PacketBuffer {
            val data = PacketBuffer(Unpooled.buffer(INITIAL_BUFFER_CAPACITY))
            data.writeInt(BasePacketHandler.Packets.MEINVENTORYUPDATE.ordinal)
            require(data.writerIndex() == ITEM_COUNT_FIELD_OFFSET)
            data.writeShort(0)
            data.writeVarInt(windowId)
            data.writeBoolean(fullUpdate)
            
            return data
        }

        fun build(): List<MEInventoryUpdatePacket> {
            flushData()
            return packets
        }

        fun buildAndSend(sender: (MEInventoryUpdatePacket) -> Unit) {
            for (packet in build()) {
                sender(packet)
            }
        }
    }

    override fun clientPacketData(player: PlayerEntity?) {
        val container = getContainer()
        
        if (container == null) {
            Logger.info("Ignoring ME inventory update packet because the target container isn't open.")
            return
        }
        
        val clientRepo = container.clientRepo
        
        if (clientRepo == null) {
            Logger.info("Ignoring ME inventory update packet because no client repo is available.")
            return
        }
        
        clientRepo.handleUpdate(fullUpdate, list)
    }
}