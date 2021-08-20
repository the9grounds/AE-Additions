package com.the9grounds.aeadditions.parts

import appeng.api.networking.GridFlags
import appeng.api.networking.events.MENetworkChannelsChanged
import appeng.api.networking.events.MENetworkEventSubscribe
import appeng.api.networking.events.MENetworkPowerStatusChange
import appeng.me.GridAccessException
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketBuffer

abstract class AbstractBasicStatePart(itemStack: ItemStack) : AEABasePart(itemStack) {
    
    companion object {
        protected val POWERED_FLAG = 1
        protected val CHANNEL_FLAG = 2
    }

    private var clientFlags = 0
    
    init {
        proxy.setFlags(GridFlags.REQUIRE_CHANNEL)
    }
    
    @MENetworkEventSubscribe
    fun channelRender(event: MENetworkChannelsChanged) {
        host!!.markForUpdate()
    }
    
    @MENetworkEventSubscribe
    fun powerRender(event: MENetworkPowerStatusChange) {
        host!!.markForUpdate()
    }

    override fun writeToStream(data: PacketBuffer?) {
        super.writeToStream(data)
        
        clientFlags = 0
        
        try {
            if (proxy.energy.isNetworkPowered) {
                clientFlags = clientFlags.or(POWERED_FLAG)
            }
            
            if (proxy.node.meetsChannelRequirements()) {
                clientFlags = clientFlags.or(CHANNEL_FLAG)
            }
        } catch (e: GridAccessException) {
            //
        }
        
        data!!.writeByte(clientFlags)
    }

    override fun readFromStream(data: PacketBuffer?): Boolean {
        val eh =  super.readFromStream(data)
        
        
        val old = clientFlags
        
        clientFlags = data!!.readByte().toInt()
        
        return eh || old != clientFlags
    }
    
    val isPowered: Boolean
    get() = clientFlags.and(POWERED_FLAG) == POWERED_FLAG
    
    val isActive: Boolean
    get() = clientFlags.and(CHANNEL_FLAG) == CHANNEL_FLAG
}