package com.the9grounds.aeadditions.core

import com.the9grounds.aeadditions.util.ChannelHolder
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.saveddata.SavedData

class ChannelHolderSavedData: SavedData() {
    val channelHolder = DirtyChannelHolder()

    override fun save(arg: CompoundTag, p1: HolderLookup.Provider): CompoundTag {
        return this.channelHolder.save(arg)
    }

    override fun isDirty(): Boolean = true

    companion object {
        @JvmStatic
        fun load(tag: CompoundTag, p1: HolderLookup.Provider): ChannelHolderSavedData {
            val channelHolder = ChannelHolderSavedData()
            channelHolder.channelHolder.load(tag)

            return channelHolder
        }
    }

    inner class DirtyChannelHolder : ChannelHolder() {
        override fun persist() {
            this@ChannelHolderSavedData.setDirty()
        }
    }
}