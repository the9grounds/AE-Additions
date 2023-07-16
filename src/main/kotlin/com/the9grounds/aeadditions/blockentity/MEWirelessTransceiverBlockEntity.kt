package com.the9grounds.aeadditions.blockentity

import appeng.api.networking.*
import appeng.api.util.AECableType
import appeng.me.helpers.BlockEntityNodeListener
import appeng.me.helpers.IGridConnectedBlockEntity
import com.the9grounds.aeadditions.Logger
import com.the9grounds.aeadditions.core.AEAConfig
import com.the9grounds.aeadditions.menu.MEWirelessTransceiverMenu
import com.the9grounds.aeadditions.registries.BlockEntities
import com.the9grounds.aeadditions.registries.Blocks
import com.the9grounds.aeadditions.registries.Capability
import com.the9grounds.aeadditions.util.Channel
import com.the9grounds.aeadditions.util.ChannelHolder
import com.the9grounds.aeadditions.util.ChannelInfo
import mcjty.theoneprobe.api.IProbeHitData
import mcjty.theoneprobe.api.IProbeInfo
import mcjty.theoneprobe.api.ProbeMode
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.fml.util.thread.SidedThreadGroups
import java.util.*

class MEWirelessTransceiverBlockEntity(pos: BlockPos, blockState: BlockState) : BlockEntity(BlockEntities.ME_WIRELESS_TRANSCEIVER, pos, blockState), IGridConnectedBlockEntity, IInWorldGridNodeHost, MenuProvider {
    var currentChannel : Channel? = null
    var channelId: UUID? = null
    var channelConnectionType: String? = null
    var connection: IGridConnection? = null
    val connections: MutableList<IGridConnection> = mutableListOf()
    val item = Blocks.BLOCK_ME_WIRELESS_TRANSCEIVER.item
    var idleDraw = 0.0
    set(value) {
        field = value
        if (mainNode.isReady) {
            mainNode.setIdlePowerUsage(value)
        }
    }
    
    val _mainGridNode: IManagedGridNode = GridHelper.createManagedNode(this, BlockEntityNodeListener.INSTANCE).setInWorldNode(true).setFlags(GridFlags.DENSE_CAPACITY).setVisualRepresentation(item).setTagName("wirelesstransceiver")

    override fun getMainNode(): IManagedGridNode = _mainGridNode

    override fun securityBreak() {
        
    }

    override fun getCableConnectionType(dir: Direction?): AECableType = AECableType.SMART

    override fun load(tag: CompoundTag) {
        super.load(tag)
        mainNode.loadFromNBT(tag)
        val channelId = tag.getString("channelId")
        
        if (channelId != "") {
            val uuid = UUID.fromString(channelId)
            
            this.channelId = uuid
            this.channelConnectionType = tag.getString("transceiverType")
        }
    }

    override fun saveChanges() {
        
    }
    
    fun removedFromChannel(channelInfo: ChannelInfo) {
        this.destroyConnections()
        this.channelId = null
        this.channelConnectionType = null
    }

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
            setChanged()
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
                setChanged()
            }
        }
    }
    
    fun setupLinks() {
        if (currentChannel != null && currentChannel!!.broadcaster == this) {
            var localIdleDraw = AEAConfig.meWirelessTransceiverBasePower.toDouble()
            for (subscriber in currentChannel!!.subscribers) {
                if (level!!.hasChunkAt(subscriber.blockPos)) {
                    if (subscriber.connection != null) {
                        subscriber.connection?.destroy()
                        subscriber.connection = null
                    }
                    try {
                        val connection = GridHelper.createGridConnection(getGridNode(null), subscriber.getGridNode(null))
                        subscriber.connection = connection
                        connections.add(connection)
                        localIdleDraw += AEAConfig.meWirelessTransceiverDistanceMultiplier * blockPos.distSqr(subscriber.blockPos)
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

    override fun saveAdditional(tag: CompoundTag) {
        super.saveAdditional(tag)
        mainNode.saveToNBT(tag)
        if (channelId !== null) {
            
            tag.putString("transceiverType", channelConnectionType)
            tag.putString("channelId", channelId.toString())
        }
    }

    override fun onChunkUnloaded() {
        super.onChunkUnloaded()
        mainNode.destroy()
        if (Thread.currentThread().threadGroup != SidedThreadGroups.SERVER) {
            return
        }
        val cachedChannel = this.currentChannel
        this.destroyConnections(remove = true, clearCurrentChannel = false)
        if (cachedChannel != null && cachedChannel.broadcaster != this) {
            cachedChannel.broadcaster?.setupLinks()
        }
    }

    override fun setRemoved() {
        super.setRemoved()
        mainNode.destroy()
        requestModelDataUpdate()
    }

    fun addProbeInfo(
        blockEntity: MEWirelessTransceiverBlockEntity?,
        mode: ProbeMode?,
        probeInfo: IProbeInfo?,
        player: Player?,
        level: Level?,
        blockState: BlockState?,
        data: IProbeHitData?
    ) {
        if (currentChannel !== null) {
            val type = if (currentChannel!!.broadcaster == this) {
                "Broadcaster"
            } else {
                "Subscriber"
            }
            
            probeInfo!!.text(Component.literal("Connected to ${currentChannel!!.channelInfo.name} as $type"))
            when(type) {
                "Broadcaster" -> probeInfo!!.text("Current Power Usage: ${idleDraw} AE/t")
                "Subscriber" -> {
                    if (currentChannel!!.broadcaster != null) {
                        val contributingPowerUsage = AEAConfig.meWirelessTransceiverDistanceMultiplier * currentChannel!!.broadcaster!!.blockPos.distSqr(blockPos)
                        probeInfo!!.text("Contributing $contributingPowerUsage AE/t to Broadcaster's power draw")
                    }
                }
            }
        } else {
            probeInfo!!.text(Component.literal("Not Connected"))
        }
    }
    
    fun blockPlaced() {
        getGridNode(null)
    }

    override fun getGridNode(dir: Direction?): IGridNode? {
        if (null == level || level!!.isClientSide) {
            return null
        }
        
        if (!_mainGridNode.isReady) {
            _mainGridNode.create(level, blockPos)
        }
        
        return _mainGridNode.node
    }

    override fun onLoad() {
        super.onLoad()
        if (channelId != null && Thread.currentThread().threadGroup == SidedThreadGroups.SERVER) {
            val channelInfo = channelHolder.getChannelById(channelId!!) ?: return

            if (channelConnectionType == "broadcast") {
                this.broadcastToChannel(channelInfo, true)
            } else {
                this.subscribeToChannel(channelInfo, true)
            }
        }
    }

    override fun createMenu(id: Int, inventory: Inventory, player: Player): AbstractContainerMenu = MEWirelessTransceiverMenu(id, inventory, this)

    override fun getDisplayName(): Component = Component.literal("ME Wireless Transceiver")

    val channelHolder: ChannelHolder 
    get () = level!!.getCapability(Capability.CHANNEL_HOLDER).resolve().get()
}