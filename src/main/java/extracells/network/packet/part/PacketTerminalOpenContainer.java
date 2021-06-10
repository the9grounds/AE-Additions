package extracells.network.packet.part;

import extracells.container.ContainerTerminal;
import extracells.network.packet.IPacketHandlerServer;
import extracells.network.packet.Packet;
import extracells.network.packet.PacketBufferEC;
import extracells.network.packet.PacketId;
import extracells.part.gas.PartGasTerminal;
import extracells.util.GuiUtil;
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
