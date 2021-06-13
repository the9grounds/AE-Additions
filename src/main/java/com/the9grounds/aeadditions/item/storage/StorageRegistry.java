package com.the9grounds.aeadditions.item.storage;

import com.google.common.collect.ImmutableList;
import com.the9grounds.aeadditions.config.CellConfig;
import com.the9grounds.aeadditions.registries.CellDefinition;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class StorageRegistry implements Iterable<StorageType> {

	public ImmutableList<StorageType> types;
	public String name;

	private StorageRegistry(ImmutableList<StorageType> types, String name) {
		this.types = types;
		this.name = name;
	}

	@Override
	public Iterator<StorageType> iterator() {
		return types.iterator();
	}

	public StorageType fromMeta(int meta) {
		if (meta >= types.size()) {
			return types.get(0);
		}
		return types.get(meta);
	}

	public static class Builder {
		private List<StorageType> types;
		private String name;

		public Builder(String name) {
			this.types = new LinkedList<>();
			this.name = name;
		}

//		public void add(CellDefinition definition, int... sizes) {
//			for (int size : sizes) {
//				add(definition, size);
//			}
//		}
//
//		public void add(CellDefinition definition, Iterable<Integer> sizes) {
//			for (int size : sizes) {
//				add(definition, size);
//			}
//		}

		public void add(CellDefinition definition, String cellName, CellConfig config) {
			types.add(new StorageType(definition, types.size(), cellName, config.getEnabled(), config.getNumberOfTypes(), config.getSize(), name));
		}

		public int size() {
			return types.size();
		}

		public StorageRegistry build() {
			return new StorageRegistry(ImmutableList.copyOf(types), name);
		}
	}
}
