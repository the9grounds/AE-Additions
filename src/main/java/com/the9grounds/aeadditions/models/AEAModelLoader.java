package com.the9grounds.aeadditions.models;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

import com.the9grounds.aeadditions.Constants;

public class AEAModelLoader implements ICustomModelLoader {
	private final Map<String, IModel> builtInModels;

	public AEAModelLoader(Map<String, IModel> builtInModels) {
		this.builtInModels = ImmutableMap.copyOf(builtInModels);
	}

	@Override
	public boolean accepts(ResourceLocation modelLocation) {
		if (!modelLocation.getNamespace().equals(Constants.MOD_ID)) {
			return false;
		}

		return builtInModels.containsKey(modelLocation.getPath());
	}

	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception {
		return builtInModels.get(modelLocation.getPath());
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		for (IModel model : builtInModels.values()) {
			if (model instanceof IResourceManagerReloadListener) {
				((IResourceManagerReloadListener) model).onResourceManagerReload(resourceManager);
			}
		}
	}
}
