package com.the9grounds.aeadditions.gui.widget.fluid;

import appeng.api.storage.data.IAEFluidStack;
import com.the9grounds.aeadditions.container.IFluidSelectorContainer;

public interface IFluidSelectorGui extends IFluidWidgetGui {

	IFluidSelectorContainer getContainer();

	IAEFluidStack getCurrentFluid();
}
