package com.the9grounds.aeadditions.registries

import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.blockentity.MEWirelessTransceiverBlockEntity
import dev.architectury.registry.registries.DeferredRegister
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.entity.BlockEntityType

object BlockEntities {
    val REGISTRY = DeferredRegister.create(AEAdditions.ID, Registries.BLOCK_ENTITY_TYPE)

    val ME_WIRELESS_TRANSCEIVER = REGISTRY.register("me_wireless_transceiver") { BlockEntityType.Builder.of(::MEWirelessTransceiverBlockEntity, Blocks.BLOCK_ME_WIRELESS_TRANSCEIVER.block).build(null)}.get()

    fun init() {

    }
}