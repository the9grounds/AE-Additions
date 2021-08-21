package com.the9grounds.aeadditions.network.packets

import com.the9grounds.aeadditions.api.container.IChemicalSyncContainer
import com.the9grounds.aeadditions.integration.mekanism.Mekanism
import com.the9grounds.aeadditions.network.AEAPacketBuffer
import io.netty.buffer.Unpooled
import mekanism.api.chemical.Chemical
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.CompoundNBT

class ChemicalConfigChangedPacket : BasePacket {
    
    var chemical: Chemical<*>? = null
    var slot: Int = -1
    
    constructor(packet: AEAPacketBuffer) {
        val nbt = packet.readCompoundTag()!!
        
        val hasChemical = nbt.getBoolean("hasChemical")
        
        if (hasChemical) {
            this.chemical = Mekanism.readChemicalFromNbt(nbt)
        }
        
        this.slot = nbt.getInt("slot")
    }
    
    constructor(chemical: Chemical<*>?, slot: Int) {
        this.chemical = chemical
        this.slot = slot
        
        val packet = AEAPacketBuffer(Unpooled.buffer())

        packet.writeInt(getPacketId())
        
        val nbt = CompoundNBT()
        
        nbt.putBoolean("hasChemical", chemical != null)
        if (chemical != null) {
            Mekanism.writeChemicalWithTypeToNbt(nbt, chemical)
        }
        nbt.putInt("slot", slot)
        
        packet.writeCompoundTag(nbt)
        
        configureWrite(packet)
    }

    override fun serverPacketData(player: PlayerEntity?) {
        
        val container = player!!.openContainer
        
        if (container is IChemicalSyncContainer) {
            container.setChemicalForSlot(chemical, slot)
        }
    }
    
}