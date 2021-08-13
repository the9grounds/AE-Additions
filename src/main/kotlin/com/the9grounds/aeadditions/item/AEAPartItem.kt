package com.the9grounds.aeadditions.item

import appeng.api.parts.IPartItem
import appeng.core.Api
import com.the9grounds.aeadditions.integration.appeng.AppEng
import com.the9grounds.aeadditions.parts.AEABasePart
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUseContext
import net.minecraft.util.ActionResultType

class AEAPartItem<T: AEABasePart>(properties: Properties, val factory: (ItemStack) -> T) : Item(properties), IPartItem<T> {

    override fun onItemUse(context: ItemUseContext): ActionResultType? {
        val player = context.player
        val held = player!!.getHeldItem(context.hand)
        return if (held.item !== this) {
            ActionResultType.PASS
        } else AppEng.API!!.partHelper().placeBus(
            held, context.pos, context.face, player,
            context.hand, context.world
        )
    }
    
    override fun createPart(`is`: ItemStack): T = factory(`is`)
}