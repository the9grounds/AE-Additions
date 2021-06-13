package com.the9grounds.aeadditions.item;

import net.minecraft.item.Item;

import com.the9grounds.aeadditions.models.IItemModelRegister;
import com.the9grounds.aeadditions.models.ModelManager;
import com.the9grounds.aeadditions.util.CreativeTabEC;

public class ItemECBase extends Item implements IItemModelRegister {
	public ItemECBase() {
		setCreativeTab(CreativeTabEC.INSTANCE);
	}

	@Override
	public void registerModel(Item item, ModelManager manager) {
		manager.registerItemModel(item);
	}
}
