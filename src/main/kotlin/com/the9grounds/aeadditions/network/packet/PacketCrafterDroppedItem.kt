package com.the9grounds.aeadditions.network.packet

import com.the9grounds.aeadditions.tileentity.TileEntityFluidCrafter
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

class PacketCrafterDroppedItem(val tileEntityFluidCrafter: TileEntityFluidCrafter, val slot: Int) : Packet() {
    override fun getPacketId(): PacketId = PacketId.FLUID_CRAFTER_DROPPED_ITEM

    override fun writeData(data: PacketBufferEC) {
        data.writeTile(tileEntityFluidCrafter)
        data.writeInt(slot)
    }

    companion object {
        @SideOnly(Side.CLIENT)
        class HandlerClient: IPacketHandlerClient {
            override fun onPacketData(data: PacketBufferEC, player: EntityPlayer) {
                val tile = data.readTile(player.world) as TileEntityFluidCrafter
                val slot = data.readInt()

                tile.removeSlot(slot)
            }
        }
    }
}