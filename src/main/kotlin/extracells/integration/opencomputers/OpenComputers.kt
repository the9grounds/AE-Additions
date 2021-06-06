package extracells.integration.opencomputers

import appeng.api.AEApi
import extracells.integration.Integration
import extracells.item.ItemOCUpgrade
import li.cil.oc.api.Driver
import li.cil.oc.api.driver.*

object OpenComputers {

    @JvmStatic fun init() {
        add(DriverFluidExportBus())
        add(DriverFluidImportBus())
        add(DriverOreDictExportBus())
        add(DriverFluidInterface())

        if (Integration.Mods.MEKANISMGAS.isEnabled) {
            add(DriverGasExportBus())
            add(DriverGasImportBus())
        }

        add(ItemOCUpgrade)
        AEApi.instance().registries().wireless().registerWirelessHandler(WirelessHandlerUpgradeAE)
        ExtraCellsPathProvider
    }

    fun add(provider: Any) {
        when(provider) {
            is EnvironmentProvider -> Driver.add(provider)
            is DriverItem -> Driver.add(provider)
            is DriverBlock -> Driver.add(provider)
            is Converter -> Driver.add(provider)
            is InventoryProvider -> Driver.add(provider)
        }
    }
}