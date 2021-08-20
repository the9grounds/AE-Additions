package com.the9grounds.aeadditions.container.chemical

import appeng.api.config.SecurityPermissions
import appeng.api.storage.ITerminalHost
import com.the9grounds.aeadditions.api.IUpgradeableHost
import com.the9grounds.aeadditions.container.AbstractUpgradableContainer
import com.the9grounds.aeadditions.container.ContainerTypeBuilder
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.container.ContainerType

class ChemicalIOContainer(
    type: ContainerType<*>?,
    id: Int,
    playerInventory: PlayerInventory,
    host: Any
) : AbstractUpgradableContainer<ChemicalIOContainer>(type, id, playerInventory, host as IUpgradeableHost) {
    
    companion object {
        val EXPORT_BUS = ContainerTypeBuilder(ITerminalHost::class) { containerType: ContainerType<ChemicalIOContainer>, windowId, playerInventory, host ->
            ChemicalIOContainer(containerType, windowId, playerInventory!!, host)
        }.requirePermission(SecurityPermissions.BUILD).build("chemical_export_bus")

        val IMPORT_BUS = ContainerTypeBuilder(ITerminalHost::class) { containerType: ContainerType<ChemicalIOContainer>, windowId, playerInventory, host ->
            ChemicalIOContainer(containerType, windowId, playerInventory!!, host)
        }.requirePermission(SecurityPermissions.BUILD).build("chemical_import_bus")
    }

    override fun setupConfig() {
        this.setupUpgrades()
    }


}