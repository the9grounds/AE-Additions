package com.the9grounds.aeadditions.item.storage;

import java.util.List;

import com.the9grounds.aeadditions.config.AEAConfiguration;
import com.the9grounds.aeadditions.registries.CellDefinition;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import appeng.api.implementations.items.IStorageComponent;
import com.the9grounds.aeadditions.integration.Integration;
import com.the9grounds.aeadditions.item.ItemECBase;
import com.the9grounds.aeadditions.models.ModelManager;

public class ItemStorageComponent extends ItemECBase implements IStorageComponent {

	public ItemStorageComponent() {
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	@Override
	public int getBytes(ItemStack itemStack) {
		StorageType type = AEAConfiguration.components.fromMeta(itemStack.getItemDamage());
		return type.getBytes();
	}

	@Override
	public EnumRarity getRarity(ItemStack itemStack) {
		StorageType type = AEAConfiguration.components.fromMeta(itemStack.getItemDamage());
		CellDefinition definition = type.getDefinition();
		return definition.getRarity();
	}

	@Override
	public void getSubItems(CreativeTabs creativeTab, NonNullList itemList) {
		if (!this.isInCreativeTab(creativeTab))
			return;
		for (StorageType type : AEAConfiguration.components) {
			if ((type.getDefinition() == CellDefinition.GAS && !Integration.Mods.MEKANISMGAS.isEnabled()) || !type.getEnabled()) {
				continue;
			}
			itemList.add(new ItemStack(this, 1, type.getMeta()));
		}
	}

	@Override
	public String getTranslationKey(ItemStack itemStack) {
		StorageType type = AEAConfiguration.components.fromMeta(itemStack.getItemDamage());
		return "com.the9grounds.aeadditions.item.storage.component." + type.getIdentifier();
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		StorageType type = AEAConfiguration.components.fromMeta(stack.getItemDamage());
		return String.format(super.getItemStackDisplayName(stack), type.getSize());
	}

	@Override
	public boolean isStorageComponent(ItemStack itemStack) {
		return itemStack.getItem() == this;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel(Item item, ModelManager manager) {
		for (StorageType type : AEAConfiguration.components) {
			if ((type.getDefinition() == CellDefinition.GAS && !Integration.Mods.MEKANISMGAS.isEnabled()) || !type.getEnabled()) {
				continue;
			}
			manager.registerItemModel(item, type.getMeta(), type.getModelName());
		}
	}
}
