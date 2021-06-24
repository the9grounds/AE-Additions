package com.the9grounds.aeadditions.network.packet

import com.the9grounds.aeadditions.tileentity.TileEntityFluidFiller
import net.minecraft.entity.player.EntityPlayerMP

class PacketFluidFillerSyncClient(val tileEntity: TileEntityFluidFiller) : Packet() {
    override fun getPacketId(): PacketId = PacketId.FLUID_FILLER_SYNC_CLIENT

    override fun writeData(data: PacketBufferEC) {
        data.writeTile(tileEntity)
    }

    class HandlerServer : IPacketHandlerServer {
        override fun onPacketData(data: PacketBufferEC, player: EntityPlayerMP?) {
            val tile = data.readTile(player!!.world)

            if (tile is TileEntityFluidFiller) {
                tile.syncClientGui(player)
            }
        }

    }
}