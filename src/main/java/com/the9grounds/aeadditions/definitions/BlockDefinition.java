package com.the9grounds.aeadditions.definitions;

import appeng.api.definitions.ITileDefinition;
import com.the9grounds.aeadditions.api.definitions.IBlockDefinition;
import com.the9grounds.aeadditions.registries.BlockEnum;
import com.the9grounds.aeadditions.tileentity.*;

public class BlockDefinition implements IBlockDefinition {

	public static final BlockDefinition instance = new BlockDefinition();

//	@Override
//	public ITileDefinition blockInterface() {
//		return new BlockItemDefinitions(BlockEnum.ECBASEBLOCK.getBlock(),
//			TileEntityFluidInterface.class);
//	}

	@Override
	public ITileDefinition certusTank() {
		return new BlockItemDefinitions(BlockEnum.CERTUSTANK.getBlock(),
			TileEntityCertusTank.class);
	}

	@Override
	public ITileDefinition fluidCrafter() {
		return new BlockItemDefinitions(BlockEnum.FLUIDCRAFTER.getBlock(),
			TileEntityFluidCrafter.class);
	}

	@Override
	public ITileDefinition fluidFiller() {
		return new BlockItemDefinitions(BlockEnum.FLUIDCRAFTER.getBlock(), 1,
			TileEntityFluidFiller.class);
	}

	@Override
	public ITileDefinition craftingStorage256() {
		return new BlockItemDefinitions(BlockEnum.UPGRADEDCRAFTINGSTORAGE256.getBlock(), 1, TileEntityCraftingStorage.class);
	}

	@Override
	public ITileDefinition craftingStorage1024() {
		return new BlockItemDefinitions(BlockEnum.UPGRADEDCRAFTINGSTORAGE1024.getBlock(), 1, TileEntityCraftingStorage.class);
	}

	@Override
	public ITileDefinition craftingStorage4096() {
		return new BlockItemDefinitions(BlockEnum.UPGRADEDCRAFTINGSTORAGE4096.getBlock(), 1, TileEntityCraftingStorage.class);
	}

	@Override
	public ITileDefinition craftingStorage16384() {
		return new BlockItemDefinitions(BlockEnum.UPGRADEDCRAFTINGSTORAGE16384.getBlock(), 1, TileEntityCraftingStorage.class);
	}

	//	@Override
//	public ITileDefinition walrus() {
//		return new BlockItemDefinitions(BlockEnum.WALRUS.getBlock(),
//			TileEntityWalrus.class);
//	}

}
