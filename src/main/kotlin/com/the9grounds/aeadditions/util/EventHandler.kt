package com.the9grounds.aeadditions.util

import com.the9grounds.aeadditions.registries.BlockEnum
import com.the9grounds.aeadditions.registries.ItemEnum
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object EventHandler {

    @SubscribeEvent
    fun onMappingsMissingEventBlock(event: RegistryEvent.MissingMappings<Block>) {
        val blockMap = mutableMapOf<String, BlockEnum>()

        for (enum in BlockEnum.values()) {
            blockMap.set(enum.internalName, enum)
        }

        event.allMappings.filter { it.key.namespace.equals("extracells") }.forEach{
            if (blockMap.contains(it.key.path)) {
                it.remap(blockMap[it.key.path]!!.block)
            }
        }
    }

    @SubscribeEvent
    fun onMappingsMissingEventItem(event: RegistryEvent.MissingMappings<Item>) {
        val itemMap = mutableMapOf<String, ItemEnum>()

        for (enum in ItemEnum.values()) {
            itemMap.set(enum.internalName, enum)
        }

        val itemBlockMap = mutableMapOf<String, BlockEnum>()

        for (enum in BlockEnum.values()) {
            itemBlockMap[enum.internalName] = enum
        }

        event.allMappings.filter { it.key.namespace.equals("extracells") }.forEach{
            if (itemMap.contains(it.key.path)) {
                it.remap(itemMap[it.key.path]!!.item)
            }

            if (itemBlockMap.contains(it.key.path)) {
                it.remap(itemBlockMap[it.key.path]!!.item)
            }
        }
    }
}