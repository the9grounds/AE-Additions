package com.the9grounds.aeadditions.util

import com.the9grounds.aeadditions.integration.Mods
import dev.ftb.mods.ftbteams.FTBTeamsAPI
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.Level
import java.util.*

data class ChannelInfo(val id: UUID, val level: Level, val name: String, val isPrivate: Boolean, val creator: UUID?, val creatorName: String?) {
    override fun equals(other: Any?): Boolean {
        return other is ChannelInfo && id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
    
    fun saveToNbt(): CompoundTag {
        val channelInfoTag = CompoundTag()

        channelInfoTag.putBoolean("isPrivate", isPrivate)
        channelInfoTag.putString("name", name)
        channelInfoTag.putString("id", id.toString())
        channelInfoTag.putString("playerUUID", creator.toString())
        channelInfoTag.putString("playerName", creatorName)
        
        return channelInfoTag
    }
    
    fun hasAccessTo(player: ServerPlayer): Boolean {
        
        if (!isPrivate) {
            return true
        }
        
        if (Mods.FTBTEAMS.isEnabled) {
            return FTBTeamsAPI.arePlayersInSameTeam(creator, player.uuid)
        }
        
        return creator === player.uuid
    }
    
    companion object {
        fun readFromNbt(nbt: CompoundTag, level: Level): ChannelInfo {
            val isPrivate = nbt.getBoolean("isPrivate")
            var creatorName: String? = null

            creatorName = nbt.getString("playerName")
            val creator = UUID.fromString(nbt.getString("playerUUID"))

            return ChannelInfo(UUID.fromString(nbt.getString("id")), level, nbt.getString("name"), isPrivate, creator, creatorName)
        }
    }
}
