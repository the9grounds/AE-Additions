package com.the9grounds.aeadditions.core

enum class TickRates(var min: Int, var max: Int) {
    ChemicalImportBus(5, 40),
    ChemicalExportBus(5, 60);
    
    val defaultMin = min
    val defaultMax = max
}