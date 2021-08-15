package com.the9grounds.aeadditions.registries

import appeng.api.util.AEColor
import appeng.client.render.StaticItemColor
import com.the9grounds.aeadditions.item.AEAPartItem
import net.minecraft.client.renderer.color.ItemColors

object ItemColors {
    
    fun init(itemColors: ItemColors) {
        val color = AEColor.TRANSPARENT
        Items.ITEMS.forEach { 
            if (it is AEAPartItem<*>) {
                itemColors.register(StaticItemColor(color), it)
            }
        }
    }
}