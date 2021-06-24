package com.the9grounds.aeadditions.network.packet.part;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;

import net.minecraft.fluid.Fluid;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.the9grounds.aeadditions.gui.GuiTerminal;
import com.the9grounds.aeadditions.network.packet.IPacketHandlerClient;
import com.the9grounds.aeadditions.network.packet.Packet;
import com.the9grounds.aeadditions.network.packet.PacketBufferEC;
import com.the9grounds.aeadditions.network.packet.PacketId;
import com.the9grounds.aeadditions.util.GuiUtil;

public class PacketTerminalSelectFluidClient extends Packet {
	Fluid fluid;

	public PacketTerminalSelectFluidClient(Fluid fluid) {
		this.fluid = fluid;
	}

	@Override
	public void writeData(PacketBufferEC data) throws IOException {
		data.writeFluid(fluid);
	}

	@Override
	public PacketId getPacketId() {
		return PacketId.TERMINAL_SELECT_FLUID;
	}

	@SideOnly(Side.CLIENT)
	public static class Handler implements IPacketHandlerClient {
		@Override
		public void onPacketData(PacketBufferEC data, EntityPlayer player) throws IOException {
			Fluid fluid = data.readFluid();
			GuiTerminal guiTerminal = GuiUtil.getGui(GuiTerminal.class);
			if (fluid == null || guiTerminal == null) {
				return;
			}

			guiTerminal.receiveSelectedFluid(fluid);
		}
	}
}
