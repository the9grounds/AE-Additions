package com.the9grounds.aeadditions.blockentity

import appeng.api.networking.*
import appeng.api.util.AECableType
import appeng.me.helpers.BlockEntityNodeListener
import appeng.me.helpers.IGridConnectedBlockEntity
import com.the9grounds.aeadditions.Config
import com.the9grounds.aeadditions.Logger
import com.the9grounds.aeadditions.forge.registries.BlockEntities
import com.the9grounds.aeadditions.registries.Blocks
import com.the9grounds.aeadditions.forge.registries.CapabilityRegistry
import com.the9grounds.aeadditions.util.Channel
import com.the9grounds.aeadditions.util.ChannelHolder
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.entity.BlockEntity
import java.util.*


class MEWirelessTransceiverBlockEntity(pos: BlockPos, blockState: BlockState): BlockEntity(BlockEntities.ME_WIRELESS_TRANSCEIVER, pos, blockState), IMEWirelessTransceiver, IGridConnectedBlockEntity {


    override val channelHolder: ChannelHolder
        get () = level!!.getCapability(CapabilityRegistry.CHANNEL_HOLDER).resolve().get()

    override var currentChannel: Channel? = null
    override var channelId: UUID? = null
    override var channelConnectionType: String? = null
    var connection: IGridConnection? = null
    var connections: MutableList<IGridConnection> = mutableListOf()
    override val item: Item
        get() = Blocks.BLOCK_ME_WIRELESS_TRANSCEIVER.item
    override var idleDraw: Double = 0.0
        set(value) {
            field = value
            if (mainNode.isReady) {
                mainNode.setIdlePowerUsage(value)
            }
        }
    val mainGridNode: IManagedGridNode = GridHelper.createManagedNode(this, BlockEntityNodeListener.INSTANCE).setInWorldNode(true).setFlags(GridFlags.DENSE_CAPACITY).setVisualRepresentation(item).setTagName("wirelesstransceiver")
    override val position: BlockPos = blockPos
    override fun isBlockEntityRemoved(): Boolean = isRemoved

    override fun hasChunkAt(pos: BlockPos): Boolean = level?.hasChunkAt(pos) ?: false

    override fun persistChanges() {
        setChanged()
    }

    override fun saveAdditional(arg: CompoundTag) {
        super.saveAdditional(arg)
        mainNode.saveToNBT(arg)
        super.saveToNbt(arg)
    }

    override fun load(arg: CompoundTag) {
        super.load(arg)
        mainNode.loadFromNBT(arg)
        super.loadFromNBT(arg)
    }

    override fun destroyConnections(remove: Boolean, clearCurrentChannel: Boolean) {
        if (currentChannel != null) {
            if (currentChannel!!.broadcaster == this) {
                currentChannel!!.subscribers.forEach {
                    it as MEWirelessTransceiverBlockEntity
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

    override fun getMainNode(): IManagedGridNode = mainGridNode

    override fun saveChanges() {
        TODO("Not yet implemented")
    }

    override fun createMenu(i: Int, inventory: Inventory, player: Player): AbstractContainerMenu? {
        TODO("Not yet implemented")
    }

    override fun onLoad() {
        super.onLoad()
        handleOnLoad()
    }

    override fun setRemoved() {
        super.setRemoved()
        mainNode.destroy()
        handleSetRemoved()
    }

    override fun onChunkUnloaded() {
        super.onChunkUnloaded()
        mainNode.destroy()
        super.handleChunkUnloaded()
    }

    override fun getGridNode(): IGridNode? {
        if (level == null || level!!.isClientSide) {
            return null
        }

        if (!mainGridNode.isReady) {
            mainGridNode.create(level, blockPos)
        }

        return mainGridNode.node
    }

    override fun blockPlaced() {
        getGridNode(null)
    }

    override fun getCableConnectionType(dir: Direction?): AECableType = AECableType.SMART

    override fun setupLinks() {
        if (currentChannel != null && currentChannel!!.broadcaster == this) {
            var localIdleDraw = Config.getConfig().meWirelessTransceiverBasePower.toDouble()
            for (subscriber in currentChannel!!.subscribers) {
                if (hasChunkAt(subscriber.position) && subscriber is MEWirelessTransceiverBlockEntity) {
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
}