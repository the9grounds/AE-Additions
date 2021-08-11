package com.the9grounds.aeadditions.client.gui.me.chemical

import appeng.api.config.SortDir
import appeng.api.config.SortOrder
import appeng.api.storage.data.IAEStack
import com.the9grounds.aeadditions.api.gas.IAEChemicalStack

object ChemicalSorters {
    fun getComparator(order: SortOrder?, dir: SortDir?): Comparator<IAEChemicalStack> {
        return Comparator.comparingLong(IAEChemicalStack::getStackSize)
    }
}