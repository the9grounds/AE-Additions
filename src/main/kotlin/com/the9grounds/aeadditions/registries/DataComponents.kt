package com.the9grounds.aeadditions.registries

import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.core.Codecs
import net.minecraft.core.registries.Registries
import net.neoforged.neoforge.registries.DeferredRegister

object DataComponents {
    val REGISTRY = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, AEAdditions.ID)

    val SUPER_STORAGE_CELL_EXTRA_INFO by REGISTRY.registerComponentType("super_storage_cell_extra_info") { builder ->
        builder.persistent(Codecs.SUPER_STORAGE_CELL_EXTRA_INFO_CODEC).networkSynchronized(Codecs.SUPER_STORAGE_CELL_EXTRA_INFO_STREAM_CODEC)
    }
}