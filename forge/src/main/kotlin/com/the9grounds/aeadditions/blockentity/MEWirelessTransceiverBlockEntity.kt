package com.the9grounds.aeadditions.blockentity

import appeng.api.networking.*
import appeng.me.helpers.BlockEntityNodeListener
import com.the9grounds.aeadditions.registries.BlockEntities
import com.the9grounds.aeadditions.registries.CapabilityRegistry
import com.the9grounds.aeadditions.util.Channel
import com.the9grounds.aeadditions.util.ChannelHolder
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.entity.BlockEntity
import java.util.*


class MEWirelessTransceiverBlockEntity(pos: BlockPos, blockState: BlockState): BlockEntity(BlockEntities.ME_WIRELESS_TRANSCEIVER, pos, blockState), IMEWirelessTransceiver {


    override val channelHolder: ChannelHolder
        get () = level!!.getCapability(CapabilityRegistry.CHANNEL_HOLDER).resolve().get()

    override var currentChannel: Channel? = null
    override var channelId: UUID? = null
    override var channelConnectionType: String? = null
    override var connection: IGridConnection? = null
    override var connections: MutableList<IGridConnection> = mutableListOf()
    override val item: Item
        get() = TODO("Not yet implemented")
    override var idleDraw: Double = 0.0
        set(value) {
            field = value
            onIdleDrawChanged()
        }
    override val mainGridNode: IManagedGridNode = GridHelper.createManagedNode(this, BlockEntityNodeListener.INSTANCE).setInWorldNode(true).setFlags(GridFlags.DENSE_CAPACITY).setVisualRepresentation(item).setTagName("wirelesstransceiver")
    override val position: BlockPos = blockPos

    override fun hasChunkAt(pos: BlockPos): Boolean = level?.hasChunkAt(pos) ?: false

    override fun persistChanges() {
        setChanged()
    }

    override fun saveAdditional(arg: CompoundTag) {
        super.saveAdditional(arg)
        super.saveToNbt(arg)
    }

    override fun load(arg: CompoundTag) {
        super.load(arg)
        super.loadFromNBT(arg)
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
        handleSetRemoved()
    }

    override fun onChunkUnloaded() {
        super.onChunkUnloaded()
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
}