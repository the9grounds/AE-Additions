package com.the9grounds.aeadditions.config

import com.the9grounds.aeadditions.registries.CellDefinition

interface ICellConfig {
    val size: Int
    val enabled: Boolean
    val numberOfTypes: Int
    val type: CellDefinition
    val name: String
}