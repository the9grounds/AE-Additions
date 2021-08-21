package com.the9grounds.aeadditions.container.chemical

import appeng.api.config.SecurityPermissions
import com.the9grounds.aeadditions.api.IAEAChemicalConfig
import com.the9grounds.aeadditions.api.IAEAHasChemicalConfig
import com.the9grounds.aeadditions.api.IUpgradeableHost
import com.the9grounds.aeadditions.container.ContainerTypeBuilder
import com.the9grounds.aeadditions.parts.chemical.ChemicalExportBus
import com.the9grounds.aeadditions.parts.chemical.ChemicalImportBus
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.container.ContainerType
import net.minecraft.util.text.StringTextComponent

class ChemicalIOContainer(
    type: ContainerType<*>?,
    id: Int,
    playerInventory: PlayerInventory,
    host: Any
) : AbstractChemicalConfigurableContainer<ChemicalIOContainer>(type, id, playerInventory, host as IUpgradeableHost) {
    
    init {
        bindPlayerInventory(7, 101)
    }
    
    companion object {
        val EXPORT_BUS = ContainerTypeBuilder(ChemicalExportBus::class) { containerType: ContainerType<ChemicalIOContainer>, windowId, playerInventory, host ->
            ChemicalIOContainer(containerType, windowId, playerInventory!!, host)
        }.requirePermission(SecurityPermissions.BUILD).apply { titleComponent = StringTextComponent("Chemical Export Bus") }.build("chemical_export_bus")

        val IMPORT_BUS = ContainerTypeBuilder(ChemicalImportBus::class) { containerType: ContainerType<ChemicalIOContainer>, windowId, playerInventory, host ->
            ChemicalIOContainer(containerType, windowId, playerInventory!!, host)
        }.requirePermission(SecurityPermissions.BUILD).apply { titleComponent = StringTextComponent("Chemical Import Bus") }.build("chemical_import_bus")
    }

    override fun setupConfig() {
        this.setupUpgrades()
    }

    override val chemicalConfig: IAEAChemicalConfig
        get() = (part as IAEAHasChemicalConfig).chemicalConfig
}