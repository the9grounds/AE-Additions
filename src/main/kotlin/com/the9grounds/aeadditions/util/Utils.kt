package com.the9grounds.aeadditions.util

import net.minecraft.entity.item.ItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import kotlin.random.Random

object Utils {
    fun spawnDrops(w: World, pos: BlockPos, drops: List<ItemStack>) {
        if (!w.isRemote()) {
            for (itemStack in drops) {
                if (!itemStack.isEmpty && itemStack.count > 0) {
                    val offset_x = ((Random.nextInt() % 32 - 16) / 82).toDouble()
                    val offset_y = ((Random.nextInt() % 32 - 16) / 82).toDouble()
                    val offset_z = ((Random.nextInt() % 32 - 16) / 82).toDouble()
                    val itemEntity = ItemEntity(
                        w, 0.5 + offset_x + pos.x,
                        0.5 + offset_y + pos.y, 0.2 + offset_z + pos.z, itemStack.copy()
                    )
                    w.addEntity(itemEntity)
                }
            }
        }
    }
}