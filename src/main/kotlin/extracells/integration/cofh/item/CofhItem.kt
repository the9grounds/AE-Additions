package extracells.integration.cofh.item

import extracells.api.ECApi

object CofhItem {
    @JvmStatic fun init() {
        ECApi.instance().registerWrenchHandler(WrenchHandler)
    }
}