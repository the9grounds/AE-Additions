package com.the9grounds.aeadditions.item

import com.the9grounds.aeadditions.integration.mekanism.Mekanism
import mekanism.api.chemical.ChemicalStack
import mekanism.api.chemical.gas.GasStack
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.NonNullList

class ChemicalDummyItem(props: Properties) : Item(props) {

    fun getChemicalStack(itemStack: ItemStack): ChemicalStack<*> {
        if (itemStack.hasTag()) {
            return Mekanism.readChemicalStackFromNbt(itemStack.tag!!)
        }
        
        // Default
        return GasStack.EMPTY
    }

    fun setChemicalStack(itemStack: ItemStack, chemicalStack: ChemicalStack<*>) {
        if (chemicalStack.isEmpty()) {
            itemStack.tag = null
        } else {
            val nbt = CompoundNBT()
            
            val type = Mekanism.getType(chemicalStack.getType())
            
            chemicalStack.write(nbt)
            nbt.putString("chemicalType", type)
            itemStack.tag = nbt
        }
    }

    override fun fillItemGroup(group: ItemGroup, items: NonNullList<ItemStack>) {
        // Do nothing
    }
}