package com.the9grounds.aeadditions.menu

import com.the9grounds.aeadditions.blockentity.BaseMEWirelessTransceiverBlockEntity
import com.the9grounds.aeadditions.blockentity.MEWirelessTransceiverBlockEntity
import com.the9grounds.aeadditions.client.gui.MEWirelessTransceiverScreen
import com.the9grounds.aeadditions.core.network.NetworkManager
import com.the9grounds.aeadditions.core.network.packet.ChannelsPacket
import com.the9grounds.aeadditions.core.network.packet.RequestChannelsPacket
import com.the9grounds.aeadditions.registries.Capability
import com.the9grounds.aeadditions.util.Channel
import com.the9grounds.aeadditions.util.ChannelInfo
import dev.architectury.platform.Platform
import net.fabricmc.api.EnvType
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import net.minecraftforge.fml.util.thread.SidedThreadGroups

class MEWirelessTransceiverMenu(id: Int, val inventory: Inventory) : AbstractContainerMenu(MenuHolder.menuMEWirelessTransceiver, id) {
    
    var blockEntity: BaseMEWirelessTransceiverBlockEntity? = null
    var isPrivate = false
    var isSubscriber = true
    var isCurrentlySubscribing = false
    var currentChannel: ChannelInfo? = null
    var isOnChannel: Boolean = false
    
    var ae2ChannelUsage = 0
    var ae2MaxChannels = 0
    
    var screen: MEWirelessTransceiverScreen? = null
    
    constructor(id: Int, inventory: Inventory, blockEntity: BaseMEWirelessTransceiverBlockEntity) : this(id, inventory) {
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
        
        if (Platform.getEnv() === EnvType.CLIENT) {
            NetworkManager.sendToServer(RequestChannelsPacket())
        }
    }

    override fun quickMoveStack(p_38941_: Player, p_38942_: Int): ItemStack {
        return ItemStack.EMPTY; 
    }

    public fun sendChannelStuffToClient() {
        val level = inventory.player.level() as ServerLevel

        val channelHolder = level.getCapability(Capability.CHANNEL_HOLDER).resolve().get()

        val filteredChannels = channelHolder.channels.filter {
            it.value.hasAccessTo(inventory.player as ServerPlayer)
        }
        val filteredChannelInfos = channelHolder.channelInfos.filter {
            it.hasAccessTo(inventory.player as ServerPlayer)
        }

        val packet = ChannelsPacket(filteredChannels.values.toList(), filteredChannelInfos)

        NetworkManager.sendTo(packet, inventory.player as ServerPlayer)
    }
    
    var channelInfos = listOf<ChannelInfo>()
    var channels = listOf<Channel>()

    override fun stillValid(player: Player): Boolean {
        return true
    }
    
    fun receiveChannelData(channelInfos: List<ChannelInfo>, channels: List<Channel>) {
        this.channelInfos = channelInfos
        this.channels = channels
        
        screen?.onChannelListChanged()
    }
}