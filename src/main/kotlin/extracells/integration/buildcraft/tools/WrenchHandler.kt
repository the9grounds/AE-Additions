package extracells.integration.buildcraft.tools

import buildcraft.api.tools.IToolWrench
import extracells.api.IWrenchHandler
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

        if (item !is IToolWrench) {
            return false
        }

        return item.canWrench(user, hand, itemStack, rayTraceResult)
    }

    override fun wrenchUsed(itemStack: ItemStack?, user: EntityPlayer?, rayTraceResult: RayTraceResult?, hand: EnumHand?) {
        if (itemStack == null) {
            return
        }
        val item = itemStack.item

        if (item is IToolWrench) {
            item.wrenchUsed(user, hand, itemStack, rayTraceResult)
        }

    }

}