package extracells.integration.opencomputers

import li.cil.oc.api.network.EnvironmentHost

object CompleteHelper {

    @JvmStatic fun getCompleteUpgradeAE(envHost: EnvironmentHost): UpgradeAE = UpgradeAEComplete(envHost)

    @JvmStatic fun getCompleteUpgradeAEClass(): Class<out UpgradeAE> = UpgradeAEComplete::class.java
}