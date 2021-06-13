package com.the9grounds.aeadditions.item.storage;

import com.the9grounds.aeadditions.api.IAEAdditionsStorageCell;
import com.the9grounds.aeadditions.registries.CellDefinition;
import net.minecraft.item.ItemStack;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import appeng.api.storage.IStorageChannel;

import com.the9grounds.aeadditions.api.gas.IAEGasStack;
import com.the9grounds.aeadditions.inventory.ECGasFilterInventory;
import com.the9grounds.aeadditions.inventory.InventoryPlain;
import com.the9grounds.aeadditions.util.StorageChannels;

public class ItemStorageCellGas extends ItemStorageCell<IAEGasStack> implements IAEAdditionsStorageCell<IAEGasStack> {

    public ItemStorageCellGas() {
        super(CellDefinition.GAS, StorageChannels.GAS);
    }

    @Override
    public IItemHandler getConfigInventory(ItemStack is) {
        return new InvWrapper(new ECGasFilterInventory("configFluidCell", 63, is));
    }

    @Override
    public IItemHandler getUpgradesInventory(ItemStack is) {
        return new InvWrapper(new InventoryPlain("configInventory", 0, 64));
    }

    public IStorageChannel<IAEGasStack> getChannel() {
        return StorageChannels.GAS;
    }
}
