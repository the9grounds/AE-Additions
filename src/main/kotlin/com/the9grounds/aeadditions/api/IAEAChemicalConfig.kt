package com.the9grounds.aeadditions.api

import mekanism.api.chemical.Chemical
import net.minecraft.nbt.CompoundNBT

interface IAEAChemicalConfig: Iterable<Chemical<*>?> {
    fun setChemicalInSlot(slot: Int, chemical: Chemical<*>)
    
    fun getChemicalInSlot(slot: Int): Chemical<*>?
    
    fun hasChemicalInSlot(slot: Int): Boolean
    
    fun readFromNbt(nbt: CompoundNBT, name: String)
    
    fun writeToNbt(nbt: CompoundNBT, name: String)
}