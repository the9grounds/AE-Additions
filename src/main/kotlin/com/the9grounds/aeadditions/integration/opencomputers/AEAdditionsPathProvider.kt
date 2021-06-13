package com.the9grounds.aeadditions.integration.opencomputers

import com.the9grounds.aeadditions.Constants
import com.the9grounds.aeadditions.registries.ItemEnum
import li.cil.oc.api.Manual
import li.cil.oc.api.manual.PathProvider
import li.cil.oc.api.prefab.ItemStackTabIconRenderer
import li.cil.oc.api.prefab.ResourceContentProvider
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object AEAdditionsPathProvider : PathProvider {

    init {
        Manual.addProvider(this)
        Manual.addProvider(ResourceContentProvider(Constants.MOD_ID, "docs/"))
        Manual.addTab(ItemStackTabIconRenderer(ItemStack(ItemEnum.FLUIDSTORAGE.item)), "itemGroup.AE_Additions",
            Constants.MOD_ID + "/%LANGUAGE%/index.md")
    }

    override fun pathFor(itemStack: ItemStack?): String? {
        if (itemStack != null && itemStack.item == ItemEnum.OCUPGRADE.item) {
            return Constants.MOD_ID + "/%LANGUAGE%/me_upgrade.md"
        }

        return null
    }

    override fun pathFor(world: World?, pos: BlockPos?): String? = null
}