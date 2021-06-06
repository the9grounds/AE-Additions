package extracells.integration.appeng

import appeng.api.implementations.items.IAEWrench
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
        if (itemStack == null || rayTraceResult == null) {
            return false
        }

        val item = itemStack.item

        if (item !is IAEWrench) {
            return false
        }

        return item.canWrench(itemStack, user, rayTraceResult!!.blockPos)

    }

    override fun wrenchUsed(item: ItemStack?, user: EntityPlayer?, rayTraceResult: RayTraceResult?, hand: EnumHand?) {
        //
    }


}