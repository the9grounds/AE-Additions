package com.the9grounds.aeadditions.item.storage

import appeng.hooks.AEToolItem
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext

open class DiskCellWithoutMod(props: Properties) : Item(props), AEToolItem {
    open override fun onItemUseFirst(stack: ItemStack?, context: UseOnContext?): InteractionResult {
        return InteractionResult.PASS
    }
}