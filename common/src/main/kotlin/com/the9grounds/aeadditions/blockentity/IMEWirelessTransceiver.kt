package com.the9grounds.aeadditions.blockentity

import com.the9grounds.aeadditions.util.Channel
import com.the9grounds.aeadditions.util.ChannelHolder
import com.the9grounds.aeadditions.util.ChannelInfo
import dev.architectury.platform.Platform
import net.fabricmc.api.EnvType
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.world.MenuProvider
import net.minecraft.world.item.Item
import java.util.*

interface IMEWirelessTransceiver: MenuProvider {
    var currentChannel: Channel?
    var channelId: UUID?
    var channelConnectionType: String?
    val item: Item
    var idleDraw: Double

    fun isServerSided(): Boolean = Platform.getEnv() == EnvType.SERVER

    val position: BlockPos

    fun isBlockEntityRemoved(): Boolean

    fun hasChunkAt(pos: BlockPos): Boolean

    fun loadFromNBT(tag: CompoundTag) {
        val channelId = tag.getString("channelId")

        if (channelId.isNotEmpty()) {
            val uuid = UUID.fromString(channelId)

            this.channelId = uuid
            this.channelConnectionType = tag.getString("transceiverType")
        }
    }

    fun removedFromChannel(channelInfo: ChannelInfo) {
        destroyConnections(false)
        channelConnectionType = null
        channelId = null
    }

    fun persistChanges()

    fun broadcastToChannel(channelInfo: ChannelInfo, initial: Boolean = false) {
        channelHolder.findChannelForBlockEntity(this)?.removeBlockEntity(this)

        val channel = channelHolder.getOrCreateChannel(channelInfo)

        channel.broadcaster = this

        currentChannel = channel
        channelId = channel.channelInfo.id
        channelConnectionType = "broadcast"

        channel.checkSubscribers()

        setupLinks()
        if (!initial) {
            persistChanges()
        }
    }

    fun subscribeToChannel(channelInfo: ChannelInfo, initial: Boolean = false) {
        channelHolder.findChannelForBlockEntity(this)?.removeBlockEntity(this)
        val channel = channelHolder.getOrCreateChannel(channelInfo)

        val alreadyExists = channel.subscribers.find { it == this } != null

        if (!alreadyExists) {
            channel.checkSubscribers()
            channel.subscribers.add(this)

            currentChannel = channel
            channelId = channel.channelInfo.id
            channelConnectionType = "subscribe"

            if (currentChannel!!.broadcaster !== null) {
                currentChannel!!.broadcaster!!.setupLinks()
            }

            if (!initial) {
                persistChanges()
            }
        }
    }

    fun setupLinks()

    fun destroyConnections(remove: Boolean = true, clearCurrentChannel: Boolean = true)

    fun saveToNbt(tag: CompoundTag) {
        if (channelId !== null) {

            tag.putString("transceiverType", channelConnectionType)
            tag.putString("channelId", channelId.toString())
        }
    }

    fun handleChunkUnloaded() {
        if (!isServerSided()) {
            return
        }
        val cachedChannel = this.currentChannel
        this.destroyConnections(remove = true, clearCurrentChannel = false)
        if (cachedChannel != null && cachedChannel.broadcaster != this) {
            cachedChannel.broadcaster?.setupLinks()
        }
    }

    fun handleSetRemoved() {

    }

    fun blockPlaced()

    fun handleOnLoad() {
        if (channelId != null && isServerSided()) {
            val channelInfo = channelHolder.getChannelById(channelId!!) ?: return

            if (channelConnectionType == "broadcast") {
                this.broadcastToChannel(channelInfo, true)
            } else {
                this.subscribeToChannel(channelInfo, true)
            }
        }
    }

    override fun getDisplayName(): Component = Component.literal("ME Wireless Transceiver")

    val channelHolder: ChannelHolder
}