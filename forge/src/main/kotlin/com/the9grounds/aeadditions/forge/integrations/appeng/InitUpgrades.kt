package com.the9grounds.aeadditions.forge.integrations.appeng

import appeng.api.upgrades.Upgrades
import appeng.core.definitions.AEItems

object InitUpgrades {
    fun init() {
        val group = "gui.ae.StorageCells";
        for (cell in listOf(Items.ITEM_STORAGE_CELL_1024k, Items.ITEM_STORAGE_CELL_4096k, Items.ITEM_STORAGE_CELL_16384k, Items.ITEM_STORAGE_CELL_65536k)) {
            Upgrades.add(AEItems.FUZZY_CARD, cell, 1, group)
            Upgrades.add(AEItems.INVERTER_CARD, cell, 1, group)
            Upgrades.add(AEItems.EQUAL_DISTRIBUTION_CARD, cell, 1, group)
            Upgrades.add(AEItems.VOID_CARD, cell, 1, group)
        }
        
        for (cell in listOf(Items.FLUID_STORAGE_CELL_1024k, Items.FLUID_STORAGE_CELL_4096k, Items.FLUID_STORAGE_CELL_16384k)) {
            Upgrades.add(AEItems.FUZZY_CARD, cell, 1, group)
            Upgrades.add(AEItems.INVERTER_CARD, cell, 1, group)
            Upgrades.add(AEItems.EQUAL_DISTRIBUTION_CARD, cell, 1, group)
            Upgrades.add(AEItems.VOID_CARD, cell, 1, group)
        }
    }
}