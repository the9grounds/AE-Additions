package com.the9grounds.aeadditions.container

import com.the9grounds.aeadditions.container.ContainerBase
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.IInventory
import com.the9grounds.aeadditions.container.slot.SlotRespective
import appeng.api.AEApi
import net.minecraft.item.ItemStack
import appeng.api.util.DimensionalCoord
import appeng.api.implementations.guiobjects.IGuiItem
import appeng.api.implementations.guiobjects.INetworkTool
import com.the9grounds.aeadditions.container.slot.SlotNetworkTool

abstract class ContainerUpgradeable : ContainerBase() {
    fun bindUpgradeInventory(upgradeable: IUpgradeable) {
        val upgradeInventory = upgradeable.upgradeInventory
        for (i in 0 until upgradeInventory.sizeInventory) {
            addSlotToContainer(SlotRespective(upgradeInventory, i, 187, i * 18 + 8))
        }
    }

    fun bindNetworkToolInventory(inv: InventoryPlayer?, upgradeable: IUpgradeable, x: Int = 187, y: Int = 102) {
        if (inv == null) {
            return
        }

        val networkToolDefinition = AEApi.instance().definitions().items().networkTool()
        for (i in 0 until inv.sizeInventory) {
            val stack = inv.getStackInSlot(i)
            if (stack != null && networkToolDefinition.isSameAs(stack)) {
                lockPlayerInventorySlot(i)
                val coord = upgradeable.location
                val guiItem = stack.item as IGuiItem
                val networkTool = guiItem.getGuiObject(stack, coord.world, coord.pos) as INetworkTool
                for (j in 0..2) {
                    for (k in 0..2) {
                        addSlotToContainer(SlotNetworkTool(networkTool, k + j * 3, x + k * 18, j * 18 + y))
                    }
                }
                return
            }
        }
    }
}