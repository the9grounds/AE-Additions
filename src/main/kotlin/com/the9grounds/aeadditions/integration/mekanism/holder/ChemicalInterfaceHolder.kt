package com.the9grounds.aeadditions.integration.mekanism.holder

import com.the9grounds.aeadditions.tile.ChemicalInterfaceTileEntity
import mekanism.api.chemical.Chemical
import mekanism.api.chemical.ChemicalStack
import mekanism.api.chemical.IChemicalTank
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder
import net.minecraft.util.Direction

class ChemicalInterfaceHolder<CHEMICAL: Chemical<CHEMICAL>, CHEMICALSTACK: ChemicalStack<CHEMICAL>, TANK: IChemicalTank<CHEMICAL, CHEMICALSTACK>>(val chemicalInterfaceTile: ChemicalInterfaceTileEntity, val chemicalClass: Class<*>): IChemicalTankHolder<CHEMICAL, CHEMICALSTACK, TANK> {
    override fun getTanks(p0: Direction?): MutableList<TANK> {

        val list = mutableListOf<TANK>()
        if (p0 != null) {
            val tank = chemicalInterfaceTile.getTankForSide(p0)
            if (tank != null) {
                list.add(tank as TANK)
            }
        }
        
        return list
    }
}