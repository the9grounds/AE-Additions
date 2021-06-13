package com.the9grounds.aeadditions.network.packet.part;

import com.the9grounds.aeadditions.container.ContainerTerminal;
import com.the9grounds.aeadditions.network.packet.IPacketHandlerServer;
import com.the9grounds.aeadditions.network.packet.Packet;
import com.the9grounds.aeadditions.network.packet.PacketBufferEC;
import com.the9grounds.aeadditions.network.packet.PacketId;
import com.the9grounds.aeadditions.part.gas.PartGasTerminal;
import com.the9grounds.aeadditions.util.GuiUtil;
import net.minecraft.entity.player.EntityPlayerMP;

import java.io.IOException;

public class PacketTerminalOpenContainer extends Packet {
	PartGasTerminal terminalFluid;

	public PacketTerminalOpenContainer(PartGasTerminal terminalFluid) {
		this.terminalFluid = terminalFluid;
	}

	@Override
	public void writeData(PacketBufferEC data) throws IOException {
		data.writePart(terminalFluid);
	}

	@Override
	public PacketId getPacketId() {
		return PacketId.TERMINAL_OPEN_CONTAINER;
	}

	public static class Handler implements IPacketHandlerServer {
		@Override
		public void onPacketData(PacketBufferEC data, EntityPlayerMP player) throws IOException {
			PartGasTerminal terminalFluid = data.readPart(player.world);
			ContainerTerminal containerTerminal = GuiUtil.getContainer(player, ContainerTerminal.class);
			if (terminalFluid == null) {
				return;
			}

			containerTerminal.forceFluidUpdate();
			terminalFluid.sendCurrentFluid(containerTerminal);
		}
	}
}
