package extracells.network.packet.part;

import appeng.api.config.AccessRestriction;
import appeng.api.config.RedstoneMode;
import extracells.gui.gas.GuiBusGasIO;
import extracells.gui.gas.GuiBusGasStorage;
import extracells.gui.gas.GuiGasEmitter;
import extracells.network.packet.*;
import extracells.part.PartECBase;
import extracells.part.gas.PartGasIO;
import extracells.part.gas.PartGasLevelEmitter;
import extracells.part.gas.PartGasStorage;
import extracells.util.GuiUtil;
import extracells.util.NetworkUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

public class PacketPartConfig extends Packet {
	public static final String FLUID_EMITTER_TOGGLE = "FluidEmitter.Toggle";
	public static final String FLUID_EMITTER_AMOUNT = "FluidEmitter.Amount";
	public static final String FLUID_EMITTER_AMOUNT_CHANGE = "FluidEmitter.Amount.Change";
	public static final String FLUID_EMITTER_MODE = "FluidEmitter.Mode";

	public static final String FLUID_IO_REDSTONE = "FluidIO.Redstone";
	public static final String FLUID_IO_REDSTONE_LOOP = "FluidIO.Redstone.Loop";
	public static final String FLUID_IO_REDSTONE_MODE = "FluidIO.Redstone.Mode";
	public static final String FLUID_IO_INFO = "FluidIO.Info";
	public static final String FLUID_IO_FILTER = "FluidIO.Filter";

	public static final String FLUID_STORAGE_INFO = "FluidStorage.Info";
	public static final String FLUID_STORAGE_ACCESS = "FluidStorage.Access";

	public static final String FLUID_PLANE_FORMATION_INFO = "FluidPlaneFormation.Info";

	private PartECBase part;
	private String name;
	private String value;

	public PacketPartConfig(PartECBase part, String name) {
		this.part = part;
		this.name = name;
		this.value = "";
	}

	public PacketPartConfig(PartECBase part, String name, String value) {
		this.part = part;
		this.name = name;
		this.value = value;
	}

	@Override
	protected void writeData(PacketBufferEC data) throws IOException {
		data.writePart(part);
		data.writeString(name);
		data.writeString(value);
	}

	@Override
	public PacketId getPacketId() {
		return PacketId.PART_CONFIG;
	}

	@SideOnly(Side.CLIENT)
	public static class HandlerClient implements IPacketHandlerClient {
		@Override
		public void onPacketData(PacketBufferEC data, EntityPlayer player) throws IOException {
			PartECBase part = data.readPart(player.world);
			String name = data.readString();
			String value = data.readString();
			if (name.equals(FLUID_EMITTER_AMOUNT) && part instanceof PartGasLevelEmitter) {
				long amount = Long.valueOf(value);
				GuiGasEmitter gui = GuiUtil.getGui(GuiGasEmitter.class);
				if (gui == null) {
					return;
				}
				gui.setAmountField(amount);
			} else if (name.equals(FLUID_EMITTER_MODE) && part instanceof PartGasLevelEmitter) {
				RedstoneMode redstoneMode = RedstoneMode.valueOf(value);
				GuiGasEmitter gui = GuiUtil.getGui(GuiGasEmitter.class);
				if (gui == null) {
					return;
				}
				gui.setRedstoneMode(redstoneMode);
			} else if (name.equals(FLUID_IO_REDSTONE) && part instanceof PartGasIO) {
				boolean redstoneControlled = Boolean.valueOf(value);
				GuiBusGasIO gui = GuiUtil.getGui(GuiBusGasIO.class);
				if (gui == null) {
					return;
				}
				gui.setRedstoneControlled(redstoneControlled);
			} else if (name.equals(FLUID_IO_FILTER) && part instanceof PartGasIO) {
				byte filterSize = Byte.valueOf(value);
				GuiBusGasIO gui = GuiUtil.getGui(GuiBusGasIO.class);
				if (gui == null) {
					return;
				}
				gui.changeConfig(filterSize);
			} else if (name.equals(FLUID_IO_REDSTONE_MODE) && part instanceof PartGasIO) {
				RedstoneMode redstoneMode = RedstoneMode.valueOf(value);
				GuiBusGasIO gui = GuiUtil.getGui(GuiBusGasIO.class);
				if (gui == null) {
					return;
				}
				gui.updateRedstoneMode(redstoneMode);
			} else if (name.equals(FLUID_STORAGE_ACCESS)) {
				AccessRestriction access = AccessRestriction.valueOf(value);
				GuiBusGasStorage gui = GuiUtil.getGui(GuiBusGasStorage.class);
				if (gui == null || access == null) {
					return;
				}
				gui.updateAccessRestriction(access);
			}
		}
	}

	public static class HandlerServer implements IPacketHandlerServer {
		@Override
		public void onPacketData(PacketBufferEC data, EntityPlayerMP player) throws IOException {
			PartECBase part = data.readPart(player.world);
			String name = data.readString();
			String value = data.readString();
			if (name.equals(FLUID_EMITTER_TOGGLE) && part instanceof PartGasLevelEmitter) {
				boolean toggle = Boolean.valueOf(value);
				if (toggle) {
					((PartGasLevelEmitter) part).toggleMode(player);
				} else {
					((PartGasLevelEmitter) part).syncClientGui(player);
				}
			} else if (name.equals(FLUID_EMITTER_AMOUNT_CHANGE) && part instanceof PartGasLevelEmitter) {
				long amount = Long.valueOf(value);
				((PartGasLevelEmitter) part).changeWantedAmount((int) amount, player);
			} else if (name.equals(FLUID_EMITTER_AMOUNT) && part instanceof PartGasLevelEmitter) {
				long amount = Long.valueOf(value);
				((PartGasLevelEmitter) part).setWantedAmount(amount, player);
			} else if (name.equals(FLUID_IO_INFO) && part instanceof PartGasIO) {
				((PartGasIO) part).sendInformation(player);
			} else if (name.equals(FLUID_IO_REDSTONE_LOOP) && part instanceof PartGasIO) {
				((PartGasIO) part).loopRedstoneMode(player);
			} else if (name.equals(FLUID_STORAGE_ACCESS) && part instanceof PartGasStorage) {
				AccessRestriction access = AccessRestriction.valueOf(value);
				if (access == null) {
					return;
				}
				((PartGasStorage) part).updateAccess(access);
				NetworkUtil.sendToPlayer(new PacketPartConfig(part, PacketPartConfig.FLUID_STORAGE_ACCESS, value), player);
			} else if (name.equals(FLUID_STORAGE_INFO) && part instanceof PartGasStorage) {
				((PartGasStorage) part).sendInformation(player);
			}
		}
	}
}
