package extracells.integration.enderio

import crazypants.enderio.api.tool.ITool
import extracells.api.IWrenchHandler
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumHand
import net.minecraft.util.math.RayTraceResult

object WrenchHandler : IWrenchHandler {
    override fun canWrench(
        itemStack: ItemStack?,
        user: EntityPlayer?,
        rayTraceResult: RayTraceResult?,
        hand: EnumHand?
    ): Boolean {
        if (itemStack == null || user == null || hand == null || rayTraceResult == null) {
            return false
        }

        val item = itemStack.item ?: return false

        return item is ITool && item.canUse(hand, user, rayTraceResult.blockPos)
    }

    override fun wrenchUsed(
        itemStack: ItemStack?,
        user: EntityPlayer?,
        rayTraceResult: RayTraceResult?,
        hand: EnumHand?
    ) {
        if (itemStack == null || user == null || rayTraceResult == null || hand == null) {
            return
        }

        val item = itemStack.item ?: return

        if (item !is ITool) {
            return
        }

        item.used(hand, user, rayTraceResult.blockPos)
    }
}