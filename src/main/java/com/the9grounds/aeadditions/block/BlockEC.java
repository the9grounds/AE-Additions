package com.the9grounds.aeadditions.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumBlockRenderType;

import com.the9grounds.aeadditions.models.IItemModelRegister;
import com.the9grounds.aeadditions.models.ModelManager;
import com.the9grounds.aeadditions.util.CreativeTabEC;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public abstract class BlockEC extends BlockContainer implements IItemModelRegister {

	protected BlockEC(Material material, float hardness, float resistance) {
		super(material);
		setHardness(hardness);
		setResistance(resistance);
		setCreativeTab(CreativeTabEC.INSTANCE);
	}

	protected BlockEC(Material material) {
		super(material);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public void registerModel(Item item, ModelManager manager) {
		manager.registerItemModel(item, 0);
	}
}
