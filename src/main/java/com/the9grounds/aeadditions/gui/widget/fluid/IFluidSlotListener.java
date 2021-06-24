package com.the9grounds.aeadditions.gui.widget.fluid;

import net.minecraft.entity.player.EntityPlayer;

import net.minecraft.fluid.Fluid;

public interface IFluidSlotListener {

	void setFluid(int index, Fluid fluid, EntityPlayer player);
}
