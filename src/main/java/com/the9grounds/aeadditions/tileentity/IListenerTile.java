package com.the9grounds.aeadditions.tileentity;

import appeng.api.networking.IGrid;

public interface IListenerTile {

	void registerListener();

	void removeListener();

	void updateGrid(IGrid oldGrid, IGrid newGrid);

}
