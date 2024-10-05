package com.the9grounds.aeadditions.core.network.packet

import com.the9grounds.aeadditions.Logger
import com.the9grounds.aeadditions.core.network.NetworkManager
import com.the9grounds.aeadditions.menu.MEWirelessTransceiverMenu
import com.the9grounds.aeadditions.registries.Capability
import com.the9grounds.aeadditions.util.ChannelInfo
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.minecraftforge.server.ServerLifecycleHooks
import java.util.*

class CreateChannelPacket : BasePacket {
    var isPrivate = false
    var channelName = ""
    var subscribe = false
    
    constructor(packet: FriendlyByteBuf) {
        val tag = packet.readNbt()

        if (tag == null) {
            Logger.warn("Tag was null in CreateChannelPacket")

            return
        }
        
        isPrivate = tag.getBoolean("isPrivate")
        channelName = tag.getString("channelName")
        subscribe = tag.getBoolean("subscribe")
    }
    
    constructor(isPrivate: Boolean, channelName: String, subscribe: Boolean) {
        val data = getInitialData()
        
        val nbt = CompoundTag()
        nbt.putBoolean("isPrivate", isPrivate)
        nbt.putString("channelName", channelName)
        nbt.putBoolean("subscribe", subscribe)
        
        data.writeNbt(nbt)
        
        configureWrite(data)
    }

    override fun serverPacketData(player: Player) {
        val containerMenu = player.containerMenu

        if (containerMenu !is MEWirelessTransceiverMenu) {
            return
        }
        
        val level = player.level() as ServerLevel

        val channelHolder = level.getCapability(Capability.CHANNEL_HOLDER).resolve().get()

        val alreadyExists = channelHolder.channelInfos.find { it.name == channelName && ((it.isPrivate && it.hasAccessTo(player as ServerPlayer)) || !it.isPrivate) } !== null

        if (alreadyExists) {
            return
        }
        
        val channelInfo = ChannelInfo(UUID.randomUUID(), level, channelName, isPrivate, player.uuid, player.name.toString())
        
        channelHolder.channelInfos.add(channelInfo)

        if (subscribe) {
            containerMenu.blockEntity!!.subscribeToChannel(channelInfo)
        } else {
            containerMenu.blockEntity!!.broadcastToChannel(channelInfo)
        }

        val filteredChannels = channelHolder.channels.filter {
            it.value.hasAccessTo(player as ServerPlayer)
        }
        val filteredChannelInfos = channelHolder.channelInfos.filter {
            it.hasAccessTo(player as ServerPlayer)
        }

        val packet = ChannelsPacket(filteredChannels.values.toList(), filteredChannelInfos)
        val transceiverDataChange = TransceiverDataChange(subscribe, player.level(), channelInfo)

        val server = ServerLifecycleHooks.getCurrentServer()

        server?.playerList?.players?.forEach {
            val innerContainerMenu = it.containerMenu
            if (innerContainerMenu is MEWirelessTransceiverMenu) {
                if (innerContainerMenu.blockEntity == containerMenu.blockEntity) {
                    NetworkManager.sendTo(transceiverDataChange, it)
                }
                
                if (it.level() == player.level()) {
                    NetworkManager.sendTo(packet, it)
                }
            }
        }
    }
}