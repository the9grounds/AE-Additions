package com.the9grounds.aeadditions.util

import com.the9grounds.aeadditions.blockentity.BaseMEWirelessTransceiverBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.Level

data class Channel(val channelInfo: ChannelInfo, var broadcaster: BaseMEWirelessTransceiverBlockEntity?, val subscribers: MutableList<BaseMEWirelessTransceiverBlockEntity>) {
    fun removeBlockEntity(blockEntity: BaseMEWirelessTransceiverBlockEntity) {
        if (broadcaster === blockEntity) {
            blockEntity.removedFromChannel(channelInfo)
            
            broadcaster = null
        }
        
        val subscriber = subscribers.find { it === blockEntity }
        
        if (subscriber != null) {
            subscriber.removedFromChannel(channelInfo)
            
            subscribers.remove(blockEntity)
        }
    }
    
    fun saveToNbt(): CompoundTag {
        val tag = CompoundTag()
        tag.putString("channelInfoId", channelInfo.id.toString())
        if (broadcaster != null) {
            val pos = broadcaster!!.blockPos
            tag.putIntArray("broadcaster", listOf(pos.x, pos.y, pos.z))
        }
        
        tag.putInt("subscriberSize", subscribers.size)
        
        subscribers.forEachIndexed { index, meWirelessTransceiverBlockEntity ->
            val pos = meWirelessTransceiverBlockEntity.blockPos
            tag.putIntArray("subscriber#$index", listOf(pos.x, pos.y, pos.z))
        }
        
        return tag
    }
    
    // Check if subscribers are removed and remove them from the array
    fun checkSubscribers() {
        subscribers.removeAll { it.isRemoved }
    }
    
    fun hasAccessTo(player: ServerPlayer): Boolean {
        return channelInfo.hasAccessTo(player)
    }
    
    companion object {
        fun loadFromNbt(tag: CompoundTag, channelInfos: List<ChannelInfo>, level: Level): Channel? {
            val channelInfo = channelInfos.find { it.id.toString() == tag.getString("channelInfoId") }
            
            val broadcasterPos = tag.getIntArray("broadcaster")
            var broadcaster: BaseMEWirelessTransceiverBlockEntity? = null
            
            if (!broadcasterPos.isEmpty()) {
                val blockEntity = level.getBlockEntity(BlockPos(broadcasterPos[0], broadcasterPos[1], broadcasterPos[2]))
                
                if (blockEntity is BaseMEWirelessTransceiverBlockEntity) {
                    broadcaster = blockEntity
                }
            }
            
            val subscriberSize = tag.getInt("subscriberSize")
            
            val subscriberList = mutableListOf<BaseMEWirelessTransceiverBlockEntity>()
            
            for (i in 0 until subscriberSize) {
                val intArray = tag.getIntArray("subscriber#$i")
                
                if (!intArray.isEmpty()) {
                    val blockEntity = level.getBlockEntity(BlockPos(intArray[0], intArray[1], intArray[2]))

                    if (blockEntity is BaseMEWirelessTransceiverBlockEntity) {
                        subscriberList.add(blockEntity)
                    }
                }
            }
            
            if (channelInfo === null) {
                return null
            }
            
            return Channel(channelInfo, broadcaster, subscriberList)
        }
    }
}
