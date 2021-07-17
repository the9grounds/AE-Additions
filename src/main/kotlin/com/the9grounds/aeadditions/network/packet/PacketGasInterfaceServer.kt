package com.the9grounds.aeadditions.network.packet

import com.the9grounds.aeadditions.tileentity.TileEntityGasInterface
import net.minecraft.entity.player.EntityPlayerMP

class PacketGasInterfaceServer(val tileEntity: TileEntityGasInterface) : Packet() {
    override fun getPacketId(): PacketId = PacketId.GAS_INTERFACE_SERVER

    override fun writeData(data: PacketBufferEC) {
        data.writeTile(tileEntity)
    }

    class HandlerServer : IPacketHandlerServer {
        override fun onPacketData(data: PacketBufferEC, player: EntityPlayerMP) {
            val tile = data.readTile(player.world)

            if (tile is TileEntityGasInterface) {
                tile.syncClientGui(player)
            }
        }

    }
}