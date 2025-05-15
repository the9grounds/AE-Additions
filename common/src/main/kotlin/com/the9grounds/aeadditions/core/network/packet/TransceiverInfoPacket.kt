package com.the9grounds.aeadditions.core.network.packet

import com.the9grounds.aeadditions.menu.MEWirelessTransceiverMenu
import com.the9grounds.aeadditions.util.ChannelInfo
import dev.architectury.networking.NetworkManager
import net.minecraft.client.Minecraft
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.level.Level
import java.util.function.Supplier

class TransceiverInfoPacket : BasePacket {
    var currentChannel : ChannelInfo
    var subscribe = false
    var channelUsage = 0
    var maxChannels = 0

    constructor(packet: FriendlyByteBuf) {
        subscribe = packet.readBoolean()
        channelUsage = packet.readInt()
        maxChannels = packet.readInt()
        val nbt = packet.readNbt()
        currentChannel = ChannelInfo.readFromNbt(nbt!!, Minecraft.getInstance().level!!)
    }

    constructor(subscribe: Boolean, level: Level, channelInfo: ChannelInfo) {
        this.subscribe = subscribe
        this.currentChannel = channelInfo
//        if (level.getCapability(Capability.CHANNEL_HOLDER).resolve().isPresent) {
//            val channelHolder = level.getCapability(Capability.CHANNEL_HOLDER).resolve().get()
//
//            val channel = channelHolder.getOrCreateChannel(channelInfo)
//
//            if (channel.broadcaster != null && channel.broadcaster?.mainNode?.node?.isActive == true) {
//                this.channelUsage = channel.broadcaster?.mainNode?.node?.usedChannels ?: 0
//                this.maxChannels = channel.broadcaster?.mainNode?.node?.maxChannels ?: 0
//            } else {
//                this.channelUsage = 0
//                this.maxChannels = 0
//            }
//        } else {
//            this.channelUsage = 0
//            this.maxChannels = 0
//        }
    }

    override fun encode(buf: FriendlyByteBuf) {
        buf.writeBoolean(subscribe)
        buf.writeInt(channelUsage)
        buf.writeInt(maxChannels)
        buf.writeNbt(currentChannel!!.saveToNbt())
    }

    override fun apply(contextSupplier: Supplier<NetworkManager.PacketContext>) {
        val containerMenu = contextSupplier.get().player.containerMenu
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