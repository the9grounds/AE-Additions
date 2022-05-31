package com.the9grounds.aeadditions.integration.theoneprobe

import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.blockentity.MEWirelessTransceiverBlockEntity
import mcjty.theoneprobe.api.IProbeHitData
import mcjty.theoneprobe.api.IProbeInfo
import mcjty.theoneprobe.api.IProbeInfoProvider
import mcjty.theoneprobe.api.ProbeMode
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

object BlockEntityProvider : IProbeInfoProvider {
    override fun getID(): ResourceLocation = ResourceLocation(AEAdditions.ID, "block_entity_provider")

    override fun addProbeInfo(
        mode: ProbeMode?,
        info: IProbeInfo?,
        player: Player?,
        level: Level?,
        blockState: BlockState?,
        hitdata: IProbeHitData?
    ) {
        val blockEntity = level!!.getBlockEntity(hitdata!!.pos)
        
        when(blockEntity) {
            is MEWirelessTransceiverBlockEntity -> blockEntity.addProbeInfo(blockEntity, mode, info, player, level, blockState, hitdata)
        }
    }
}