package com.the9grounds.aeadditions.menu

import com.the9grounds.aeadditions.Logger
import com.the9grounds.aeadditions.blockentity.MEWirelessTransceiverBlockEntity
import com.the9grounds.aeadditions.client.gui.MEWirelessTransceiverScreen
import com.the9grounds.aeadditions.core.network.NetworkManager
import com.the9grounds.aeadditions.core.network.packet.client.ChannelsPacket
import com.the9grounds.aeadditions.core.network.packet.client.TransceiverInfoPacket
import com.the9grounds.aeadditions.core.network.packet.server.RequestChannelsPacket
import com.the9grounds.aeadditions.util.ChannelInfo
import com.the9grounds.aeadditions.util.Utils
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import net.neoforged.fml.util.thread.SidedThreadGroups
import java.util.Optional

class MEWirelessTransceiverMenu(id: Int, val inventory: Inventory) : AbstractContainerMenu(MenuHolder.menuMEWirelessTransceiver, id) {
    
    var blockEntity: MEWirelessTransceiverBlockEntity? = null
    var isPrivate = false
    var isSubscriber = true
    var isCurrentlySubscribing = false
    var currentChannel: ChannelInfo? = null
    var isOnChannel: Boolean = false
    
    var ae2ChannelUsage = 0
    var ae2MaxChannels = 0
    
    var screen: MEWirelessTransceiverScreen? = null
    
    constructor(id: Int, inventory: Inventory, blockEntity: MEWirelessTransceiverBlockEntity) : this(id, inventory) {
        this.blockEntity = blockEntity
    }
    
    init {
        val posX = 7
        val posY = 144
        var slotX = posX
        var slotY = posY + (18 * 2) + 18 + 4
        var isHotBar = true
        inventory.items.forEachIndexed { index, itemStack -> 
            addSlot(Slot(inventory, index, slotX + 1, slotY + 1))

            slotX += 18

            if ((index + 1).rem(9) == 0) {
                if (isHotBar) {
                    slotY -= 4
                    isHotBar = false
                }

                slotY -= 18
                slotX = posX
            }
        }
        
        if (Thread.currentThread().threadGroup != SidedThreadGroups.SERVER) {
            NetworkManager.sendToServer(RequestChannelsPacket())
        }
    }

    override fun quickMoveStack(p_38941_: Player, p_38942_: Int): ItemStack {
        return ItemStack.EMPTY; 
    }

    fun sendTransceiverInfoToClient() {
        val level = inventory.player.level() as ServerLevel

        val channelHolder = Utils.getChannelHolderForLevel(level)

        if (channelHolder === null) {
            Logger.error("Channel holder is null")
            return
        }

        var maxChannels = 0
        var usedChannels = 0

        val channelInfo = blockEntity?.currentChannel?.channelInfo

        if (channelInfo != null) {
            val channel = channelHolder.getOrCreateChannel(channelInfo)

            maxChannels = channel.maxChannels
            usedChannels = channel.usedChannels
        }

        val transceiverInfoPacket = TransceiverInfoPacket(Optional.ofNullable(channelInfo), blockEntity?.currentChannel?.broadcaster !== blockEntity, usedChannels, maxChannels)

        NetworkManager.sendTo(transceiverInfoPacket, inventory.player as ServerPlayer)
    }

    fun sendChannelsToClient() {
        val level = inventory.player.level() as ServerLevel

        val channelHolder = Utils.getChannelHolderForLevel(level)

        if (channelHolder === null) {
            Logger.error("Channel holder is null")
            return
        }

        val filteredChannels = channelHolder.channelInfos.filter {
            it.hasAccessTo(inventory.player as ServerPlayer)
        }

        NetworkManager.sendTo(ChannelsPacket(filteredChannels), inventory.player as ServerPlayer)
    }
    
    var channelInfos = listOf<ChannelInfo>()

    override fun stillValid(player: Player): Boolean {
        return true
    }
    
    fun receiveChannelData(channelInfos: List<ChannelInfo>) {
        this.channelInfos = channelInfos
        
        screen?.onChannelListChanged()
    }
}