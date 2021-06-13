package com.the9grounds.aeadditions.item.storage;

import com.the9grounds.aeadditions.api.IAEAdditionsStorageCell;
import com.the9grounds.aeadditions.registries.CellDefinition;
import net.minecraft.item.ItemStack;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import appeng.api.storage.data.IAEFluidStack;

import com.the9grounds.aeadditions.inventory.ECFluidFilterInventory;
import com.the9grounds.aeadditions.inventory.InventoryPlain;
import com.the9grounds.aeadditions.util.StorageChannels;

public class ItemStorageCellFluid extends ItemStorageCell<IAEFluidStack> implements IAEAdditionsStorageCell<IAEFluidStack> {

    public ItemStorageCellFluid() {
        super(CellDefinition.FLUID, StorageChannels.FLUID);
    }

    @Override
    public IItemHandler getConfigInventory(ItemStack is) {
        return new InvWrapper(new ECFluidFilterInventory("configFluidCell", 63, is));
    }

    @Override
    public IItemHandler getUpgradesInventory(ItemStack is) {
        return new InvWrapper(new InventoryPlain("configInventory", 0, 64));
    }
}
