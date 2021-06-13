package com.the9grounds.aeadditions.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import appeng.api.implementations.ICraftingPatternItem;
import appeng.api.networking.crafting.ICraftingPatternDetails;
import com.the9grounds.aeadditions.crafting.CraftingPattern;
import com.the9grounds.aeadditions.crafting.CraftingPattern2;
import com.the9grounds.aeadditions.models.IItemModelRegister;
import com.the9grounds.aeadditions.models.ModelManager;

public class ItemInternalCraftingPattern extends Item implements ICraftingPatternItem, IItemModelRegister {

	@Override
	public ICraftingPatternDetails getPatternForItem(ItemStack is, World w) {
		if (is == null || w == null) {
			return null;
		}
		switch (is.getItemDamage()) {
			case 0:
				if (is.hasTagCompound() && is.getTagCompound().hasKey("item")) {
					ItemStack s = new ItemStack(is.getTagCompound().getCompoundTag("item"));
					if (s != null && s.getItem() instanceof ICraftingPatternItem) {
						return new CraftingPattern(((ICraftingPatternItem) s.getItem()).getPatternForItem(s, w));
					}
				}
				return null;
			case 1:
				if (is.hasTagCompound() && is.getTagCompound().hasKey("item")) {
					ItemStack s = new ItemStack(is.getTagCompound().getCompoundTag("item"));
					if (s != null && s.getItem() instanceof ICraftingPatternItem) {
						return new CraftingPattern2(((ICraftingPatternItem) s.getItem()).getPatternForItem(s, w));
					}
				}
			default:
				return null;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel(Item item, ModelManager manager) {
		manager.registerItemModel(item, 0);
	}
}
