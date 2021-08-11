package com.the9grounds.aeadditions.client.gui.me.chemical

import appeng.api.config.SortDir
import appeng.api.config.SortOrder
import appeng.client.gui.me.common.Repo
import appeng.client.gui.widgets.IScrollSource
import appeng.client.gui.widgets.ISortSource
import com.the9grounds.aeadditions.api.gas.IAEChemicalStack
import java.util.Comparator
import java.util.regex.Pattern

class ChemicalRepo(src: IScrollSource?, sortSrc: ISortSource?): Repo<IAEChemicalStack>(src, sortSrc) {
    override fun matchesSearch(searchMode: SearchMode?, searchPattern: Pattern?, stack: IAEChemicalStack?): Boolean {
        val displayName = stack!!.getChemical().name
        
        return searchPattern!!.matcher(displayName).find()
    }

    override fun getComparator(sortBy: SortOrder?, sortDir: SortDir?): Comparator<in IAEChemicalStack> {
        return ChemicalSorters.getComparator(sortBy, sortDir)
    }
}