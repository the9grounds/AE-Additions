package com.the9grounds.aeadditions.integration.mekanism

import mekanism.api.chemical.Chemical
import mekanism.api.chemical.ChemicalStack
import mekanism.api.chemical.gas.Gas
import mekanism.api.chemical.gas.GasStack
import mekanism.api.chemical.infuse.InfuseType
import mekanism.api.chemical.infuse.InfusionStack
import mekanism.api.chemical.pigment.Pigment
import mekanism.api.chemical.pigment.PigmentStack
import mekanism.api.chemical.slurry.Slurry
import mekanism.api.chemical.slurry.SlurryStack
import net.minecraft.nbt.CompoundNBT

object Mekanism {
    fun getType(chemical: Chemical<*>): String {
        return when(chemical) {
            is Gas -> "gas"
            is Pigment -> "pigment"
            is InfuseType -> "infusion"
            is Slurry -> "slurry"
            else -> throw RuntimeException("Invalid chemical type")
        }
    }
    
    fun readChemicalFromNbt(nbt: CompoundNBT): Chemical<*> {
        return when(nbt.getString("chemicalType")) {
            "gas" -> Gas.readFromNBT(nbt)
            "pigment" -> Pigment.readFromNBT(nbt)
            "infusion" -> InfuseType.readFromNBT(nbt)
            "slurry" -> Slurry.readFromNBT(nbt)
            else -> throw RuntimeException("Invalid chemical type")
        }
    }
    
    fun readChemicalStackFromNbt(nbt: CompoundNBT): ChemicalStack<*> {
        return when(nbt.getString("chemicalType")) {
            "gas" -> GasStack.readFromNBT(nbt)
            "pigment" -> PigmentStack.readFromNBT(nbt)
            "infusion" -> InfusionStack.readFromNBT(nbt)
            "slurry" -> SlurryStack.readFromNBT(nbt)
            else -> throw RuntimeException("Invalid chemical type")
        }
    }
}