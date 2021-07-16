package com.the9grounds.aeadditions.block;

import appeng.api.config.SecurityPermissions;
import com.the9grounds.aeadditions.api.IWrenchHandler;
import com.the9grounds.aeadditions.util.PermissionUtil;
import com.the9grounds.aeadditions.util.TileUtil;
import com.the9grounds.aeadditions.util.WrenchUtil;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import appeng.api.networking.IGridNode;
import appeng.api.util.AEPartLocation;
import com.the9grounds.aeadditions.container.ContainerVibrationChamberFluid;
import com.the9grounds.aeadditions.gui.GuiVibrationChamberFluid;
import com.the9grounds.aeadditions.network.GuiHandler;
import com.the9grounds.aeadditions.tileentity.TileEntityVibrationChamberFluid;

public class BlockVibrationChamberFluid extends BlockAE implements IGuiBlock {

	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyBool STATE = PropertyBool.create("state");

	public BlockVibrationChamberFluid() {
		super(Material.IRON, 2.0F, 10.0F);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			return true;
		}

		ItemStack current = player.getHeldItem(hand);
		if (!player.isSneaking()) {
			TileEntity tile = world.getTileEntity(pos);
			if (!PermissionUtil.hasPermission(player, SecurityPermissions.BUILD, tile)) {
				return false;
			}
			RayTraceResult rayTraceResult = new RayTraceResult(new Vec3d(hitX, hitY, hitZ), side, pos);
			IWrenchHandler wrenchHandler = WrenchUtil.getHandler(current, player, rayTraceResult, hand);
			if (wrenchHandler != null) {
				spawnAsEntity(world, pos, new ItemStack(this));
				world.setBlockToAir(pos);
				wrenchHandler.wrenchUsed(current, player, rayTraceResult, hand);
				return true;
			}
		}

		GuiHandler.launchGui(0, player, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityVibrationChamberFluid();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Object getClientGuiElement(EntityPlayer player, World world, BlockPos pos) {
		TileEntity tileEntity = world.getTileEntity(pos);
		if (tileEntity != null && tileEntity instanceof TileEntityVibrationChamberFluid) {
			return new GuiVibrationChamberFluid(player, (TileEntityVibrationChamberFluid) tileEntity);
		}
		return null;
	}

	@Override
	public Object getServerGuiElement(EntityPlayer player, World world, BlockPos pos) {
		TileEntity tileEntity = world.getTileEntity(pos);
		if (tileEntity != null && tileEntity instanceof TileEntityVibrationChamberFluid) {
			return new ContainerVibrationChamberFluid(player.inventory, (TileEntityVibrationChamberFluid) tileEntity);
		}
		return null;
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		this.setDefaultFacing(worldIn, pos, state);
	}

	private void setDefaultFacing(World worldIn, BlockPos pos, IBlockState state) {
		if (!worldIn.isRemote) {
			IBlockState iblockstate = worldIn.getBlockState(pos.north());
			IBlockState iblockstate1 = worldIn.getBlockState(pos.south());
			IBlockState iblockstate2 = worldIn.getBlockState(pos.west());
			IBlockState iblockstate3 = worldIn.getBlockState(pos.east());
			EnumFacing enumfacing = state.getValue(FACING);

			if (enumfacing == EnumFacing.NORTH && iblockstate.isFullBlock() && !iblockstate1.isFullBlock()) {
				enumfacing = EnumFacing.SOUTH;
			} else if (enumfacing == EnumFacing.SOUTH && iblockstate1.isFullBlock() && !iblockstate.isFullBlock()) {
				enumfacing = EnumFacing.NORTH;
			} else if (enumfacing == EnumFacing.WEST && iblockstate2.isFullBlock() && !iblockstate3.isFullBlock()) {
				enumfacing = EnumFacing.EAST;
			} else if (enumfacing == EnumFacing.EAST && iblockstate3.isFullBlock() && !iblockstate2.isFullBlock()) {
				enumfacing = EnumFacing.WEST;
			}

			worldIn.setBlockState(pos, state.withProperty(FACING, enumfacing), 2);
		}
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
		if (worldIn.isRemote)
			return;
		TileUtil.setOwner(worldIn, pos, placer);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.VALUES[meta];

		if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
			enumfacing = EnumFacing.NORTH;
		}

		return this.getDefaultState().withProperty(FACING, enumfacing);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getIndex();
	}

	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if (tile instanceof TileEntityVibrationChamberFluid) {
			TileEntityVibrationChamberFluid vibrationChamberFluid = (TileEntityVibrationChamberFluid) tile;
			state = state.withProperty(STATE, vibrationChamberFluid.burnTime > 0);
		}
		return state;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, STATE);
	}

	@Override
	public void onPlayerDestroy(World world, BlockPos pos, IBlockState state) {
		if (world.isRemote) {
			return;
		}
		TileEntity tile = world.getTileEntity(pos);
		if (tile != null) {
			if (tile instanceof TileEntityVibrationChamberFluid) {
				IGridNode node = ((TileEntityVibrationChamberFluid) tile).getGridNode(AEPartLocation.INTERNAL);
				if (node != null) {
					node.destroy();
				}
			}
		}
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (worldIn.isRemote){
			super.breakBlock(worldIn, pos, state);
			return;
		}
		TileUtil.destroy(worldIn, pos);

		super.breakBlock(worldIn, pos, state);
	}
}
