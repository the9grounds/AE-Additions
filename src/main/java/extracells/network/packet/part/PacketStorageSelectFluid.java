package extracells.network.packet.part;

import extracells.container.ContainerStorage;
import extracells.network.packet.IPacketHandlerServer;
import extracells.network.packet.Packet;
import extracells.network.packet.PacketBufferEC;
import extracells.network.packet.PacketId;
import extracells.util.GuiUtil;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fluids.Fluid;

import java.io.IOException;

public class PacketStorageSelectFluid extends Packet {
	Fluid fluid;

	public PacketStorageSelectFluid(Fluid fluid) {
		this.fluid = fluid;
	}

	@Override
	public void writeData(PacketBufferEC data) throws IOException {
		data.writeFluid(fluid);
	}

	@Override
	public PacketId getPacketId() {
		return PacketId.STORAGE_SELECT_FLUID;
	}

	public static class Handler implements IPacketHandlerServer {
		@Override
		public void onPacketData(PacketBufferEC data, EntityPlayerMP player) throws IOException {
			Fluid fluid = data.readFluid();
			ContainerStorage containerStorage = GuiUtil.getContainer(player, ContainerStorage.class);
			if (fluid == null || containerStorage == null) {
				return;
			}

			containerStorage.receiveSelectedFluid(fluid);
		}
	}
}
