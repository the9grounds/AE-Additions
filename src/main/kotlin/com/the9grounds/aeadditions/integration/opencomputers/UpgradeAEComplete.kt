package com.the9grounds.aeadditions.integration.opencomputers

import appeng.api.util.AEPartLocation
import appeng.tile.misc.TileSecurityStation
import li.cil.oc.api.machine.Arguments
import li.cil.oc.api.machine.Context
import li.cil.oc.api.network.EnvironmentHost
import li.cil.oc.integration.appeng.NetworkControl
//NetworkControl<TileSecurityStation>,
//li.cil.oc.integration.ec.NetworkControl<TileSecurityStation>
class UpgradeAEComplete(environmentHost: EnvironmentHost) : UpgradeAE(environmentHost) {
//    override fun pos(): AEPartLocation = AEPartLocation.INTERNAL
}