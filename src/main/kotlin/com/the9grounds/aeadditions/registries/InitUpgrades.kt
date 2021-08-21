package com.the9grounds.aeadditions.registries

import appeng.api.config.Upgrades

object InitUpgrades {
    
    fun init () {
        val group = "gui.aeadditions.chemicalIO"
        
        Upgrades.SPEED.registerItem(Parts.CHEMICAL_EXPORT_BUS, 4, group)
        Upgrades.CAPACITY.registerItem(Parts.CHEMICAL_EXPORT_BUS, 2, group)
        Upgrades.REDSTONE.registerItem(Parts.CHEMICAL_EXPORT_BUS, 1, group)

        Upgrades.SPEED.registerItem(Parts.CHEMICAL_IMPORT_BUS, 4, group)
        Upgrades.CAPACITY.registerItem(Parts.CHEMICAL_IMPORT_BUS, 2, group)
        Upgrades.REDSTONE.registerItem(Parts.CHEMICAL_IMPORT_BUS, 1, group)
    }
}