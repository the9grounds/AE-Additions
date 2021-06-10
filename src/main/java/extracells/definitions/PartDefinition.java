package extracells.definitions;

import appeng.api.definitions.IItemDefinition;
import extracells.api.definitions.IPartDefinition;
import extracells.registries.ItemEnum;
import extracells.registries.PartEnum;

public class PartDefinition implements IPartDefinition {

	public static final PartDefinition instance = new PartDefinition();

	@Override
	public IItemDefinition partBattery() {
		return new ItemItemDefinitions(ItemEnum.PARTITEM.getItem(),
			PartEnum.BATTERY.ordinal());
	}

	@Override
	public IItemDefinition partConversionMonitor() {
		return new ItemItemDefinitions(ItemEnum.PARTITEM.getItem(),
			PartEnum.GASCONVERSIONMONITOR.ordinal());
	}

	@Override
	public IItemDefinition partDrive() {
		return new ItemItemDefinitions(ItemEnum.PARTITEM.getItem(),
			PartEnum.DRIVE.ordinal());
	}

	@Override
	public IItemDefinition partGasImportBus() {
		return new ItemItemDefinitions(ItemEnum.PARTITEM.getItem(),
			PartEnum.GASIMPORT.ordinal());
	}

	@Override
	public IItemDefinition partGasExportBus() {
		return new ItemItemDefinitions(ItemEnum.PARTITEM.getItem(),
				PartEnum.GASEXPORT.ordinal());
	}

	@Override
	public IItemDefinition partGasLevelEmitter() {
		return new ItemItemDefinitions(ItemEnum.PARTITEM.getItem(),
			PartEnum.GASLEVELEMITTER.ordinal());
	}

	@Override
	public IItemDefinition partGasStorageBus() {
		return new ItemItemDefinitions(ItemEnum.PARTITEM.getItem(),
			PartEnum.GASSTORAGE.ordinal());
	}

	@Override
	public IItemDefinition partGasTerminal() {
		return new ItemItemDefinitions(ItemEnum.PARTITEM.getItem(),
			PartEnum.GASTERMINAL.ordinal());
	}

//	@Override
//	public IItemDefinition partInterface() {
//		return new ItemItemDefinitions(ItemEnum.PARTITEM.getItem(),
//			PartEnum.INTERFACE.ordinal());
//	}

	@Override
	public IItemDefinition partOreDictExportBus() {
		return new ItemItemDefinitions(ItemEnum.PARTITEM.getItem(),
			PartEnum.OREDICTEXPORTBUS.ordinal());
	}

	@Override
	public IItemDefinition partStorageMonitor() {
		return new ItemItemDefinitions(ItemEnum.PARTITEM.getItem(),
			PartEnum.GASMONITOR.ordinal());
	}

}
