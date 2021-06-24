package com.the9grounds.aeadditions.gui.gas;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.the9grounds.aeadditions.Constants;
import com.the9grounds.aeadditions.gui.IFluidSlotGuiTransfer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import net.minecraft.fluid.Fluid;
import net.minecraftforge.fluids.FluidStack;

import appeng.api.AEApi;
import appeng.api.config.RedstoneMode;
import com.the9grounds.aeadditions.container.gas.ContainerBusGasIO;
import com.the9grounds.aeadditions.gui.GuiBase;
import com.the9grounds.aeadditions.gui.ISlotRenderer;
import com.the9grounds.aeadditions.gui.SlotUpgradeRenderer;
import com.the9grounds.aeadditions.gui.buttons.ButtonRedstoneModes;
import com.the9grounds.aeadditions.gui.widget.fluid.WidgetFluidSlot;
import com.the9grounds.aeadditions.network.packet.other.IFluidSlotGui;
import com.the9grounds.aeadditions.network.packet.part.PacketPartConfig;
import com.the9grounds.aeadditions.part.gas.PartGasIO;
import com.the9grounds.aeadditions.util.FluidHelper;
import com.the9grounds.aeadditions.util.NetworkUtil;

public class GuiBusGasIO extends GuiBase<ContainerBusGasIO> implements
	WidgetFluidSlot.IConfigurable, IFluidSlotGui, IFluidSlotGuiTransfer {

	private PartGasIO part;
	private byte filterSize;
	private List<WidgetFluidSlot> fluidSlotList = new ArrayList<WidgetFluidSlot>();
	private boolean redstoneControlled;
	private boolean hasNetworkTool;

	public GuiBusGasIO(PartGasIO terminal, EntityPlayer player) {
		super(new ResourceLocation(Constants.MOD_ID, "textures/gui/busiofluid.png"), new ContainerBusGasIO(terminal, player));
		((ContainerBusGasIO) this.inventorySlots).setGui(this);
		this.part = terminal;
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				int id = y + x * 3;
				byte configOption = (byte) (((id + 1) % 2) + 1);
				if (id == 4) {
					configOption = 0;
				}
				WidgetFluidSlot fluidSlot = new WidgetFluidSlot(widgetManager, part, id, 61 + x * 18, 21 + y * 18, this, configOption);
				widgetManager.add(fluidSlot);
				fluidSlotList.add(fluidSlot);
			}
		}

		NetworkUtil.sendToServer(new PacketPartConfig(part, PacketPartConfig.FLUID_IO_INFO));
		this.hasNetworkTool = this.inventorySlots.getInventory().size() > 40;
		this.xSize = this.hasNetworkTool ? 246 : 211;
		this.ySize = 184;

	}

	@Override
	public void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		NetworkUtil.sendToServer(new PacketPartConfig(part, PacketPartConfig.FLUID_IO_REDSTONE_LOOP));
	}

	public void changeConfig(byte _filterSize) {
		this.filterSize = _filterSize;
	}

	@Override
	protected void drawBackground() {
		drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, 176, 184);
		drawTexturedModalRect(this.guiLeft + 179, this.guiTop, 179, 0, 32, 86);
		if (this.hasNetworkTool) {
			drawTexturedModalRect(this.guiLeft + 179, this.guiTop + 93, 178, 93, 68, 68);
		}
	}

	@Override
	public byte getConfigState() {
		return this.filterSize;
	}

	protected Slot getSlotAtPosition(int p_146975_1_, int p_146975_2_) {
		for (int k = 0; k < this.inventorySlots.inventorySlots.size(); ++k) {
			Slot slot = this.inventorySlots.inventorySlots.get(k);

			if (this.isMouseOverSlot(slot, p_146975_1_, p_146975_2_)) {
				return slot;
			}
		}
		return null;
	}

	private boolean isMouseOverSlot(Slot p_146981_1_, int p_146981_2_, int p_146981_3_) {
		return this.isPointInRegion(p_146981_1_.xPos, p_146981_1_.yPos, 16, 16, p_146981_2_, p_146981_3_);
	}

	protected boolean isPointInRegion(int top, int left, int height, int width, int pointX, int pointY) {
		int k1 = this.guiLeft;
		int l1 = this.guiTop;
		pointX -= k1;
		pointY -= l1;
		return pointX >= top - 1 && pointX < top + height + 1
			&& pointY >= left - 1 && pointY < left + width + 1;
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseBtn) throws IOException {
		Slot slot = getSlotAtPosition(mouseX, mouseY);

		if (slot != null) {
			ItemStack itemStack = slot.getStack();
			if (itemStack != null && itemStack.isItemEqual(AEApi.instance().definitions().items().networkTool().maybeStack(1).get())) {
				return;
			}
		}
		super.mouseClicked(mouseX, mouseY, mouseBtn);
	}

	@Override
	protected boolean hasSlotRenders() {
		return true;
	}

	@Nullable
	@Override
	protected ISlotRenderer getSlotRenderer(Slot slot) {
		if ((slot.getStack() == null || slot.getStack().isEmpty()) && slot.slotNumber > 35) {
			return SlotUpgradeRenderer.INSTANCE;
		}
		return null;
	}

	public void setRedstoneControlled(boolean _redstoneControlled) {
		this.redstoneControlled = _redstoneControlled;
		this.buttonList.clear();
		if (this.redstoneControlled) {
			this.buttonList.add(new ButtonRedstoneModes(0, this.guiLeft - 18, this.guiTop, 16, 16, this.part.getRedstoneMode()));
		}
	}

	@Override
	public boolean shiftClick(ItemStack itemStack) {
		FluidStack containerFluid = FluidHelper.getFluidFromContainer(itemStack);
		Fluid fluid = containerFluid == null ? null : containerFluid.getFluid();
		for (WidgetFluidSlot fluidSlot : this.fluidSlotList) {
			if (fluid != null && (fluidSlot.getFluid() == null || fluidSlot.getFluid() == fluid) && fluidSlot.isVisable()) {
				fluidSlot.handleContainer(itemStack);
				return true;
			}
		}
		return false;
	}

	@Override
	public void updateFluids(List<Fluid> fluidList) {
		for (int i = 0; i < this.fluidSlotList.size() && i < fluidList.size(); i++) {
			this.fluidSlotList.get(i).setFluid(fluidList.get(i));
		}
	}

	public void updateRedstoneMode(RedstoneMode mode) {
		if (this.redstoneControlled && this.buttonList.size() > 0) {
			((ButtonRedstoneModes) this.buttonList.get(0)).setRedstoneMode(mode);
		}
	}
}
