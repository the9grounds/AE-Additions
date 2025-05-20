package com.the9grounds.aeadditions.registries

import appeng.blockentity.AEBaseBlockEntity
import appeng.blockentity.crafting.CraftingBlockEntity
import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.blockentity.MEWirelessTransceiverBlockEntity
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.neoforge.registries.DeferredRegister
//import com.the9grounds.aeadditions.registries.getValue
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object BlockEntities {
    val REGISTRY = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, AEAdditions.ID)

    val AE_CRAFTING_STORAGE = REGISTRY.register("ae2_crafting_storage") { ->
        val blocks = arrayOf(
            Blocks.BLOCK_CRAFTING_STORAGE_1024k.block.get(),
            Blocks.BLOCK_CRAFTING_STORAGE_4096k.block.get(),
            Blocks.BLOCK_CRAFTING_STORAGE_16384k.block.get(),
            Blocks.BLOCK_CRAFTING_STORAGE_65536k.block.get()
        )
        var typeRef: BlockEntityType<*>? = null
        val type = BlockEntityType.Builder.of(
            { blockPos, blockState ->
                CraftingBlockEntity(typeRef!!, blockPos, blockState)
            },
            *blocks
        ).build(null)
        typeRef = type

        AEBaseBlockEntity.registerBlockEntityItem(type, blocks.last().asItem())

        for (block in blocks) {
            block.setBlockEntity(CraftingBlockEntity::class.java, type, null, null)
        }

        type
    }
    val ME_WIRELESS_TRANSCEIVER by REGISTRY.register("me_wireless_transceiver") { -> BlockEntityType.Builder.of(::MEWirelessTransceiverBlockEntity, Blocks.BLOCK_ME_WIRELESS_TRANSCEIVER.block.get()).build(null) }

    fun init() {
        
    }
}