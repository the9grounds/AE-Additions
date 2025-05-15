package com.the9grounds.aeadditions

import dev.architectury.injectables.annotations.ExpectPlatform
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

object Blocks {
    @ExpectPlatform
    @JvmStatic
    fun getTransceiverBlockEntity(pos: BlockPos, blockState: BlockState): BlockEntity {
        throw Error()
    }

    @ExpectPlatform
    @JvmStatic
    fun getTransceiverBlockEntityType(): BlockEntityType<BlockEntity> {
        throw Error()
    }
}