package com.the9grounds.aeadditions.integration.mekanism.chemical

import appeng.api.config.FuzzyMode
import appeng.api.storage.data.IItemList
import com.the9grounds.aeadditions.api.gas.IAEChemicalStack

class ChemicalList : IItemList<IAEChemicalStack> {
    
    val records = mutableMapOf<IAEChemicalStack, IAEChemicalStack>()
    
    override fun add(option: IAEChemicalStack?) {
        if (option == null) {
            return
        }

        val stack = records[option]

        if (stack != null) {
            stack.add(option)

            return
        }

        val toAdd = option.copy()

        records[toAdd] = toAdd
    }

    override fun findPrecise(i: IAEChemicalStack?): IAEChemicalStack? {
        if (i == null) {
            return null
        }
        return records[i]
    }

    override fun findFuzzy(input: IAEChemicalStack?, fuzzy: FuzzyMode?): MutableCollection<IAEChemicalStack?> {
        if (input == null) {
            return mutableListOf()
        }
        
        return mutableListOf(findPrecise(input))
    }

    override fun isEmpty(): Boolean = !iterator().hasNext()

    override fun iterator(): MutableIterator<IAEChemicalStack> = ChemicalIterator(records.values.iterator())

    override fun addStorage(option: IAEChemicalStack?) {
        if (option == null) {
            return
        }

        val stack = records[option]

        if (stack != null) {
            stack.add(option)

            return
        }

        val toAdd = option.copy()

        records[toAdd] = toAdd
    }

    override fun addCrafting(option: IAEChemicalStack?) {
        if (option == null) {
            return
        }
        
        val stack = records[option]
        
        if (stack != null) {
            stack.isCraftable = true
            
            return
        }
        
        val toAdd = option.copy()
        toAdd.stackSize = 0
        toAdd.isCraftable = true
        
        records[toAdd] = toAdd
    }

    override fun addRequestable(option: IAEChemicalStack?) {
        if (option == null) {
            return
        }

        val stack = records[option]

        if (stack != null) {
            stack.countRequestable += option.countRequestable

            return
        }

        val toAdd = option.copy()
        toAdd.stackSize = 0
        toAdd.isCraftable = false
        toAdd.countRequestable = option.countRequestable

        records[toAdd] = toAdd
    }

    override fun getFirstItem(): IAEChemicalStack? = this.firstOrNull()

    override fun size(): Int = records.values.size

    override fun resetStatus() {
        this.forEach { 
            it.reset()
        }
    }
}