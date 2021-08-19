package com.the9grounds.aeadditions.network

import appeng.api.storage.data.IItemList
import com.the9grounds.aeadditions.api.chemical.IAEChemicalStack
import com.the9grounds.aeadditions.integration.mekanism.chemical.AEChemicalStack
import com.the9grounds.aeadditions.network.packets.BasePacket
import com.the9grounds.aeadditions.util.StorageChannels
import io.netty.buffer.ByteBuf
import mekanism.api.chemical.ChemicalStack
import net.minecraft.network.PacketBuffer

class AEAPacketBuffer(wrapped: ByteBuf) : PacketBuffer(wrapped) {

    override fun readString(): String {
        return super.readString(BasePacket.MAX_STRING_LENGTH)
    }
    
    fun writeIAEChemicalStack(chemicalStack: IAEChemicalStack) {
        chemicalStack.writeToPacket(this)
    }
    
    fun writeChemicalStack(chemicalStack: ChemicalStack<*>) {
        chemicalStack.writeToPacket(this)
    }
    
    fun writeIAEChemicalStackList(chemicalStackList: IItemList<IAEChemicalStack>) {
        for (chemicalStack in chemicalStackList) {
            writeIAEChemicalStack(chemicalStack)
        }
    }
    
    fun readIAEChemicalStackList(): IItemList<IAEChemicalStack> {
        val list = StorageChannels.CHEMICAL.createList()
        
        while (readableBytes() > 0) {
            list.add(readIAEChemicalStack())
        }
        
        return list
    }
    
    fun readIAEChemicalStack(): IAEChemicalStack {
        return AEChemicalStack.fromPacket(this)
    }
}