package com.the9grounds.aeadditions.api.gas

import appeng.api.storage.data.IAEStack
import mekanism.api.chemical.ChemicalStack
import mekanism.api.chemical.Chemical

interface IAEChemicalStack : IAEStack<IAEChemicalStack> {
    /**
     * @return [ChemicalStack]
     */
    fun getChemicalStack(): ChemicalStack<*>

    /**
     * @return [Chemical]
     */
    fun getChemical(): Chemical<*>
}