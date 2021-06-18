package com.the9grounds.aeadditions.inventory

import appeng.api.AEApi
import appeng.api.config.Upgrades
import com.the9grounds.aeadditions.registries.BlockEnum
import net.minecraft.item.ItemStack

open class CraftingUpgradeInventory(listener: IInventoryListener, val enum: BlockEnum) : InventoryPlain("", 5, 1, listener) {

    val installedUpgrades = mutableMapOf<Upgrades, Int>()

    override fun onContentsChanged() {
        installedUpgrades.clear()
        super.onContentsChanged()
    }

    override fun isItemValidForSlot(i: Int, itemStack: ItemStack?): Boolean {
        if (itemStack == null) {
            return false
        }

        if (installedUpgrades.isEmpty()) {
            for (i in 0 until sizeInventory) {
                val currentStack = getStackInSlot(i)

                if (currentStack != null) {
                    if (AEApi.instance().definitions().materials().cardSpeed().isSameAs(currentStack)) {
                        installedUpgrades[Upgrades.SPEED] = (installedUpgrades[Upgrades.SPEED] ?: 0) + 1
                    }
                    if (AEApi.instance().definitions().materials().cardCapacity().isSameAs(currentStack)) {
                        installedUpgrades[Upgrades.CAPACITY] = (installedUpgrades[Upgrades.CAPACITY] ?: 0) + 1
                    }
                }
            }
        }


        val materials = AEApi.instance().definitions().materials()

        if (materials.cardSpeed().isSameAs(itemStack)) {

            if (installedUpgrades[Upgrades.SPEED] ?: 0 < enum.upgrades[Upgrades.SPEED] ?: 0) {
                return true
            }

            return false
        }

        if (materials.cardCapacity().isSameAs(itemStack)) {
            if (installedUpgrades[Upgrades.CAPACITY] ?: 0 < enum.upgrades[Upgrades.CAPACITY] ?: 0) {
                return true
            }

            return false
        }

        return false
    }
}