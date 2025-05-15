package com.the9grounds.aeadditions.forge.registries

import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.forge.block.CraftingStorageBlock
import com.the9grounds.aeadditions.block.MEWirelessTransceiverBlock
import com.the9grounds.aeadditions.forge.block.crafting.ExtendedCraftingUnitType
import com.the9grounds.aeadditions.core.BlockDefinition
import com.the9grounds.aeadditions.forge.integrations.Mods
import com.the9grounds.aeadditions.registries.Ids
import com.the9grounds.aeadditions.forge.registries.Items
import dev.architectury.registry.registries.DeferredRegister
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.BlockItem
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour

object Blocks {
    val REGISTRY = DeferredRegister.create(AEAdditions.ID, Registries.BLOCK)

    val BLOCKS = mutableListOf<Block>()
    
    
    @JvmField val BLOCK_CRAFTING_STORAGE_1024k = createBlock(Ids.CRAFTING_STORAGE_1024k, SoundType.METAL) {
        CraftingStorageBlock(it.strength(.75f, 11f).sound(SoundType.METAL), ExtendedCraftingUnitType.STORAGE_1024)
    }
    @JvmField val BLOCK_CRAFTING_STORAGE_4096k = createBlock(Ids.CRAFTING_STORAGE_4096k, SoundType.METAL) {
        CraftingStorageBlock(it.strength(.75f, 11f).sound(SoundType.METAL), ExtendedCraftingUnitType.STORAGE_4096)
    }
    @JvmField val BLOCK_CRAFTING_STORAGE_16384k = createBlock(Ids.CRAFTING_STORAGE_16384k, SoundType.METAL) {
        CraftingStorageBlock(it.strength(.75f, 11f).sound(SoundType.METAL), ExtendedCraftingUnitType.STORAGE_16384)
    }
    @JvmField val BLOCK_CRAFTING_STORAGE_65536k = createBlock(Ids.CRAFTING_STORAGE_65536k, SoundType.METAL) {
        CraftingStorageBlock(it.strength(.75f, 11f).sound(SoundType.METAL), ExtendedCraftingUnitType.STORAGE_65536)
    }
    
    val BLOCK_ME_WIRELESS_TRANSCEIVER = createBlock(Ids.ME_WIRELESS_TRANSCEIVER, SoundType.METAL) {
        MEWirelessTransceiverBlock(it.strength(.75f, 11f).sound(SoundType.METAL))
    }
    
    fun init() {
        
    }

    fun <T: Block> createBlock(id: ResourceLocation, soundType: SoundType, factory: (BlockBehaviour.Properties) -> T): BlockDefinition<T> {
        val block = constructBlock(soundType, factory, id)

        val item = Items.createItem(id) { properties -> BlockItem(block, properties) }

        return BlockDefinition(block, item)
    }

    fun <T: Block> createBlock(id: ResourceLocation, soundType: SoundType, requiredMod: Mods, factory: (BlockBehaviour.Properties) -> T): BlockDefinition<T> {
        val block = constructBlock(soundType, factory, id)

        val item = Items.createItem(id, { properties -> BlockItem(block, properties) }, requiredMod)

        return BlockDefinition(block, item)
    }

    private fun <T : Block> constructBlock(
        soundType: SoundType,
        factory: (BlockBehaviour.Properties) -> T,
        id: ResourceLocation
    ): T {
        val props = BlockBehaviour.Properties.of().strength(2.2f, 11f).sound(soundType)

        val block = factory(props)

        BLOCKS.add(block)

        REGISTRY.register(id.path) {
            block
        }
        return block
    }
}