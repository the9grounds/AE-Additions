package com.the9grounds.aeadditions.config

import com.the9grounds.aeadditions.Constants
import com.the9grounds.aeadditions.item.storage.StorageRegistry
import com.the9grounds.aeadditions.registries.CellDefinition
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.fml.client.event.ConfigChangedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class AEAConfiguration(private val configuration: Configuration) {
    val cellEntries = mutableMapOf<CellDefinition, Map<Int, CellConfig>>()

    companion object {
        @JvmField var components: StorageRegistry? = null
    }

    fun createCellsAndComponentEntries() {
        val componentBuilder = StorageRegistry.Builder("components")

        for (cellDef in CellDefinition.values()) {
            registerSpecificCellEntries(componentBuilder, cellDef)
        }

        components = componentBuilder.build()
    }

    fun registerSpecificCellEntries(componentBuilder: StorageRegistry.Builder, cellDefinition: CellDefinition) {
        val entries = cellEntries[cellDefinition]
        val cellBuilder = StorageRegistry.Builder("cells")
        cellDefinition.componentMetaStart = componentBuilder.size()

        entries!!.forEach { (k, v) ->
            componentBuilder.add(cellDefinition, k.toString(), v)
            cellBuilder.add(cellDefinition, k.toString(), v)
        }

        cellDefinition.cells = cellBuilder.build()
    }

    fun reload() {
        for (enum in CellDefinition.values()) {
            val entries = mutableMapOf<Int, CellConfig>()
            for (cell in enum.sizes) {
                val category = "%s.%d".format(enum.configCategory, cell)

                configuration.addCustomCategoryComment(enum.configCategory, "Do not edit the keys as the keys point to model & texture definitions")
                val size = configuration.get(category, "size", cell, "Size in K").getInt()
                val enabled = configuration.get(category, "enabled", true).getBoolean()
                val numberOfTypes = configuration.get(category, "numberOfTypes", enum.defaultNumberOfTypes).getInt()

                entries[cell] = CellConfig(size, enabled, cell.toString(), numberOfTypes, enum)
            }

            cellEntries.set(enum, entries.toMap())
        }

        if (configuration.hasChanged()) {
            configuration.save()
        }
    }

    @SubscribeEvent
    fun onChangeConfig(event: ConfigChangedEvent.OnConfigChangedEvent) {
        if (event.modID.equals(Constants.MOD_ID)) {
            return
        }

        reload()
    }
}