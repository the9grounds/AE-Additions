package com.the9grounds.aeadditions.blockentity

import appeng.api.networking.*
import appeng.me.helpers.BlockEntityNodeListener
import appeng.me.helpers.IGridConnectedBlockEntity
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
import net.minecraft.network.chat.TextComponent
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
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
        mainNode.setIdlePowerUsage(value)
    }
    
    val _mainGridNode: IManagedGridNode = GridHelper.createManagedNode(this, BlockEntityNodeListener.INSTANCE).setInWorldNode(true).setVisualRepresentation(item).setTagName("idk")

    override fun getMainNode(): IManagedGridNode = _mainGridNode

    override fun securityBreak() {
        
    }

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

    fun broadcastToChannel(channelInfo: ChannelInfo) {
        val channel = channelHolder.getOrCreateChannel(channelInfo)
        
        channel.broadcaster = this
        
        currentChannel = channel
        
        setupLinks()
        setChanged()
    }
    
    fun subscribeToChannel(channelInfo: ChannelInfo) {
        val channel = channelHolder.getOrCreateChannel(channelInfo)
        
        val alreadyExists = channel.subscribers.find { it == this } != null
        
        if (!alreadyExists) {
            channel.subscribers.add(this)

            currentChannel = channel

            if (currentChannel!!.broadcaster !== null) {
                currentChannel!!.broadcaster!!.setupLinks()
            }

            setChanged()
        }
    }
    
    fun setupLinks() {
        if (currentChannel != null && currentChannel!!.broadcaster == this) {
            var localIdleDraw = AEAConfig.meWirelessTransceiverBasePower.toDouble()
            for (subscriber in currentChannel!!.subscribers) {
                if (subscriber.connection == null) {
                    val connection = GridHelper.createGridConnection(getGridNode(null), subscriber.getGridNode(null))
                    subscriber.connection = connection
                    connections.add(connection)
                }
                localIdleDraw += AEAConfig.meWirelessTransceiverDistanceMultiplier * blockPos.distSqr(subscriber.blockPos)
            }
            idleDraw = localIdleDraw
        }
    }
    
    fun destroyConnections() {
        if (currentChannel != null) {
            if (currentChannel!!.broadcaster == this) {
                currentChannel!!.subscribers.forEach {
                    if (it.connection != null) {
                        it.connection!!.destroy()
                        it.connection = null
                    }
                }
                channelHolder.channels.remove(currentChannel!!.channelInfo)
            } else {
                if (connection != null) {
                    connection!!.destroy()
                    connection = null
                }
                currentChannel!!.subscribers.remove(this)
            }
            
            currentChannel = null
        }
    }

    override fun saveAdditional(tag: CompoundTag) {
        super.saveAdditional(tag)
        mainNode.saveToNBT(tag)
        if (currentChannel !== null) {
            val type = if (currentChannel!!.broadcaster == this) {
                "broadcast"
            } else {
                "subscribe"
            }
            
            tag.putString("transceiverType", type)
            tag.putString("channelId", currentChannel!!.channelInfo.id.toString())
        }
    }

    override fun onChunkUnloaded() {
        super.onChunkUnloaded()
        mainNode.destroy()
        this.destroyConnections()
    }

    override fun setRemoved() {
        super.setRemoved()
        mainNode.destroy()
        this.destroyConnections()
        if (level!!.hasChunkAt(blockPos)) {
        level!!.updateNeighborsAt(blockPos, blockState.block)
        }
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
            
            probeInfo!!.text(TextComponent("Connected to ${currentChannel!!.channelInfo.name} as $type"))
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
            probeInfo!!.text(TextComponent("Not Connected"))
        }
    }
    
    fun blockPlaced() {
        getGridNode(null)
    }

    override fun getGridNode(dir: Direction?): IGridNode? {
        if (null == level || level!!.isClientSide) {
            return null
        }
        
        if (_mainGridNode.node == null) {
            _mainGridNode.create(level, blockPos)
            level!!.updateNeighborsAt(blockPos, blockState.block)
        }
        
        return _mainGridNode.node
    }

    override fun onLoad() {
        super.onLoad()
        if (channelId != null) {
            val channelInfo = channelHolder.getChannelById(channelId!!) ?: return

            if (channelConnectionType == "broadcast") {
                this.broadcastToChannel(channelInfo)
            } else {
                this.subscribeToChannel(channelInfo)
            }
        }
    }

    override fun createMenu(id: Int, inventory: Inventory, player: Player): AbstractContainerMenu = MEWirelessTransceiverMenu(id, inventory, this)

    override fun getDisplayName(): Component = TextComponent("ME Wireless Transceiver")

    val channelHolder: ChannelHolder 
    get () = level!!.getCapability(Capability.CHANNEL_HOLDER).resolve().get()
}