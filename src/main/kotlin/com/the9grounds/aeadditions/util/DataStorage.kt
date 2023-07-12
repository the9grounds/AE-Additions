package com.the9grounds.aeadditions.util

import io.github.projectet.ae2things.storage.DISKCellInventory
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag

class DataStorage {
    var stackKeys: ListTag
    var stackAmounts: LongArray
    var itemCount: Long

    constructor() {
        stackKeys = ListTag()
        stackAmounts = LongArray(0)
        itemCount = 0
    }

    constructor(stackKeys: ListTag, stackAmounts: LongArray, itemCount: Long) {
        this.stackKeys = stackKeys
        this.stackAmounts = stackAmounts
        this.itemCount = itemCount
    }

    fun toNbt(): CompoundTag {
        val nbt = CompoundTag()
        nbt.put(DISKCellInventory.STACK_KEYS, stackKeys)
        nbt.putLongArray(DISKCellInventory.STACK_AMOUNTS, stackAmounts)
        if (itemCount != 0L) nbt.putLong(DISKCellInventory.ITEM_COUNT_TAG, itemCount)
        return nbt
    }

    companion object {
        val EMPTY = DataStorage()
        fun fromNbt(nbt: CompoundTag): DataStorage {
            var itemCount: Long = 0
            val stackKeys = nbt.getList(DISKCellInventory.STACK_KEYS, Tag.TAG_COMPOUND.toInt())
            val stackAmounts = nbt.getLongArray(DISKCellInventory.STACK_AMOUNTS)
            if (nbt.contains(DISKCellInventory.ITEM_COUNT_TAG)) itemCount =
                nbt.getLong(DISKCellInventory.ITEM_COUNT_TAG)
            return DataStorage(stackKeys, stackAmounts, itemCount)
        }
    }
}