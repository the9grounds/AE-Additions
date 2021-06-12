package extracells.config

import extracells.registries.CellDefinition

class CellConfig(override val size: Int, override val enabled: Boolean, override val name: String, override val numberOfTypes: Int, override val type: CellDefinition
) : ICellConfig {
}