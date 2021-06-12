package extracells.config

import extracells.registries.CellDefinition

interface ICellConfig {
    val size: Int
    val enabled: Boolean
    val numberOfTypes: Int
    val type: CellDefinition
    val name: String
}