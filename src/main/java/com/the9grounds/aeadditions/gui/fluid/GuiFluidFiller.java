package com.the9grounds.aeadditions.gui.fluid;

import com.the9grounds.aeadditions.Constants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

import com.the9grounds.aeadditions.container.fluid.ContainerFluidFiller;
import com.the9grounds.aeadditions.gui.GuiBase;
import com.the9grounds.aeadditions.gui.widget.WidgetSlotFluidContainer;
import com.the9grounds.aeadditions.tileentity.TileEntityFluidFiller;

public class GuiFluidFiller extends GuiBase<ContainerFluidFiller> {
	public static final int xSize = 176;
	public static final int ySize = 166;

	public GuiFluidFiller(EntityPlayer player, TileEntityFluidFiller tileentity) {
		super(new ResourceLocation(Constants.MOD_ID, "textures/gui/fluidfiller.png"), new ContainerFluidFiller(player.inventory, tileentity));
		widgetManager.add(new WidgetSlotFluidContainer(tileentity, widgetManager, 80, 35));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.fontRenderer.drawString(I18n.translateToLocal("tile.com.the9grounds.aeadditions.block.fluidfiller.name").replace("ME ", ""), 5, 5, 0x000000);
	}
}
