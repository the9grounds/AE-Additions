package com.the9grounds.aeadditions.integration.opencomputers

import appeng.api.util.AEPartLocation
import appeng.tile.misc.TileInterface
import appeng.tile.misc.TileSecurityStation
import li.cil.oc.api.Network
import li.cil.oc.api.network.EnvironmentHost
import li.cil.oc.api.network.Visibility
import li.cil.oc.api.prefab.AbstractManagedEnvironment
import net.minecraft.tileentity.TileEntity

open class UpgradeAE(val envHost: EnvironmentHost) : NetworkControl<TileSecurityStation>() {

    init {
        setNode(Network.newNode(this, Visibility.Network).withConnector().withComponent("upgrade_me", Visibility.Network).create())
    }

    override fun tile(): TileSecurityStation {
        val security = getSecurity() ?: throw SecurityException("No Security Station")

        val node = security.getGridNode(AEPartLocation.INTERNAL) ?: throw SecurityException("No Security Station")

        val gridBlock = node.gridBlock ?: throw SecurityException("No Security Station")

        val location = gridBlock.location ?: throw SecurityException("No Security Station")

        val tileSecurity = location.world.getTileEntity(location.pos) as? TileSecurityStation ?: throw SecurityException("No Security Station")

        return tileSecurity
    }

    override fun host(): EnvironmentHost = envHost
}