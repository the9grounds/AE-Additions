package com.the9grounds.aeadditions.models.drive;

import appeng.api.storage.ICellContainer;

public interface IECDrive extends ICellContainer {
	int getCellCount();

	int getCellStatus(int index);

	boolean isPowered();
}
