package com.the9grounds.aeadditions.api.container

import mekanism.api.chemical.Chemical

interface IChemicalSyncContainer {
    fun receiveChemicals(chemicals: Array<Chemical<*>?>)
    
    fun setChemicalForSlot(chemical: Chemical<*>?, slot: Int)
}