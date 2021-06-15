package com.the9grounds.aeadditions.integration.opencomputers

import appeng.api.util.AEPartLocation
import appeng.tile.misc.TileInterface
import appeng.tile.misc.TileSecurityStation
import li.cil.oc.api.Network
import li.cil.oc.api.network.EnvironmentHost
import li.cil.oc.api.network.Node
import li.cil.oc.api.network.Visibility
import li.cil.oc.api.prefab.AbstractManagedEnvironment
import net.minecraft.tileentity.TileEntity

open class UpgradeAE(val envHost: EnvironmentHost) : AppEngNetworkControl(), NetworkControl<TileSecurityStation> {

    var node: Node?

    init {
        node = Network.newNode(this, Visibility.Network).withConnector().withComponent("upgrade_me", Visibility.Network).create()
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
    override var isActive = false
    override fun node(): Node? = node

    override fun onConnect(p0: Node?) {
        TODO("Not yet implemented")
    }

    override fun onDisconnect(p0: Node?) {
        TODO("Not yet implemented")
    }

    override fun canUpdate(): Boolean {
        TODO("Not yet implemented")
    }
}