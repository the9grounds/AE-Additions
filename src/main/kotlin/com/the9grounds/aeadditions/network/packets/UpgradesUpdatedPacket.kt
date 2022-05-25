package com.the9grounds.aeadditions.network.packets

import appeng.api.config.Upgrades
import com.the9grounds.aeadditions.container.AbstractUpgradableContainer
import com.the9grounds.aeadditions.network.AEAPacketBuffer
import io.netty.buffer.Unpooled
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.CompoundNBT

class UpgradesUpdatedPacket : BasePacket {
    var upgrades = mutableMapOf<Upgrades, Int>()
    
    constructor(packet: AEAPacketBuffer) {
        val nbt = packet.readCompoundTag()
        
        for (upgrade in Upgrades.values()) {
            val upgradeNbt = nbt!!.getCompound("${upgrade.ordinal}")
            
            this.upgrades[upgrade] = upgradeNbt.getInt("amount")
        }
    }
    
    constructor(upgrades: Map<Upgrades, Int>) {
        this.upgrades = upgrades.toMutableMap()
        
        val packet = AEAPacketBuffer(Unpooled.buffer())
        
        packet.writeInt(getPacketId())
        val nbt = CompoundNBT()
        
        for (upgrade in Upgrades.values()) {
            val compoundNBT = CompoundNBT()
            
            compoundNBT.putInt("amount", upgrades[upgrade]!!)
            
            nbt.put("${upgrade.ordinal}", compoundNBT)
        }
        
        packet.writeCompoundTag(nbt)
        
        configureWrite(packet)
    }

    override fun clientPacketData(player: PlayerEntity?) {
        val container = player!!.openContainer
        
        if (container is AbstractUpgradableContainer<*>) {
            container.updateUpgrades(this.upgrades)
        }
    }
}