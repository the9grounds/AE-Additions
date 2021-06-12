package extracells.item.storage;

import extracells.api.IExtraCellsStorageCell;
import extracells.registries.CellDefinition;
import net.minecraft.item.ItemStack;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import appeng.api.storage.IStorageChannel;

import extracells.api.gas.IAEGasStack;
import extracells.inventory.ECGasFilterInventory;
import extracells.inventory.InventoryPlain;
import extracells.util.StorageChannels;

public class ItemStorageCellGas extends ItemStorageCell<IAEGasStack> implements IExtraCellsStorageCell<IAEGasStack> {

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
