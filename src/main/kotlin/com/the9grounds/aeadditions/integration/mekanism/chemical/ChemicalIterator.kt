package com.the9grounds.aeadditions.integration.mekanism.chemical

import com.the9grounds.aeadditions.api.gas.IAEChemicalStack

class ChemicalIterator<T: IAEChemicalStack>(val parent: MutableIterator<T>) : MutableIterator<T> {
    
    private var next: T? = null
    
    override fun hasNext(): Boolean {
        while (parent.hasNext()) {
            next = parent.next()
            
            if (next!!.isMeaningful) {
                return true
            }
            
            parent.remove()
        }
        
        next = null
        
        return false
    }

    override fun next(): T {
        if (next == null) {
            throw NoSuchElementException()
        }
        
        return next!!
    }

    override fun remove() {
        parent.remove()
    }
}