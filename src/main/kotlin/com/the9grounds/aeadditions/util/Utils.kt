package com.the9grounds.aeadditions.util

import com.the9grounds.aeadditions.core.ChannelHolderSavedData
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.saveddata.SavedData


object Utils {

    fun getChannelHolderForLevel(level: Level?): ChannelHolder? {
        if (level !is ServerLevel) {
            return null
        }

        return level.dataStorage.computeIfAbsent(SavedData.Factory(::ChannelHolderSavedData, ChannelHolderSavedData::load), "channelHolder").channelHolder
    }
}