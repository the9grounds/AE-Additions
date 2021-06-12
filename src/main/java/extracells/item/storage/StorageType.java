package extracells.item.storage;

import extracells.registries.CellDefinition;

public class StorageType {
	private int meta;
	private String name;
	private int size;
	private int bytes;
	private Boolean enabled;
	private int numberOfTypes;
	private String identifier;
	private CellDefinition definition;
	private String storageName;

	public StorageType(CellDefinition definition, int meta, String name, Boolean enabled, int numberOfTypes, int size, String storageName) {
		this.size = size;
		this.bytes = size * 1024;
		this.name = name;
		this.enabled = enabled;
		this.numberOfTypes = numberOfTypes;
//		this.identifier = definition + "." + size + "k";
		this.identifier = definition + ".dynamic";
		this.definition = definition;
		this.meta = meta;
		this.storageName = storageName;
	}

	public int getBytes() {
		return bytes;
	}

	public int getSize() {
		return size;
	}

	public String getIdentifier() {
		return identifier;
	}

	public int getMeta() {
		return meta;
	}

	public String getName() {
		return name;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public int getNumberOfTypes() {
		return numberOfTypes;
	}

	public CellDefinition getDefinition() {
		return definition;
	}

	public String getModelName() {
		return "storage/" + definition + "/" + storageName + "/" + Integer.parseInt(name) + "k";
	}
}
