package com.the9grounds.aeadditions.crafting;

import com.the9grounds.aeadditions.util.FluidHelper;
import com.the9grounds.aeadditions.util.StorageChannels;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import net.minecraftforge.fluids.FluidStack;

import appeng.api.AEApi;
import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.api.storage.data.IAEFluidStack;
import appeng.api.storage.data.IAEItemStack;
import com.the9grounds.aeadditions.api.crafting.IFluidCraftingPatternDetails;
import com.the9grounds.aeadditions.registries.ItemEnum;

public class CraftingPattern implements IFluidCraftingPatternDetails,
	Comparable<CraftingPattern> {

	protected final ICraftingPatternDetails pattern;

	private IAEFluidStack[] fluidsCondensed = null;
	private IAEFluidStack[] fluids = null;

	public CraftingPattern(ICraftingPatternDetails _pattern) {
		this.pattern = _pattern;
	}

	@Override
	public boolean canSubstitute() {
		return this.pattern.canSubstitute();
	}

	public int compareInt(int int1, int int2) {
		if (int1 == int2) {
			return 0;
		}
		if (int1 < int2) {
			return -1;
		}
		return 1;
	}

	@Override
	public int compareTo(CraftingPattern o) {
		return compareInt(o.getPriority(), this.getPriority());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		CraftingPattern other = (CraftingPattern) obj;
		if (this.pattern != null && other.pattern != null) {
			return this.pattern.equals(other.pattern);
		}
		return false;
	}

	@Override
	public IAEFluidStack[] getCondensedFluidInputs() {
		if (this.fluidsCondensed == null) {
			getCondensedInputs();
		}
		return this.fluidsCondensed;
	}

	@Override
	public IAEItemStack[] getCondensedInputs() {
		if (this.pattern == null)
		{
			return new IAEItemStack[0];
		}
		return removeFluidContainers(this.pattern.getCondensedInputs(), true);
	}

	@Override
	public IAEItemStack[] getCondensedOutputs() {
		if (this.pattern == null)
		{
			return new IAEItemStack[0];
		}

		return this.pattern.getCondensedOutputs();
	}

	@Override
	public IAEFluidStack[] getFluidInputs() {
		if (this.fluids == null) {
			getInputs();
		}
		return this.fluids;
	}

	@Override
	public IAEItemStack[] getInputs() {
		return removeFluidContainers(this.pattern.getInputs(), false);
	}

	@Override
	public ItemStack getOutput(InventoryCrafting craftingInv, World world) {
		IAEItemStack[] input = this.pattern.getInputs();
		for (int i = 0; i < input.length; i++) {
			IAEItemStack stack = input[i];
			if (stack != null
				&& FluidHelper.isFluidContainer(stack.createItemStack())) {
				try {
					craftingInv.setInventorySlotContents(i,
						input[i].createItemStack());
				} catch (Throwable e) {
				}
			}
		}
		ItemStack returnStack = this.pattern.getOutput(craftingInv, world);
		for (int i = 0; i < input.length; i++) {
			IAEItemStack stack = input[i];
			if (stack != null
				&& FluidHelper.isFluidContainer(stack.createItemStack()) && FluidHelper.isFilled(stack.createItemStack())) {
				craftingInv.setInventorySlotContents(i, ItemStack.EMPTY);
			}
		}
		return returnStack;
	}

	@Override
	public IAEItemStack[] getOutputs() {
		return this.pattern.getOutputs();
	}

	@Override
	public ItemStack getPattern() {
		ItemStack p = this.pattern.getPattern();
		if (p == null || p.isEmpty()) {
			return ItemStack.EMPTY;
		}
		ItemStack s = new ItemStack(ItemEnum.CRAFTINGPATTERN.getItem());
		NBTTagCompound tag = new NBTTagCompound();
		tag.setTag("item", p.writeToNBT(new NBTTagCompound()));
		s.setTagCompound(tag);
		return s;
	}

	@Override
	public int getPriority() {
		return this.pattern.getPriority();
	}

	@Override
	public boolean isCraftable() {
		return this.pattern.isCraftable();
	}

	@Override
	public boolean isValidItemForSlot(int slotIndex, ItemStack itemStack,
		World world) {
		return this.pattern.isValidItemForSlot(slotIndex, itemStack, world);
	}

	public IAEItemStack[] removeFluidContainers(IAEItemStack[] requirements,
		boolean isCondenced) {

		IAEItemStack[] returnStack = new IAEItemStack[requirements.length];

		IAEFluidStack[] fluidStacks = new IAEFluidStack[requirements.length];

		int removed = 0;
		int i = 0;
		for (IAEItemStack currentRequirement : requirements) {

			if (currentRequirement != null) {
				ItemStack current = currentRequirement.createItemStack();
				current.setCount(1);
				FluidStack fluid = null;
				if (FluidHelper.isFilled(current)) {
					fluid = FluidHelper.getFluidFromContainer(current);
				}
				if (fluid == null) {
					returnStack[i] = currentRequirement;
				} else {
					removed++;
					fluidStacks[i] = StorageChannels.FLUID.createStack(new FluidStack(fluid.getFluid(),(int) (fluid.amount * currentRequirement.getStackSize())));
				}
			}
			i++;
		}

		if (isCondenced) {
			int i2 = 0;
			IAEFluidStack[] fluids = new IAEFluidStack[removed];
			for (IAEFluidStack fluid : fluidStacks) {
				if (fluid != null) {
					fluids[i2] = fluid;
					i2++;
				}
			}
			int i3 = 0;
			IAEItemStack[] items = new IAEItemStack[requirements.length
				- removed];
			for (IAEItemStack item : returnStack) {
				if (item != null) {
					items[i3] = item;
					i3++;
				}
			}
			returnStack = items;
			this.fluidsCondensed = fluids;
		} else {
			this.fluids = fluidStacks;
		}
		return returnStack;
	}

	@Override
	public void setPriority(int priority) {
		this.pattern.setPriority(priority);
	}

	@Override
	public int hashCode() {
		return this.pattern.hashCode();
	}

}
