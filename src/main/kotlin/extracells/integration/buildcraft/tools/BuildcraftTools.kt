package extracells.integration.buildcraft.tools

import extracells.api.ECApi

object BuildcraftTools {
    @JvmStatic fun init() {
        ECApi.instance().registerWrenchHandler(WrenchHandler)
    }
}