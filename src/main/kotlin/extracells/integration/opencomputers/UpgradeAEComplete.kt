package extracells.integration.opencomputers

import appeng.api.util.AEPartLocation
import appeng.tile.misc.TileSecurityStation
import li.cil.oc.api.network.EnvironmentHost
import li.cil.oc.integration.appeng.NetworkControl

class UpgradeAEComplete(environmentHost: EnvironmentHost) : UpgradeAE(environmentHost), NetworkControl<TileSecurityStation>, li.cil.oc.integration.ec.NetworkControl<TileSecurityStation> {
    override fun pos(): AEPartLocation = AEPartLocation.INTERNAL
}