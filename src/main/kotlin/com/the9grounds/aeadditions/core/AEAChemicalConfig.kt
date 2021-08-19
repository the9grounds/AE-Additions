package com.the9grounds.aeadditions.core

import com.the9grounds.aeadditions.api.IAEAChemicalConfig
import com.the9grounds.aeadditions.integration.mekanism.Mekanism
import mekanism.api.chemical.Chemical
import net.minecraft.nbt.CompoundNBT

class AEAChemicalConfig(private val size: Int) : IAEAChemicalConfig {
    
    val config = arrayOfNulls<Chemical<*>>(size)
    
    override fun setChemicalInSlot(slot: Int, chemical: Chemical<*>) {
        config[slot] = chemical
    }

    override fun getChemicalInSlot(slot: Int): Chemical<*>? = config[slot]

    override fun hasChemicalInSlot(slot: Int): Boolean = config[slot] != null

    override fun readFromNbt(nbt: CompoundNBT, name: String) {
        if (nbt.contains(name)) {
            val compound = nbt.getCompound(name)
            for (i in 0..size) {
                if (compound.contains("config#${i}")) {
                    val chemicalNbt = compound.getCompound("config#${i}")

                    val chemical = Mekanism.readChemicalFromNbt(chemicalNbt)

                    config[i] = chemical
                }
            }
        }
    }

    override fun writeToNbt(nbt: CompoundNBT, name: String) {
        val compound = CompoundNBT()

        for (i in 0..size) {
            val chemical = getChemicalInSlot(i)

            if (chemical != null) {
                val chemicalNbt = CompoundNBT()

                chemical.write(chemicalNbt)

                chemicalNbt.putString("chemicalType", Mekanism.getType(chemical))

                compound.put("config#${i}", chemicalNbt)
            }
        }

        nbt.put(name, compound)
    }

    override fun iterator(): Iterator<Chemical<*>?> = config.iterator()
}