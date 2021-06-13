package com.the9grounds.aeadditions.util;

import com.the9grounds.aeadditions.AEAdditions;
import net.minecraftforge.common.config.Configuration;

import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.the9grounds.aeadditions.Constants;

public class AEAConfigHandler {

	Configuration config;

	public static boolean shortenedBuckets = true;
	public static boolean dynamicTypes = true;

	public AEAConfigHandler(Configuration configuration) {
		this.config = configuration;
	}

	public void reload() {
		shortenedBuckets = config.get("Tooltips", "shortenedBuckets", true, "Shall the guis shorten large mB values?").getBoolean(true);
		dynamicTypes = config.get("Storage Cells", "dynamicTypes", true, "Should the mount of bytes needed for a new type depend on the cellsize?").getBoolean(true);
		AEAdditions.integration.loadConfig(config);

		if (config.hasChanged()) {
			config.save();
		}
	}

	@SubscribeEvent
	public void onChangeConfig(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (!event.getModID().equals(Constants.MOD_ID)) {
			return;
		}
		reload();
	}
}
