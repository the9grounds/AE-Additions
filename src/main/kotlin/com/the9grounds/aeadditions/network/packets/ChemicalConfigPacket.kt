package com.the9grounds.aeadditions.network.packets

import com.the9grounds.aeadditions.api.container.IChemicalSyncContainer
import com.the9grounds.aeadditions.integration.mekanism.Mekanism
import com.the9grounds.aeadditions.network.AEAPacketBuffer
import io.netty.buffer.Unpooled
import mekanism.api.chemical.Chemical
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.CompoundNBT

class ChemicalConfigPacket : BasePacket {
    
    private var chemicalList: Array<Chemical<*>?> = arrayOf()
    private var windowId: Int = 0

    constructor(packet: AEAPacketBuffer) {
        val nbt = packet.readCompoundTag()
        
        val size = nbt!!.getInt("chemicalSize")
        
        val tempList = arrayOfNulls<Chemical<*>>(size)
        
        for (i in 0 until size) {
            if (nbt.contains("chemical#${i}")) {
                
                val childNbt = nbt.getCompound("chemical#${i}")
                
                val chemical = Mekanism.readChemicalFromNbt(childNbt)
                
                tempList[i] = chemical
            }
        }
        
        chemicalList = tempList
        
        windowId = nbt.getInt("windowId")
    }
    
    constructor(windowId: Int, chemicalList: Array<Chemical<*>?>) {
        this.windowId = windowId
        this.chemicalList = chemicalList
        
        val packet = AEAPacketBuffer(Unpooled.buffer())

        packet.writeInt(getPacketId())
        
        val nbt = CompoundNBT()
        
        nbt.putInt("chemicalSize", chemicalList.size)
        
        chemicalList.forEachIndexed { index, chemical -> 
            if (chemical == null) {
                return@forEachIndexed
            }
            
            val childNbt = CompoundNBT()
            
            Mekanism.writeChemicalWithTypeToNbt(childNbt, chemical)
            
            nbt.put("chemical#${index}", childNbt)
        }
        
        nbt.putInt("windowId", windowId)
        
        packet.writeCompoundTag(nbt)
        configureWrite(packet)
    }

    override fun clientPacketData(player: PlayerEntity?) {
        val container = player!!.openContainer
        
        if (container is IChemicalSyncContainer) {
            container.receiveChemicals(chemicalList)
        }
    }
}