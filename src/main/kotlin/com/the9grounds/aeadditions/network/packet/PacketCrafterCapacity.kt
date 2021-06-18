package com.the9grounds.aeadditions.network.packet

import com.the9grounds.aeadditions.gui.fluid.GuiFluidCrafter
import com.the9grounds.aeadditions.gui.gas.GuiBusGasIO
import com.the9grounds.aeadditions.tileentity.TileEntityFluidCrafter
import com.the9grounds.aeadditions.util.GuiUtil
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

class PacketCrafterCapacity(val tileEntity: TileEntity, val capacity: Int) : Packet() {
    override fun getPacketId(): PacketId = PacketId.FLUID_CRAFTER_CAPACITY

    override fun writeData(data: PacketBufferEC) {
        data.writeTile(tileEntity)
        data.writeInt(capacity)
    }

    companion object {
        @SideOnly(Side.CLIENT)
        class HandlerClient : IPacketHandlerClient {
            override fun onPacketData(data: PacketBufferEC, player: EntityPlayer) {
                val tile = data.readTile(player.world) as TileEntityFluidCrafter
                val capacity = data.readInt()

                tile.capacity = capacity

                val gui = GuiUtil.getGui(GuiFluidCrafter::class.java)
                    ?: return

                gui.onCapacityChanged()
            }

        }
    }
}