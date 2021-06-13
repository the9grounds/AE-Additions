package com.the9grounds.aeadditions.inventory;

import net.minecraft.item.ItemStack;

import appeng.api.AEApi;
import appeng.api.storage.ICellRegistry;
import com.the9grounds.aeadditions.part.PartDrive;

public class InventoryPartDrive extends InventoryPlain {
	private final ICellRegistry cellRegistry;

	public InventoryPartDrive(PartDrive listener) {
		super("com.the9grounds.aeadditions.part.drive", 6, 1, listener);
		cellRegistry = AEApi.instance().registries().cell();
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemStack) {
		return this.cellRegistry.isCellHandled(itemStack);
	}
}
