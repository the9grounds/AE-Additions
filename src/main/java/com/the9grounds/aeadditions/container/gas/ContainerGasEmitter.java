package com.the9grounds.aeadditions.container.gas;

import com.the9grounds.aeadditions.part.gas.PartGasLevelEmitter;
import com.the9grounds.aeadditions.util.GasUtil;
import mekanism.api.gas.GasStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fluids.FluidStack;

import com.the9grounds.aeadditions.network.packet.part.PacketPartConfig;
import com.the9grounds.aeadditions.util.FluidHelper;
import com.the9grounds.aeadditions.util.NetworkUtil;

public class ContainerGasEmitter extends Container {

	private final PartGasLevelEmitter part;
	private final EntityPlayer player;
	private long clientAmount = -1;

	public ContainerGasEmitter(PartGasLevelEmitter part, EntityPlayer player) {
		super();
		this.part = part;
		this.player = player;
		bindPlayerInventory(this.player.inventory);
	}

	protected void bindPlayerInventory(IInventory inventoryPlayer) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9,
					8 + j * 18, i * 18 + 84));
			}
		}

		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
		}
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		if (clientAmount != part.getWantedAmount()) {
			clientAmount = part.getWantedAmount();
			NetworkUtil.sendToPlayer(new PacketPartConfig(part, PacketPartConfig.FLUID_EMITTER_AMOUNT, Long.toString(clientAmount)), player);
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotnumber) {
		Slot slot = this.inventorySlots.get(slotnumber);
		if (slot != null && slot.getHasStack()) {
			ItemStack gasItem = slot.getStack().copy();
			gasItem.setCount(1);
			GasStack gasStack = GasUtil.getGasFromContainer(gasItem);
			FluidStack fluidStack = GasUtil.getFluidStack(gasStack);
			if (fluidStack == null) {
				return ItemStack.EMPTY;
			}
			this.part.setFluid(0, fluidStack.getFluid(), player);
			return ItemStack.EMPTY;
		}
		return ItemStack.EMPTY;
	}
}
