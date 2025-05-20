package com.the9grounds.aeadditions.core

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import com.the9grounds.aeadditions.core.data.SuperStorageCellExtraInfo
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

object Codecs {
    val SUPER_STORAGE_CELL_EXTRA_INFO_CODEC = RecordCodecBuilder.create {
        it.group(
            Codec.LONG.fieldOf("usedBytes").forGetter(SuperStorageCellExtraInfo::usedBytes)
        ).apply(it, ::SuperStorageCellExtraInfo)
    }

    val SUPER_STORAGE_CELL_EXTRA_INFO_STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_LONG, SuperStorageCellExtraInfo::usedBytes,
        ::SuperStorageCellExtraInfo
    )
}