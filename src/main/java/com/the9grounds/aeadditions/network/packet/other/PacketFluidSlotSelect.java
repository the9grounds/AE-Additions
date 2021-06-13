package com.the9grounds.aeadditions.network.packet.other;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;

import net.minecraftforge.fluids.Fluid;

import com.the9grounds.aeadditions.gui.widget.fluid.IFluidSlotListener;
import com.the9grounds.aeadditions.network.packet.IPacketHandlerServer;
import com.the9grounds.aeadditions.network.packet.Packet;
import com.the9grounds.aeadditions.network.packet.PacketBufferEC;
import com.the9grounds.aeadditions.network.packet.PacketId;
import com.the9grounds.aeadditions.part.PartECBase;

public class PacketFluidSlotSelect extends Packet {

	private int index;
	private Fluid fluid;
	private IFluidSlotListener partOrBlock;

	public PacketFluidSlotSelect(IFluidSlotListener partOrBlock, int index, Fluid fluid) {
		this.partOrBlock = partOrBlock;
		this.index = index;
		this.fluid = fluid;
	}

	@Override
	public PacketId getPacketId() {
		return PacketId.FLUID_SLOT;
	}

	@Override
	protected void writeData(PacketBufferEC data) throws IOException {
		if (this.partOrBlock instanceof PartECBase) {
			data.writeBoolean(true);
			data.writePart((PartECBase) this.partOrBlock);
		} else {
			data.writeBoolean(false);
			data.writeTile((TileEntity) this.partOrBlock);
		}
		data.writeInt(this.index);
		data.writeFluid(this.fluid);
	}

	public static class Handler implements IPacketHandlerServer {
		@Override
		public void onPacketData(PacketBufferEC data, EntityPlayerMP player) throws IOException {
			IFluidSlotListener listener;

			if (data.readBoolean()) {
				listener = data.readPart(player.world);
			} else {
				listener = data.readTile(player.world, IFluidSlotListener.class);
			}
			int index = data.readInt();
			Fluid fluid = data.readFluid();
			if (listener == null) {
				return;
			}
			listener.setFluid(index, fluid, player);
		}
	}
}
