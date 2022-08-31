package com.the9grounds.aeadditions.registries

import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.blockentity.MEWirelessTransceiverBlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import thedarkcolour.kotlinforforge.forge.registerObject

object BlockEntities {
    val REGISTRY = KDeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, AEAdditions.ID)
    
    val ME_WIRELESS_TRANSCEIVER by REGISTRY.registerObject("me_wireless_transceiver") { BlockEntityType.Builder.of(::MEWirelessTransceiverBlockEntity, Blocks.BLOCK_ME_WIRELESS_TRANSCEIVER.block).build(null) }

    fun init() {
        
    }
}