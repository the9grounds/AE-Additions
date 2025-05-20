package com.the9grounds.aeadditions.util

import com.the9grounds.aeadditions.integration.Mods
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI
import net.minecraft.core.UUIDUtil
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.server.level.ServerPlayer
import java.util.*
import kotlin.jvm.optionals.getOrNull

data class ChannelInfo(val id: UUID, val name: String, val isPrivate: Boolean, val creator: UUID?, val creatorName: String?) {

    val creatorCodec: Optional<UUID>
        get() = Optional.ofNullable(creator)
    val creatorNameCodec: Optional<String>
    get() = Optional.ofNullable(creatorName)

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
            return FTBTeamsAPI.api().manager.arePlayersInSameTeam(creator, player.uuid)
        }
        
        return creator == player.uuid
    }

    fun hasAccessToDelete(player: ServerPlayer): Boolean {

        if (Mods.FTBTEAMS.isEnabled) {
            return FTBTeamsAPI.api().manager.arePlayersInSameTeam(creator, player.uuid)
        }

        return creator == player.uuid
    }
    
    companion object {
        val STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            ChannelInfo::id,
            ByteBufCodecs.STRING_UTF8,
            ChannelInfo::name,
            ByteBufCodecs.BOOL,
            ChannelInfo::isPrivate,
            ByteBufCodecs.optional(UUIDUtil.STREAM_CODEC),
            ChannelInfo::creatorCodec,
            ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8),
            ChannelInfo::creatorNameCodec,
            ChannelInfo::fromCodec
        )

        fun fromCodec(id: UUID, name: String, isPrivate: Boolean, creator: Optional<UUID>, creatorName: Optional<String>): ChannelInfo {
            return ChannelInfo(id, name, isPrivate, creator.getOrNull(), creatorName.getOrNull())
        }

        fun readFromNbt(nbt: CompoundTag): ChannelInfo {
            val isPrivate = nbt.getBoolean("isPrivate")
            var creatorName: String? = null

            creatorName = nbt.getString("playerName")
            val creator = UUID.fromString(nbt.getString("playerUUID"))

            return ChannelInfo(UUID.fromString(nbt.getString("id")), nbt.getString("name"), isPrivate, creator, creatorName)
        }
    }
}
