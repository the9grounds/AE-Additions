package com.the9grounds.aeadditions.registries

import net.minecraft.core.component.DataComponentType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.neoforge.registries.DeferredHolder
import kotlin.reflect.KProperty

public operator fun <D> DeferredHolder<DataComponentType<*>, DataComponentType<D>>.getValue(any: Any?, property: KProperty<*>): DataComponentType<D> {
    return get()
}

public operator fun <D: BlockEntity> DeferredHolder<BlockEntityType<*>, BlockEntityType<D>>.getValue(any: BlockEntity?, property: KProperty<*>): BlockEntityType<D> {
    return get()
}