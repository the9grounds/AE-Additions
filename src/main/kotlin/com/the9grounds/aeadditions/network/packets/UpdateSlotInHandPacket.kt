package com.the9grounds.aeadditions.network.packets

import com.the9grounds.aeadditions.network.AEAPacketBuffer
import io.netty.buffer.Unpooled
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.EquipmentSlotType
import net.minecraft.item.ItemStack

class UpdateSlotInHandPacket : BasePacket {
    var slot: EquipmentSlotType? = null
    var itemStack: ItemStack? = null
    
    constructor(packetBuffer: AEAPacketBuffer) {
        itemStack = packetBuffer.readItemStack()
        slot = EquipmentSlotType.fromString(packetBuffer.readString())
    }
    
    constructor(slotType: EquipmentSlotType, itemStack: ItemStack) {
        this.slot = slotType
        this.itemStack = itemStack
        
        val packet = AEAPacketBuffer(Unpooled.buffer())
        
        packet.writeItemStack(itemStack)
        packet.writeString(slot!!.getName())
        configureWrite(packet)
    }
    
    override fun clientPacketData(player: PlayerEntity?) {
        player!!.setItemStackToSlot(slot, itemStack)
    }
}