package com.the9grounds.aeadditions.network.packets

import com.the9grounds.aeadditions.container.chemical.ChemicalInterfaceContainer
import com.the9grounds.aeadditions.network.AEAPacketBuffer
import io.netty.buffer.Unpooled
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.CompoundNBT

class ChemicalInterfaceContentsChangedPacket: BasePacket {
    
    var chemicalTankList: ChemicalInterfaceContainer.ChemicalTankList? = null
    
    constructor(packet: AEAPacketBuffer) {
        val nbt = packet.readCompoundTag()
        
        val size = nbt!!.getInt("size")
        val list = ChemicalInterfaceContainer.ChemicalTankList(size)
        list.readFromNbt(nbt)
        
        chemicalTankList = list
    }
    
    constructor(chemicalTankList: ChemicalInterfaceContainer.ChemicalTankList) {
        this.chemicalTankList = chemicalTankList
        
        val packet = AEAPacketBuffer(Unpooled.buffer())
        packet.writeInt(getPacketId())
        
        val nbt = CompoundNBT()
        
        this.chemicalTankList!!.writeToNbt(nbt)
        
        packet.writeCompoundTag(nbt)
        
        configureWrite(packet)
    }

    override fun clientPacketData(player: PlayerEntity?) {
        val container = player!!.openContainer
        
        if (container is ChemicalInterfaceContainer) {
            container.onChemicalTankListChanged(this.chemicalTankList!!)
        }
    }
}