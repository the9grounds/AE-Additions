package extracells.integration.appeng

import extracells.api.ECApi

object AppEng {
    @JvmStatic fun init() {
        ECApi.instance().registerWrenchHandler(WrenchHandler)
    }
}