package com.the9grounds.aeadditions.inventory

import appeng.api.AEApi
import net.minecraft.item.ItemStack

open class CraftingUpgradeInventory(listener: IInventoryListener) : InventoryPlain("", 5, 1, listener) {

    override fun isItemValidForSlot(i: Int, itemStack: ItemStack?): Boolean {
        if (itemStack == null) {
            return false
        }

        val materials = AEApi.instance().definitions().materials()

        if (materials.cardSpeed().isSameAs(itemStack)) {
            return true
        }

        return false
    }
}