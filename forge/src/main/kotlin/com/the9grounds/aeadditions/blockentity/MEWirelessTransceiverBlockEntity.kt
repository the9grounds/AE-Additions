package com.the9grounds.aeadditions.blockentity

import appeng.api.networking.IInWorldGridNodeHost
import com.the9grounds.aeadditions.registries.BlockEntities
import com.the9grounds.aeadditions.registries.Capability
import com.the9grounds.aeadditions.util.ChannelHolder
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.entity.BlockEntity

class MEWirelessTransceiverBlockEntity(pos: BlockPos, blockState: BlockState): BaseMEWirelessTransceiverBlockEntity(BlockEntities.ME_WIRELESS_TRANSCEIVER, pos, blockState), IInWorldGridNodeHost {

    override val channelHolder: ChannelHolder
        get () = level!!.getCapability(Capability.CHANNEL_HOLDER).resolve().get()
}