package com.the9grounds.aeadditions.tileentity;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

public class TileEntityCertusTank extends TileBase {

	public TileEntityCertusTank(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	private FluidStack lastBeforeUpdate = null;
	public FluidTank tank = new FluidTank(32000) {

		@Override
		public FluidTank readFromNBT(CompoundNBT nbt) {
			if (!nbt.contains("Empty")) {
				FluidStack fluid = FluidStack.loadFluidStackFromNBT(nbt);
				setFluid(fluid);
			} else {
				setFluid(null);
			}
			return this;
		}
	};

	@NotNull
	@Override
	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @org.jetbrains.annotations.Nullable Direction side) {
		if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return (LazyOptional<T>) LazyOptional.of(() -> new FluidHandler());
		}
		return super.getCapability(cap, side);
	}

	public void compareAndUpdate() {
		if (!this.world.isRemote) {
			FluidStack current = this.tank.getFluid();

			if (current != null) {
				if (this.lastBeforeUpdate != null) {
					if (Math.abs(current.getAmount() - this.lastBeforeUpdate.getAmount()) >= 500) {
						updateBlock();
						this.lastBeforeUpdate = current.copy();
					} else if (this.lastBeforeUpdate.getAmount() < this.tank
						.getCapacity()
						&& current.getAmount() == this.tank.getCapacity()
						|| this.lastBeforeUpdate.getAmount() == this.tank
						.getCapacity()
						&& current.getAmount() < this.tank.getCapacity()) {
						updateBlock();
						this.lastBeforeUpdate = current.copy();
					}
				} else {
					updateBlock();
					this.lastBeforeUpdate = current.copy();
				}
			} else if (this.lastBeforeUpdate != null) {
				updateBlock();
				this.lastBeforeUpdate = null;
			}
		}
	}

	/* Multiblock stuff */
	public FluidStack drain(FluidStack fluid, IFluidHandler.FluidAction fluidAction,
		boolean findMainTank) {
		if (findMainTank) {
			int yOff = 0;
			TileEntity offTE = this.world.getTileEntity(pos);
			TileEntityCertusTank mainTank = this;
			while (true) {
				if (offTE != null && offTE instanceof TileEntityCertusTank) {
					Fluid offFluid = ((TileEntityCertusTank) offTE).getFluid();
					if (offFluid != null && offFluid == fluid.getFluid()) {
						mainTank = (TileEntityCertusTank) this.world
							.getTileEntity(pos.up(yOff));
						yOff++;
						offTE = this.world.getTileEntity(pos.up(yOff));
						continue;
					}
				}
				break;
			}

			return mainTank != null ? mainTank.drain(fluid, fluidAction, false)
				: null;
		}

		FluidStack drained = this.tank.drain(fluid.getAmount(), fluidAction);
		compareAndUpdate();

		if (drained == null || drained.getAmount() < fluid.getAmount()) {
			TileEntity offTE = this.world.getTileEntity(pos.down());
			if (offTE instanceof TileEntityCertusTank) {
				TileEntityCertusTank tank = (TileEntityCertusTank) offTE;
				FluidStack externallyDrained = tank.drain(new FluidStack(
						fluid.getFluid(), fluid.getAmount()
						- (drained != null ? drained.getAmount() : 0)),
						fluidAction, false);

				if (externallyDrained != null) {
					return new FluidStack(fluid.getFluid(),
						(drained != null ? drained.getAmount() : 0)
							+ externallyDrained.getAmount());
				} else {
					return drained;
				}
			}
		}

		return drained;
	}

	public int fill(FluidStack fluid, IFluidHandler.FluidAction fluidAction, boolean findMainTank) {
		if (findMainTank) {
			int yOff = 0;
			TileEntity offTE = this.world.getTileEntity(pos);
			TileEntityCertusTank mainTank = this;
			while (true) {
				if (offTE != null && offTE instanceof TileEntityCertusTank) {
					Fluid offFluid = ((TileEntityCertusTank) offTE).getFluid();
					if (offFluid == null || offFluid == fluid.getFluid()) {
						mainTank = (TileEntityCertusTank) this.world
							.getTileEntity(pos.down(yOff));
						yOff++;
						offTE = this.world.getTileEntity(pos.down(yOff));
						continue;
					}
				}
				break;
			}

			return mainTank != null ? mainTank.fill(fluid, fluidAction, false) : 0;
		}

		int filled = this.tank.fill(fluid, fluidAction);
		compareAndUpdate();

		if (filled < fluid.getAmount()) {
			TileEntity offTE = this.world.getTileEntity(pos.up());
			if (offTE instanceof TileEntityCertusTank) {
				TileEntityCertusTank tank = (TileEntityCertusTank) offTE;
				return filled
					+ tank.fill(new FluidStack(fluid.getFluid(), fluid.getAmount()
					- filled), fluidAction, false);
			}
		}

		return filled;
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return writeToNBT(new CompoundNBT());
	}

	public Fluid getFluid() {
		FluidStack tankFluid = this.tank.getFluid();
		return tankFluid != null && tankFluid.getAmount() > 0 ? tankFluid.getFluid()
			: null;
	}

	public Fluid getRenderFluid() {
		return this.tank.getFluid() != null ? this.tank.getFluid().getFluid()
			: null;
	}

	public float getRenderScale() {
		return (float) this.tank.getFluidAmount() / this.tank.getCapacity();
	}

	public IFluidTankProperties[] getTankInfo(boolean goToMainTank) {
		if (!goToMainTank) {
			return this.tank.getTankProperties();
		}

		int amount = 0, capacity = 0;
		Fluid fluid = null;

		int yOff = 1;
		TileEntity offTE = this.world.getTileEntity(pos.offset(EnumFacing.DOWN, yOff));
		TileEntityCertusTank mainTank = this;
		while (true) {
			if (offTE != null && offTE instanceof TileEntityCertusTank) {
				if (((TileEntityCertusTank) offTE).getFluid() == null || getFluid() == null
					|| ((TileEntityCertusTank) offTE).getFluid() == getFluid()) {
					mainTank = (TileEntityCertusTank) this.world
						.getTileEntity(pos.offset(EnumFacing.DOWN, yOff));
					yOff++;
					offTE = this.world.getTileEntity(pos.offset(EnumFacing.DOWN, yOff));
					continue;
				}
			}
			break;
		}

		BlockPos posBaseTank = pos.offset(EnumFacing.DOWN, yOff - 1);

		yOff = 0;
		offTE = this.world.getTileEntity(posBaseTank.offset(EnumFacing.UP, yOff));
		while (true) {
			if (offTE != null && offTE instanceof TileEntityCertusTank) {
				mainTank = (TileEntityCertusTank) offTE;
				if (mainTank.getFluid() == null || getFluid() == null
					|| mainTank.getFluid() == getFluid()) {
					IFluidTankProperties info = mainTank.getTankInfo(false)[0];
					if (info != null) {
						capacity += info.getCapacity();
						if (info.getContents() != null) {
							amount += info.getContents().amount;
							if (info.getContents().getFluid() != null) {
								fluid = info.getContents().getFluid();
							}
						}
					}
					yOff++;
					offTE = this.world.getTileEntity(posBaseTank.offset(EnumFacing.UP, yOff));
					continue;
				}
			}
			break;
		}

		return new IFluidTankProperties[]{new FluidTankProperties(fluid != null ? new FluidStack(fluid, amount) : null, capacity)};
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		super.onDataPacket(net, pkt);
		world.markBlockRangeForRenderUpdate(pos, pos);
	}

	@Override
	public void readFromNBT(CompoundNBT tag) {
		super.readFromNBT(tag);
		readFromNBTWithoutCoords(tag);
	}

	public void readFromNBTWithoutCoords(CompoundNBT tag) {
		this.tank.readFromNBT(tag);
	}

	public void setFluid(FluidStack fluidStack) {
		tank.setFluid(fluidStack);
	}

	@Override
	public CompoundNBT writeToNBT(CompoundNBT tag) {
		super.writeToNBT(tag);
		writeToNBTWithoutCoords(tag);
		return tag;
	}

	public void writeToNBTWithoutCoords(NBTTagCompound tag) {
		this.tank.writeToNBT(tag);
	}

	private class FluidHandler implements net.minecraftforge.fluids.capability.IFluidHandler {
		@Override
		public IFluidTankProperties[] getTankProperties() {
			return TileEntityCertusTank.this.getTankInfo(true);
		}

		@Override
		public int fill(FluidStack resource, boolean doFill) {
			if (resource == null || tank.getFluid() != null
				&& resource.getFluid() != tank.getFluid().getFluid()) {
				return 0;
			}
			markDirty();
			return TileEntityCertusTank.this.fill(resource, doFill, true);
		}

		@Nullable
		@Override
		public FluidStack drain(FluidStack resource, boolean doDrain) {
			if (tank.getFluid() == null || resource == null
				|| resource.getFluid() != tank.getFluid().getFluid()) {
				return null;
			}
			markDirty();
			return TileEntityCertusTank.this.drain(resource, doDrain, true);
		}

		@Nullable
		@Override
		public FluidStack drain(int maxDrain, boolean doDrain) {
			if (tank.getFluid() == null) {
				return null;
			}
			markDirty();
			return drain(new FluidStack(tank.getFluid(), maxDrain), doDrain);
		}
	}
}