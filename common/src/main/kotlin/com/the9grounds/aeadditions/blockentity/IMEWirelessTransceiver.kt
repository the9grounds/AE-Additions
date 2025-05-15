package com.the9grounds.aeadditions.blockentity

import appeng.api.networking.GridHelper
import appeng.api.networking.IGridConnection
import appeng.api.networking.IManagedGridNode
import appeng.api.util.AECableType
import appeng.me.helpers.IGridConnectedBlockEntity
import com.the9grounds.aeadditions.Config
import com.the9grounds.aeadditions.Logger
import com.the9grounds.aeadditions.util.Channel
import com.the9grounds.aeadditions.util.ChannelHolder
import com.the9grounds.aeadditions.util.ChannelInfo
import dev.architectury.platform.Platform
import net.fabricmc.api.EnvType
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.world.MenuProvider
import net.minecraft.world.item.Item
import java.util.*

interface IMEWirelessTransceiver: IGridConnectedBlockEntity, MenuProvider {
    var currentChannel: Channel?
    var channelId: UUID?
    var channelConnectionType: String?
    var connection: IGridConnection?
    var connections: MutableList<IGridConnection>
    val item: Item
    var idleDraw: Double
    val mainGridNode: IManagedGridNode

    override fun getCableConnectionType(dir: Direction?): AECableType = AECableType.SMART

    fun isServerSided(): Boolean = Platform.getEnv() == EnvType.SERVER

    val position: BlockPos

    fun isBlockEntityRemoved(): Boolean


    fun hasChunkAt(pos: BlockPos): Boolean

    fun onIdleDrawChanged() {
        if (mainNode.isReady) {
            mainNode.setIdlePowerUsage(idleDraw)
        }
    }

    fun loadFromNBT(tag: CompoundTag) {
        mainNode.loadFromNBT(tag)
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

    fun setupLinks() {
        if (currentChannel != null && currentChannel!!.broadcaster == this) {
            var localIdleDraw = Config.getConfig().meWirelessTransceiverBasePower.toDouble()
            for (subscriber in currentChannel!!.subscribers) {
                if (hasChunkAt(subscriber.position)) {
                    if (subscriber.connection != null) {
                        subscriber.connection?.destroy()
                        subscriber.connection = null
                    }
                    try {
                        val connection = GridHelper.createConnection(getGridNode(null), subscriber.getGridNode(null))
                        subscriber.connection = connection
                        connections.add(connection)
                        localIdleDraw += Config.getConfig().meWirelessTransceiverDistanceMultiplier * position.distSqr(subscriber.position)
                    } catch (e: Exception) {
                        Logger.info(e)
                    }
                }
            }
            idleDraw = localIdleDraw
        }
    }

    fun destroyConnections(remove: Boolean = true, clearCurrentChannel: Boolean = true) {
        if (currentChannel != null) {
            if (currentChannel!!.broadcaster == this) {
                currentChannel!!.subscribers.forEach {
                    if (it.connection != null) {
                        it.connection!!.destroy()
                    }
                }

                if (remove) {
                    currentChannel!!.broadcaster = null
                }
            } else {
                if (connection != null) {
                    connection!!.destroy()
                    connection = null
                }
                if (remove) {
                    currentChannel!!.subscribers.remove(this)
                }
            }

            if (clearCurrentChannel) {
                currentChannel = null
            }
        }
    }

    fun saveToNbt(tag: CompoundTag) {
        mainNode.saveToNBT(tag)
        if (channelId !== null) {

            tag.putString("transceiverType", channelConnectionType)
            tag.putString("channelId", channelId.toString())
        }
    }

    fun handleChunkUnloaded() {
        mainNode.destroy()
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
        mainNode.destroy()
    }

    fun blockPlaced() {
        getGridNode(null)
    }

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