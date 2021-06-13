package com.the9grounds.aeadditions.integration.mekanism

import com.the9grounds.aeadditions.api.IWrenchHandler
import mekanism.api.IMekWrench
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumHand
import net.minecraft.util.math.RayTraceResult

object WrenchHandler: IWrenchHandler {
    override fun canWrench(
        itemStack: ItemStack?,
        user: EntityPlayer?,
        rayTraceResult: RayTraceResult?,
        hand: EnumHand?
    ): Boolean {
        if (itemStack == null) {
            return false
        }

        val item = itemStack.item

        if (item == null || item !is IMekWrench) {
            return false
        }

        return item.canUseWrench(user, hand, itemStack, rayTraceResult)
    }

    override fun wrenchUsed(
        itemStack: ItemStack?,
        user: EntityPlayer?,
        rayTraceResult: RayTraceResult?,
        hand: EnumHand?
    ) {
        TODO("Not yet implemented")
    }
}