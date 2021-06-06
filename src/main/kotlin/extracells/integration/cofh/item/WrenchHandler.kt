package extracells.integration.cofh.item

import buildcraft.api.tools.IToolWrench
import cofh.api.item.IToolHammer
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
        if (itemStack == null || rayTraceResult == null) {
            return false
        }
        val item = itemStack.item

        if (item !is IToolHammer) {
            return false
        }

        return item.isUsable(itemStack, user, rayTraceResult.blockPos)
    }

    override fun wrenchUsed(
        itemStack: ItemStack?,
        user: EntityPlayer?,
        rayTraceResult: RayTraceResult?,
        hand: EnumHand?
    ) {
        if (itemStack == null || rayTraceResult == null) {
            return
        }
        val item = itemStack.item

        if (item is IToolHammer) {
            item.toolUsed(itemStack, user, rayTraceResult.blockPos)
        }
    }
}