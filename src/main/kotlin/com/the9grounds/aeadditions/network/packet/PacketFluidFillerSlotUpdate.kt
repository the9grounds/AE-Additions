package com.the9grounds.aeadditions.network.packet

import com.the9grounds.aeadditions.gui.fluid.GuiFluidFiller
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

class PacketFluidFillerSlotUpdate(val fluid: Fluid?) : Packet() {
    override fun getPacketId(): PacketId = PacketId.FLUID_FILLER_SLOT_UPDATE

    override fun writeData(data: PacketBufferEC) {
        data.writeFluid(fluid)
    }

    companion object {
        @SideOnly(Side.CLIENT)
        class HandlerClient : IPacketHandlerClient {
            override fun onPacketData(data: PacketBufferEC, player: EntityPlayer) {
                val fluid = data.readFluid()

                val gui: Gui? = Minecraft.getMinecraft().currentScreen

                if (gui != null && gui is GuiFluidFiller) {
                    gui.updateSelectedFluid(fluid)
                }
            }
        }
    }
}