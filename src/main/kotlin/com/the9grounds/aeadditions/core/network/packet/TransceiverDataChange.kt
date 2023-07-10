package com.the9grounds.aeadditions.core.network.packet

import com.the9grounds.aeadditions.core.network.NetworkManager
import com.the9grounds.aeadditions.menu.MEWirelessTransceiverMenu
import com.the9grounds.aeadditions.registries.Capability
import com.the9grounds.aeadditions.util.ChannelInfo
import net.minecraft.client.Minecraft
import net.minecraft.core.Registry
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraftforge.fml.loading.FMLEnvironment
import net.minecraftforge.fml.util.thread.SidedThreadGroups
import net.minecraftforge.server.ServerLifecycleHooks

class TransceiverDataChange: BasePacket {
    
    var currentChannel : ChannelInfo? = null
    var subscribe = false
    var channelUsage = 0
    var maxChannels = 0
    
    constructor(packet: FriendlyByteBuf) {
        val resourceLocation = packet.readResourceLocation()
        subscribe = packet.readBoolean()
        channelUsage = packet.readInt()
        maxChannels = packet.readInt()
        val nbt = packet.readNbt()
        val level = if (FMLEnvironment.dist.isClient) {
            Minecraft.getInstance().level
        } else{
            ServerLifecycleHooks.getCurrentServer()?.getLevel(ResourceKey.create(Registry.DIMENSION_REGISTRY, resourceLocation))
        }
        currentChannel = ChannelInfo.readFromNbt(nbt!!, level!!)
    }
    
    constructor(subscribe: Boolean, level: Level, channelInfo: ChannelInfo) {
        val data = this.getInitialData()
        
        data.writeResourceLocation(level.dimension().location())
        data.writeBoolean(subscribe)
        
        if (Thread.currentThread().threadGroup == SidedThreadGroups.CLIENT) {
            data.writeInt(0)
            data.writeInt(0)
        } else {
            if (level.getCapability(Capability.CHANNEL_HOLDER).resolve().isPresent) {
                val channelHolder = level.getCapability(Capability.CHANNEL_HOLDER).resolve().get()

                val channel = channelHolder.getOrCreateChannel(channelInfo)

                if (channel.broadcaster != null && channel.broadcaster?.mainNode?.node?.isActive == true) {
                    data.writeInt(channel.broadcaster?.mainNode?.node?.usedChannels ?: 0)
                    data.writeInt(channel.broadcaster?.mainNode?.node?.maxChannels ?: 0)
                } else {
                    data.writeInt(0)
                    data.writeInt(0)
                }
            } else {
                data.writeInt(0)
                data.writeInt(0)
            }
        }
        
        val nbt = channelInfo.saveToNbt()
        data.writeNbt(nbt)
        
        configureWrite(data)
    }

    override fun serverPacketData(player: Player) {
        val containerMenu = player.containerMenu
        
        if (containerMenu !is MEWirelessTransceiverMenu) {
            return
        }
        
        val blockEntity = containerMenu.blockEntity!!

        var channelHolder = player.level.getCapability(Capability.CHANNEL_HOLDER).resolve().get()

        if (!channelHolder.channelInfos.contains(currentChannel!!)) {
            return
        }
        
        // Verify has access to channel
        
        if (!currentChannel!!.hasAccessTo(player as ServerPlayer)) {
            return
        }
        
        if (subscribe) {
            blockEntity.subscribeToChannel(currentChannel!!)
        } else {
            blockEntity.broadcastToChannel(currentChannel!!)
        }
        
        val packet = TransceiverDataChange(subscribe, player.level, currentChannel!!)

        val server = ServerLifecycleHooks.getCurrentServer()

        server?.playerList?.players?.forEach {
            val containerMenu = it.containerMenu
            if (containerMenu is MEWirelessTransceiverMenu && containerMenu.blockEntity!! == blockEntity) {
                NetworkManager.sendTo(packet, it)
            }
        }
    }

    override fun clientPacketData(player: Player?) {
        val containerMenu = player!!.containerMenu
        if (containerMenu is MEWirelessTransceiverMenu) {
            containerMenu.isCurrentlySubscribing = subscribe
            
            if (!containerMenu.isCurrentlySubscribing) {
                containerMenu.isSubscriber = false
                containerMenu.screen?.typeButton?.isSubscriber = false
            }
            
            containerMenu.currentChannel = this.currentChannel
            containerMenu.isOnChannel = currentChannel != null
            containerMenu.ae2ChannelUsage = channelUsage
            containerMenu.ae2MaxChannels = maxChannels
            
            if (this.currentChannel?.isPrivate == true) {
                containerMenu.isPrivate = true
                containerMenu.screen?.accessBtn?.isPrivate = true
                containerMenu.screen?.onChannelListChanged()
            }
        }
    }
}