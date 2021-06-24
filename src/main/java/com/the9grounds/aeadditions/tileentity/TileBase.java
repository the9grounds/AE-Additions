package com.the9grounds.aeadditions.tileentity;

import javax.annotation.Nullable;

import appeng.api.storage.IStorageChannel;
import appeng.api.storage.data.IAEStack;
import com.the9grounds.aeadditions.util.StorageChannels;
import net.minecraft.block.BlockState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;

import appeng.api.networking.IGrid;
import appeng.api.networking.IGridHost;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.data.IAEFluidStack;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.util.AEPartLocation;
import net.minecraft.tileentity.TileEntityType;

public abstract class TileBase extends TileEntity {

	public TileBase(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(pos, 0, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		handleUpdateTag(getBlockState(), pkt.getNbtCompound());
	}

	public void updateBlock() {
		if (world == null || pos == null) {
			return;
		}
		BlockState blockState = world.getBlockState(pos);
		world.notifyBlockUpdate(pos, blockState, blockState, 0);
	}

	public IStorageGrid getStorageGrid(AEPartLocation side) {
		if (!(this instanceof IGridHost)) {
			return null;
		}
		IGridHost host = (IGridHost) this;
		if (host.getGridNode(side) == null) {
			return null;
		}
		IGrid grid = host.getGridNode(side).getGrid();
		if (grid == null) {
			return null;
		}
		return grid.getCache(IStorageGrid.class);
	}

	@Nullable
	public IMEMonitor<IAEFluidStack> getFluidInventory(AEPartLocation side) {
		IStorageGrid storageGrid = getStorageGrid(side);
		if (storageGrid == null) {
			return null;
		} else {
			return storageGrid.getInventory(StorageChannels.FLUID);
		}
	}

	@Nullable
	public IMEMonitor<IAEItemStack> getItemInventory(AEPartLocation side) {
		IStorageGrid storageGrid = getStorageGrid(side);
		if (storageGrid == null) {
			return null;
		} else {
			return storageGrid.getInventory(StorageChannels.ITEM);
		}
	}

	@Nullable
	public <T extends IAEStack<T>> IMEMonitor<T>  getInventory(AEPartLocation side, IStorageChannel<T> channel) {
		IStorageGrid storageGrid = getStorageGrid(side);
		if (storageGrid == null) {
			return null;
		} else {
			return storageGrid.getInventory(channel);
		}
	}

	protected void saveData(){
		markDirty();
	}
}
