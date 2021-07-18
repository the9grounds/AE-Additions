package com.the9grounds.aeadditions.gui.gas;

import javax.annotation.Nullable;

import com.the9grounds.aeadditions.Constants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import appeng.api.implementations.ICraftingPatternItem;
import appeng.api.util.AEPartLocation;
import com.the9grounds.aeadditions.api.IFluidInterface;
import com.the9grounds.aeadditions.container.fluid.ContainerFluidInterface;
import com.the9grounds.aeadditions.gui.GuiBase;
import com.the9grounds.aeadditions.gui.ISlotRenderer;
import com.the9grounds.aeadditions.gui.SlotOutputRenderer;
import com.the9grounds.aeadditions.gui.widget.WidgetFluidTank;
import com.the9grounds.aeadditions.gui.widget.fluid.IFluidSlotListener;
import com.the9grounds.aeadditions.gui.widget.fluid.WidgetFluidSlot;

public class GuiFluidInterface extends GuiBase<ContainerFluidInterface> {
	private AEPartLocation partSide;
	public WidgetFluidSlot[] filter = new WidgetFluidSlot[6];

	public GuiFluidInterface(EntityPlayer player, IFluidInterface fluidInterface) {
		this(player, fluidInterface, AEPartLocation.INTERNAL);
	}

	public GuiFluidInterface(EntityPlayer player, IFluidInterface fluidInterface, AEPartLocation side) {
		super(new ResourceLocation(Constants.MOD_ID, "textures/gui/interfacefluid.png"), new ContainerFluidInterface(player, fluidInterface));
		this.ySize = 230;
		this.partSide = side;
		((ContainerFluidInterface) this.inventorySlots).gui = this;
		for (int i = 0; i < 6; i++) {
			if (this.partSide != null && this.partSide != AEPartLocation.INTERNAL && this.partSide.ordinal() != i) {
				continue;
			}
			int xPos = i * 20 + 30;
			AEPartLocation location = AEPartLocation.fromOrdinal(i);
			widgetManager.add(new WidgetFluidTank(widgetManager, fluidInterface.getFluidTank(location), xPos, 16, location));
			if (fluidInterface instanceof IFluidSlotListener) {
				widgetManager.add(filter[i] = new WidgetFluidSlot(widgetManager, (IFluidSlotListener) fluidInterface, i, xPos, 93));
			}
		}
	}

	@Override
	protected boolean hasSlotRenders() {
		return true;
	}

	@Nullable
	@Override
	protected ISlotRenderer getSlotRenderer(Slot slot) {
		if (slot.slotNumber < 9) {
			ItemStack stack = slot.getStack();
			if (stack == null || stack.isEmpty()) {
				return SlotOutputRenderer.INSTANCE;
			} else {
				if (stack.getItem() instanceof ICraftingPatternItem) {
					return SlotOutputRenderer.INSTANCE;
				}
			}
		}
		return null;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
//		ItemStack itemStack = new ItemStack(BlockEnum.ECBASEBLOCK.getBlock());
//		this.fontRenderer.drawString(itemStack.getDisplayName().replace("ME ", ""), 8, 5, 0x000000);
//		this.fontRenderer.drawString(I18n.translateToLocal("container.inventory"), 8, 136, 0x000000);
	}
}
