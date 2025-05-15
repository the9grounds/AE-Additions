package com.the9grounds.aeadditions.forge

import com.the9grounds.aeadditions.blockentity.MEWirelessTransceiverBlockEntity
import com.the9grounds.aeadditions.forge.registries.BlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

object BlocksImpl {
    @JvmStatic
    fun getTransceiverBlockEntity(pos: BlockPos, blockState: BlockState): BlockEntity {
        return MEWirelessTransceiverBlockEntity(pos, blockState)
    }

    @JvmStatic
    fun getTransceiverBlockEntityType(): BlockEntityType<MEWirelessTransceiverBlockEntity>? {
        return BlockEntities.ME_WIRELESS_TRANSCEIVER
    }
}