package com.the9grounds.aeadditions.util;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.the9grounds.aeadditions.registries.ItemEnum;

public class CreativeTabEC extends CreativeTabs {

	public static final CreativeTabs INSTANCE = new CreativeTabEC();

	public CreativeTabEC() {
		super("AE_Additions");
	}

	@Override
	public ItemStack createIcon() {
		return ItemEnum.FLUIDSTORAGE.getSizedStack(1);
	}
}
