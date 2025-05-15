package com.the9grounds.aeadditions

import com.the9grounds.aeadditions.core.BaseAEAConfig
import dev.architectury.injectables.annotations.ExpectPlatform

object Config {
    @ExpectPlatform
    @JvmStatic
    fun getConfig(): BaseAEAConfig {
        throw AssertionError()
    }
}