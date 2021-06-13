package com.the9grounds.aeadditions.network.packet.other;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

import com.the9grounds.aeadditions.network.packet.IPacketHandlerServer;
import com.the9grounds.aeadditions.network.packet.Packet;
import com.the9grounds.aeadditions.network.packet.PacketBufferEC;
import com.the9grounds.aeadditions.network.packet.PacketId;
import com.the9grounds.aeadditions.tileentity.TileEntityFluidFiller;

public class PacketFluidContainerSlot extends Packet {

	private ItemStack container;
	private TileEntityFluidFiller fluidFiller;

	public PacketFluidContainerSlot(TileEntityFluidFiller fluidFiller, ItemStack container) {
		this.fluidFiller = fluidFiller;
		this.container = container;
	}

	@Override
	public PacketId getPacketId() {
		return PacketId.FLUID_CONTAINER_SLOT;
	}

	@Override
	protected void writeData(PacketBufferEC data) throws IOException {
		data.writeTile(this.fluidFiller);
		data.writeItemStack(this.container);
	}

	public static class Handler implements IPacketHandlerServer {
		@Override
		public void onPacketData(PacketBufferEC data, EntityPlayerMP player) throws IOException {
			TileEntityFluidFiller fluidFiller = data.readTile(player.world, TileEntityFluidFiller.class);
			ItemStack container = data.readItemStack();

			if (fluidFiller == null) {
				return;
			}

			container.setCount(1);
			fluidFiller.containerItem = container;
			fluidFiller.markDirty();
			if (fluidFiller.hasWorld()) {
				fluidFiller.updateBlock();
			}
			fluidFiller.postUpdateEvent();
		}
	}
}
