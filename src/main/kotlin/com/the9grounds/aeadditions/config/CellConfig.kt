package com.the9grounds.aeadditions.config

import com.the9grounds.aeadditions.registries.CellDefinition

class CellConfig(override val size: Int, override val enabled: Boolean, override val name: String, override val numberOfTypes: Int, override val type: CellDefinition
) : ICellConfig {
}