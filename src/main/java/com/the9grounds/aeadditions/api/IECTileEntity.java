package com.the9grounds.aeadditions.api;

import appeng.api.networking.IGridHost;
import appeng.api.util.DimensionalCoord;

public interface IECTileEntity extends IGridHost {

	DimensionalCoord getLocation();

	double getPowerUsage();

}
