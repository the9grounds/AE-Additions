package extracells.integration.mekanism

import extracells.api.ECApi
import extracells.integration.Integration

object Mekanism {

    @JvmStatic fun init() {
        if (Integration.Mods.MEKANISMGAS.isEnabled) {
            ECApi.instance().addExternalStorageInterface(HandlerMekanismGasTank)
        }

        ECApi.instance().registerWrenchHandler(WrenchHandler)
    }
}