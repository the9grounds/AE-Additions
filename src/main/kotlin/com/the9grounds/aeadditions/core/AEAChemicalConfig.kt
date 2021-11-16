package com.the9grounds.aeadditions.core

import com.the9grounds.aeadditions.api.IAEAChemicalConfig
import com.the9grounds.aeadditions.integration.mekanism.Mekanism
import mekanism.api.chemical.Chemical
import net.minecraft.nbt.CompoundNBT

class AEAChemicalConfig(override val size: Int) : IAEAChemicalConfig {
    
    private val _config = arrayOfNulls<Chemical<*>?>(size)

    override fun getConfig(): Array<Chemical<*>?> = _config
    
    override fun setChemicalInSlot(slot: Int, chemical: Chemical<*>?) {
        _config[slot] = chemical
    }

    override fun getChemicalInSlot(slot: Int): Chemical<*>? = _config[slot]

    override fun hasChemicalInSlot(slot: Int): Boolean = _config[slot] != null

    override fun readFromNbt(nbt: CompoundNBT, name: String) {
        if (nbt.contains(name)) {
            val compound = nbt.getCompound(name)
            for (i in 0 until size) {
                if (compound.contains("config#${i}")) {
                    val chemicalNbt = compound.getCompound("config#${i}")

                    val chemical = Mekanism.readChemicalFromNbt(chemicalNbt)

                    _config[i] = chemical
                }
            }
        }
    }

    override fun writeToNbt(nbt: CompoundNBT, name: String) {
        val compound = CompoundNBT()

        for (i in 0 until size) {
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

    override fun iterator(): Iterator<Chemical<*>?> = _config.iterator()
}