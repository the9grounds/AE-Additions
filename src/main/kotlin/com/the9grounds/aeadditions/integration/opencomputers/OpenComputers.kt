package com.the9grounds.aeadditions.integration.opencomputers

import appeng.api.AEApi
import com.the9grounds.aeadditions.integration.Integration
import com.the9grounds.aeadditions.item.ItemOCUpgrade
import li.cil.oc.api.Driver
import li.cil.oc.api.driver.*

object OpenComputers {

    @JvmStatic fun init() {
        add(DriverOreDictExportBus())
        add(DriverFluidInterface())

        if (Integration.Mods.MEKANISMGAS.isEnabled) {
            add(DriverGasExportBus())
            add(DriverGasImportBus())
        }

        // TODO: Re-enable when fixed
//        add(ItemOCUpgrade)
//        AEApi.instance().registries().wireless().registerWirelessHandler(WirelessHandlerUpgradeAE)
        AEAdditionsPathProvider
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