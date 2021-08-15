package com.the9grounds.aeadditions.api.gas

import appeng.api.storage.data.IAEStack
import mekanism.api.chemical.ChemicalStack
import mekanism.api.chemical.Chemical
import mekanism.api.chemical.IChemicalHandler
import net.minecraft.item.ItemStack
import java.util.*
import kotlin.Comparator

interface IAEChemicalStack : IAEStack<IAEChemicalStack>, Comparable<IAEChemicalStack> {
    /**
     * @return [ChemicalStack]
     */
    fun getChemicalStack(): ChemicalStack<*>

    /**
     * @return [Chemical]
     */
    fun getChemical(): Chemical<*>
    
    fun getCapabilityForChemical(): Optional<IChemicalHandler<*, *>>
}