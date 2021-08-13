package com.the9grounds.aeadditions.integration.mekanism.chemical

import appeng.api.config.FuzzyMode
import appeng.api.storage.IStorageChannel
import com.the9grounds.aeadditions.api.gas.IAEChemicalStack
import com.the9grounds.aeadditions.integration.mekanism.Mekanism
import com.the9grounds.aeadditions.item.ChemicalDummyItem
import com.the9grounds.aeadditions.registries.Items
import com.the9grounds.aeadditions.util.StorageChannels
import mekanism.api.chemical.Chemical
import mekanism.api.chemical.ChemicalStack
import mekanism.api.chemical.ChemicalUtils
import mekanism.api.chemical.IChemicalHandler
import mekanism.api.chemical.gas.Gas
import mekanism.api.chemical.gas.GasStack
import mekanism.api.chemical.infuse.InfuseType
import mekanism.api.chemical.infuse.InfusionStack
import mekanism.api.chemical.pigment.Pigment
import mekanism.api.chemical.pigment.PigmentStack
import mekanism.api.chemical.slurry.Slurry
import mekanism.api.chemical.slurry.SlurryStack
import mekanism.common.util.ChemicalUtil
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.PacketBuffer
import java.util.*

class AEChemicalStack() : IAEChemicalStack {

    private var _stackSize = 0L
    private var _chemical: Chemical<*>? = null
    private var canCraft = false
    private var _countRequestable = 0L
    
    constructor(chemical: Chemical<*>): this() {
        _chemical = chemical
    }
    
    constructor(chemical: Chemical<*>, amount: Long): this() {
        _chemical = chemical
        _stackSize = amount
    }

    constructor(chemicalStack: ChemicalStack<*>?): this() {
        if (chemicalStack == null || chemicalStack.type == null) {
            throw IllegalArgumentException("Chemical is null")
        }

        _chemical = chemicalStack.type
        _stackSize = chemicalStack.amount
        canCraft = false
        _countRequestable = 0L
    }

    constructor(oldStack: AEChemicalStack) : this() {
        _chemical = oldStack.getChemical()
        canCraft = oldStack.isCraftable
        _stackSize = oldStack.stackSize
        _countRequestable = oldStack.countRequestable
    }

    constructor(nbt: CompoundNBT): this() {
        _chemical = Mekanism.readChemicalFromNbt(nbt)
        canCraft = nbt.getBoolean("isCraftable")
        _stackSize = nbt.getLong("stackSize")
        _countRequestable = nbt.getLong("countRequestable")
    }

    override fun getChemicalStack(): ChemicalStack<*> {
        return when(_chemical) {
            is Gas -> GasStack(_chemical as Gas, stackSize)
            is Slurry -> SlurryStack(_chemical as Slurry, stackSize)
            is InfuseType -> InfusionStack(_chemical as InfuseType, stackSize)
            is Pigment -> PigmentStack(_chemical as Pigment, stackSize)
            else -> throw RuntimeException("Received invalid chemical")
        }
    }

    override fun getChemical(): Chemical<*> {
        return _chemical!!
    }

    override fun add(itemStack: IAEChemicalStack?) {
        if (itemStack == null) {
            return
        }

        incStackSize(itemStack.stackSize)

        countRequestable += itemStack.countRequestable
        isCraftable = isCraftable || itemStack.isCraftable
    }

    override fun getStackSize(): Long = _stackSize

    override fun setStackSize(stackSize: Long): IAEChemicalStack {
        _stackSize = stackSize

        return this
    }

    override fun getCountRequestable(): Long = _countRequestable

    override fun setCountRequestable(countRequestable: Long): IAEChemicalStack {
        _countRequestable = countRequestable

        return this
    }

    override fun isCraftable(): Boolean = canCraft

    override fun setCraftable(isCraftable: Boolean): IAEChemicalStack {
        canCraft = isCraftable

        return this
    }

    override fun reset(): IAEChemicalStack {
        canCraft = false
        _stackSize = 0L
        _countRequestable = 0L

        return this
    }

    override fun isMeaningful(): Boolean = stackSize != 0L || countRequestable > 0 || isCraftable

    override fun incStackSize(i: Long) {
        _stackSize += i
    }

    override fun decStackSize(i: Long) {
        _stackSize -= i
    }

    override fun incCountRequestable(i: Long) {
        _countRequestable += i
    }

    override fun decCountRequestable(i: Long) {
        _countRequestable -= i
    }

    override fun writeToNBT(i: CompoundNBT) {
        _chemical!!.write(i)
        i.putString("chemicalType", Mekanism.getType(_chemical!!))
        i.putBoolean("isCraftable", isCraftable)
        i.putLong("stackSize", _stackSize)
        i.putLong("countRequestable", _countRequestable)
    }

    override fun fuzzyComparison(other: IAEChemicalStack?, mode: FuzzyMode?): Boolean = other != null && this.getChemical() == other.getChemical()

    override fun writeToPacket(data: PacketBuffer) {
        data.writeString(Mekanism.getType(_chemical!!))
        data.writeString(_chemical!!.getRegistryName().toString())
        data.writeBoolean(isCraftable)
        data.writeLong(stackSize)
        data.writeLong(countRequestable)
    }

    override fun copy(): IAEChemicalStack = AEChemicalStack(this)

    override fun empty(): IAEChemicalStack {
        val copy = copy()
        copy.reset()
        return copy
    }

    override fun getChannel(): IStorageChannel<IAEChemicalStack> = StorageChannels.CHEMICAL

    override fun asItemStackRepresentation(): ItemStack {
        val itemStack = ItemStack(Items.DUMMY_CHEMICAL_ITEM)
        
        if (!itemStack.isEmpty) {
            val item = itemStack.item as ChemicalDummyItem
            item.setChemicalStack(itemStack, getChemicalStack())
            return itemStack
        }
        
        return ItemStack.EMPTY
    }

    override fun getCapabilityForChemical(): Optional<IChemicalHandler<*, *>> {
        return asItemStackRepresentation().getCapability(ChemicalUtil.getCapabilityForChemical(_chemical)).resolve()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AEChemicalStack

        if (_chemical != other._chemical) return false
        if (canCraft != other.canCraft) return false
        if (_countRequestable != other._countRequestable) return false

        return true
    }

    override fun hashCode(): Int {
        return _chemical?.hashCode() ?: 0
    }

    companion object {
        fun fromPacket(packet: PacketBuffer): IAEChemicalStack {
            val chemical = Mekanism.readChemicalFromPacket(packet)
            val isCraftable = packet.readBoolean()
            val stackSize = packet.readLong()
            val countRequestable = packet.readLong()
            
            val chemicalStack = AEChemicalStack(chemical)
            chemicalStack.isCraftable = isCraftable
            chemicalStack.stackSize = stackSize
            chemicalStack.countRequestable = countRequestable
            
            return chemicalStack
        }
    }
    
    
    
}