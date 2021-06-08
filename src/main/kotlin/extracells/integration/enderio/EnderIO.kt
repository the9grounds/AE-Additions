package extracells.integration.enderio

import extracells.api.ECApi

object EnderIO {

    @JvmStatic fun init() {
        ECApi.instance().registerWrenchHandler(WrenchHandler)
    }
}