package com.the9grounds.aeadditions.integration.mekanism.chemical

import appeng.api.storage.data.IItemList
import com.the9grounds.aeadditions.api.chemical.IAEChemicalStack
import com.the9grounds.aeadditions.api.chemical.IChemicalStorageChannel
import mekanism.api.chemical.Chemical
import mekanism.api.chemical.ChemicalStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.PacketBuffer

class ChemicalStorageChannel : IChemicalStorageChannel {

    override fun getUnitsPerByte(): Int = 8000
    
    override fun createList(): IItemList<IAEChemicalStack> = ChemicalList()

    override fun createStack(input: Any): IAEChemicalStack? {
        return when(input) {
            is Chemical<*> -> AEChemicalStack(input, 1000L)
            is ChemicalStack<*> -> AEChemicalStack(input)
            is AEChemicalStack -> AEChemicalStack(input)
            else -> null
        }
    }

    override fun readFromPacket(input: PacketBuffer): IAEChemicalStack = AEChemicalStack.fromPacket(input)

    override fun createFromNBT(nbt: CompoundNBT): IAEChemicalStack = AEChemicalStack(nbt)
}