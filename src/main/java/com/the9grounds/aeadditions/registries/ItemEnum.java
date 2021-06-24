package com.the9grounds.aeadditions.registries;

import com.the9grounds.aeadditions.item.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

import com.the9grounds.aeadditions.integration.Integration;
import com.the9grounds.aeadditions.item.storage.ItemStorageCasing;
import com.the9grounds.aeadditions.item.storage.ItemStorageCellFluid;
import com.the9grounds.aeadditions.item.storage.ItemStorageCellGas;
import com.the9grounds.aeadditions.item.storage.ItemStorageCellPhysical;
import com.the9grounds.aeadditions.item.storage.ItemStorageCellPortableFluid;
import com.the9grounds.aeadditions.item.storage.ItemStorageCellPortableGas;
import com.the9grounds.aeadditions.item.storage.ItemStorageComponent;
import com.the9grounds.aeadditions.util.CreativeTabEC;

public enum ItemEnum {
	PARTITEM("part.base", new ItemPartECBase()),
	FLUIDSTORAGE("storage.fluid", new ItemStorageCellFluid()),
	PHYSICALSTORAGE("storage.physical", new ItemStorageCellPhysical()),
	GASSTORAGE("storage.gas", new ItemStorageCellGas(), Integration.Mods.MEKANISMGAS),
	FLUIDPATTERN("pattern.fluid", new ItemFluidPattern()),
	FLUIDWIRELESSTERMINAL("terminal.fluid.wireless", ItemWirelessTerminalFluid.INSTANCE),
	STORAGECOMPONET("storage.component", new ItemStorageComponent()),
	STORAGECASING("storage.casing", new ItemStorageCasing()),
	FLUIDITEM("fluid.item", new ItemFluid(), null, null, true), // Internal EC Item
	FLUIDSTORAGEPORTABLE("storage.fluid.portable", new ItemStorageCellPortableFluid()),
	GASSTORAGEPORTABLE("storage.gas.portable", new ItemStorageCellPortableGas(), Integration.Mods.MEKANISMGAS),
	CRAFTINGPATTERN("pattern.crafting", new ItemInternalCraftingPattern(), null, null, true),// Internal EC Item
	UNIVERSALTERMINAL("terminal.universal.wireless", new ItemWirelessTerminalUniversal()),
	GASWIRELESSTERMINAL("terminal.gas.wireless", ItemWirelessTerminalGas.INSTANCE, Integration.Mods.MEKANISMGAS),
	OCUPGRADE("oc.upgrade", ItemOCUpgrade.INSTANCE, Integration.Mods.OPENCOMPUTERS, false),
	GASITEM("gas.item", ItemGas.INSTANCE, Integration.Mods.MEKANISMGAS, null, true); //Internal EC Item

	private final String internalName;
	private Item item;
	private Integration.Mods mod;
	private Boolean enabled = true;

	ItemEnum(String internalName, Item item) {
		this(internalName, item, null);
	}

	ItemEnum(String internalName, Item item, Integration.Mods mod) {
		this(internalName, item, mod, CreativeTabEC.INSTANCE);
	}

	ItemEnum(String internalName, Item item, Integration.Mods mod, Boolean enabled) {
		this(internalName, item, mod, CreativeTabEC.INSTANCE);
		this.enabled = enabled;
	}

	ItemEnum(String internalName, Item item, Integration.Mods mod, CreativeTabs creativeTab) {
		this.internalName = internalName;
		this.item = item;
		this.item.setTranslationKey("com.the9grounds.aeadditions." + this.internalName);
		this.item.setRegistryName(this.internalName);
		this.mod = mod;
		if ((creativeTab != null) && (mod == null || mod.isEnabled())) {
			this.item.setCreativeTab(creativeTab);
		}
	}

	ItemEnum(String internalName, Item item, Integration.Mods mod, CreativeTabs creativeTab, Boolean enabled) {
		this.internalName = internalName;
		this.item = item;
		this.item.setTranslationKey("com.the9grounds.aeadditions." + this.internalName);
		this.item.setRegistryName(this.internalName);
		this.mod = mod;
		if ((creativeTab != null) && (mod == null || mod.isEnabled())) {
			this.item.setCreativeTab(creativeTab);
		}
		this.enabled = enabled;
	}

	public ItemStack getDamagedStack(int damage) {
		return new ItemStack(this.item, 1, damage);
	}

	public String getInternalName() {
		return this.internalName;
	}

	public Item getItem() {
		return this.item;
	}

	public ItemStack getSizedStack(int size) {
		return new ItemStack(this.item, size);
	}

	public String getStatName() {
		return I18n.translateToLocal(this.item.getTranslationKey());
	}

	public Integration.Mods getMod() {
		return mod;
	}

	public boolean shouldRegister() {
		return mod == null || mod.isEnabled();
	}

	public Boolean isEnabled() {
		return enabled;
	}
}
