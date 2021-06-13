package com.the9grounds.aeadditions.network.packet.part;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.the9grounds.aeadditions.container.ContainerStorage;
import com.the9grounds.aeadditions.network.packet.IPacketHandlerClient;
import com.the9grounds.aeadditions.network.packet.Packet;
import com.the9grounds.aeadditions.network.packet.PacketBufferEC;
import com.the9grounds.aeadditions.network.packet.PacketId;
import com.the9grounds.aeadditions.util.GuiUtil;

public class PacketStorageUpdateState extends Packet {
	boolean hasTermHandler;

	public PacketStorageUpdateState(boolean hasTermHandler) {
		this.hasTermHandler = hasTermHandler;
	}

	@Override
	public void writeData(PacketBufferEC data) throws IOException {
		data.writeBoolean(hasTermHandler);
	}

	@Override
	public PacketId getPacketId() {
		return PacketId.STORAGE_UPDATE_STATE;
	}

	@SideOnly(Side.CLIENT)
	public static class Handler implements IPacketHandlerClient {
		@Override
		public void onPacketData(PacketBufferEC data, EntityPlayer player) throws IOException {
			boolean hasTermHandler = data.readBoolean();
			ContainerStorage containerStorage = GuiUtil.getContainer(player, ContainerStorage.class);
			if (containerStorage == null) {
				return;
			}

			containerStorage.hasWirelessTermHandler = hasTermHandler;
		}
	}
}
