package com.the9grounds.aeadditions.proxy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.the9grounds.aeadditions.Constants;
import com.the9grounds.aeadditions.integration.Integration;
import com.the9grounds.aeadditions.tileentity.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;

import appeng.api.AEApi;
import appeng.api.IAppEngApi;
import appeng.api.movable.IMovableRegistry;
import appeng.api.recipes.IRecipeHandler;
import appeng.api.recipes.IRecipeLoader;
import com.the9grounds.aeadditions.network.PacketHandler;
import com.the9grounds.aeadditions.registries.BlockEnum;
import com.the9grounds.aeadditions.registries.ItemEnum;
import com.the9grounds.aeadditions.util.FuelBurnTime;
import com.the9grounds.aeadditions.util.recipe.RecipeUniversalTerminal;

public class CommonProxy {

	public CommonProxy(){
		MinecraftForge.EVENT_BUS.register(this);
	}

	private class ExternalRecipeLoader implements IRecipeLoader {

		@Override
		public BufferedReader getFile(String path) throws Exception {
			return new BufferedReader(new FileReader(new File(path)));
		}
	}

	private class InternalRecipeLoader implements IRecipeLoader {

		@Override
		public BufferedReader getFile(String path) throws Exception {
			InputStream resourceAsStream = getClass().getResourceAsStream("/assets/" + Constants.MOD_ID + "/aerecipes/" + path);
			InputStreamReader reader = new InputStreamReader(resourceAsStream, "UTF-8");
			return new BufferedReader(reader);
		}
	}

	public void addRecipes(File configFolder) {
		IRecipeHandler recipeHandler = AEApi.instance().registries().recipes().createNewRecipehandler();
		File externalRecipe = new File(configFolder.getPath() + File.separator + "AppliedEnergistics2" + File.separator + "com.the9grounds.aeadditions.recipe");
		if (externalRecipe.exists()) {
			recipeHandler.parseRecipes(new ExternalRecipeLoader(), externalRecipe.getPath());
		} else {
			recipeHandler.parseRecipes(new InternalRecipeLoader(), "main.recipe");
		}
		recipeHandler.injectRecipes();
	}

	public void registerBlocks() {
		for (BlockEnum current : BlockEnum.values()) {
			if (current.getEnabled()) {
				registerBlock(current.getBlock());
				registerItem(current.getItem());
				current.registerUpgrades();
			}
		}
	}

	@SubscribeEvent
	public void onRegister(RegistryEvent.Register<IRecipe> event){
		RecipeUniversalTerminal.INSTANCE.setRegistryName("UniversalCraftingTerminalRecipe");
		event.getRegistry().register(RecipeUniversalTerminal.INSTANCE);
	}

	public void registerItems() {
		for (ItemEnum current : ItemEnum.values()) {
			if (current.shouldRegister()) {
				registerItem(current.getItem());
			}
		}
	}

	public void registerBlock(Block block) {
		ForgeRegistries.BLOCKS.register(block);
	}

	public void registerItem(Item item) {
		ForgeRegistries.ITEMS.register(item);
	}

	public void registerMovables() {
		IAppEngApi api = AEApi.instance();
		IMovableRegistry movable = api.registries().movable();
		movable.whiteListTileEntity(TileEntityCertusTank.class);
		movable.whiteListTileEntity(TileEntityWalrus.class);
		movable.whiteListTileEntity(TileEntityFluidCrafter.class);
		movable.whiteListTileEntity(TileEntityFluidInterface.class);
		movable.whiteListTileEntity(TileEntityFluidFiller.class);
		movable.whiteListTileEntity(TileEntityHardMeDrive.class);
		movable.whiteListTileEntity(TileEntityVibrationChamberFluid.class);
		if (Integration.Mods.MEKANISMGAS.isEnabled()) {
			movable.whiteListTileEntity(TileEntityGasInterface.class);
		}
	}

	public void registerRenderers() {
		// Only Client Side
	}

	public void registerModels() {
		// Only Client Side
	}

	public void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityCertusTank.class, "tileEntityCertusTank");
		GameRegistry.registerTileEntity(TileEntityWalrus.class, "tileEntityWalrus");
		GameRegistry.registerTileEntity(TileEntityFluidCrafter.class, "tileEntityFluidCrafter");
		GameRegistry.registerTileEntity(TileEntityFluidInterface.class, "tileEntityFluidInterface");
		GameRegistry.registerTileEntity(TileEntityFluidFiller.class, "tileEntityFluidFiller");
		GameRegistry.registerTileEntity(TileEntityHardMeDrive.class, "tileEntityHardMEDrive");
		GameRegistry.registerTileEntity(TileEntityVibrationChamberFluid.class, "tileEntityVibrationChamberFluid");

		if (Integration.Mods.MEKANISMGAS.isEnabled()) {
			GameRegistry.registerTileEntity(TileEntityGasInterface.class, "tileEntityGasInterface");
		}
	}

	public void registerFluidBurnTimes() {
		FuelBurnTime.registerFuel(FluidRegistry.LAVA, 800);
	}

	public boolean isClient() {
		return false;
	}

	public boolean isServer() {
		return true;
	}

	public void registerPackets() {
		PacketHandler.registerServerPackets();
	}
}
