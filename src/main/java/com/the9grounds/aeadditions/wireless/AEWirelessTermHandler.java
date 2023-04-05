package com.the9grounds.aeadditions.wireless;

import appeng.core.sync.GuiBridge;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import appeng.api.features.IWirelessTermHandler;
import appeng.api.util.IConfigManager;
import com.the9grounds.aeadditions.api.IWirelessGasFluidTermHandler;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class AEWirelessTermHandler implements IWirelessTermHandler {

	@Override
	public boolean canHandle(ItemStack is) {
		IWirelessGasFluidTermHandler handler = WirelessTermRegistry
			.getWirelessTermHandler(is);
		if (handler == null) {
			return false;
		}
		return !handler.isItemNormalWirelessTermToo(is);
	}

	@Override
	public IConfigManager getConfigManager(ItemStack is) {
		return new ConfigManager();
	}

	public IGuiHandler getGuiHandler(ItemStack itemStack){ return GuiBridge.GUI_WIRELESS_TERM;
	}

	@Override
	public String getEncryptionKey(ItemStack item) {
		IWirelessGasFluidTermHandler handler = WirelessTermRegistry
			.getWirelessTermHandler(item);
		if (handler == null) {
			return null;
		}
		return handler.getEncryptionKey(item);
	}

	@Override
	public boolean hasPower(EntityPlayer player, double amount, ItemStack is) {
		IWirelessGasFluidTermHandler handler = WirelessTermRegistry
			.getWirelessTermHandler(is);
		if (handler == null) {
			return false;
		}
		return handler.hasPower(player, amount, is);
	}

	@Override
	public void setEncryptionKey(ItemStack item, String encKey, String name) {
		IWirelessGasFluidTermHandler handler = WirelessTermRegistry.getWirelessTermHandler(item);
		if (handler == null) {
			return;
		}
		handler.setEncryptionKey(item, encKey, name);
	}

	@Override
	public boolean usePower(EntityPlayer player, double amount, ItemStack is) {
		IWirelessGasFluidTermHandler handler = WirelessTermRegistry.getWirelessTermHandler(is);
		if (handler == null) {
			return false;
		}
		return handler.usePower(player, amount, is);
	}

}
