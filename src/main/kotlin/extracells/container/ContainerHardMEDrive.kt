package extracells.container

import appeng.api.AEApi
import extracells.container.slot.SlotRespective
import extracells.tileentity.TileEntityHardMeDrive
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.Container
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack

class ContainerHardMEDrive(val inventory: InventoryPlayer, val tile: TileEntityHardMeDrive): Container() {

    init {
        for (i in 0..2) {
            addSlotToContainer(object : SlotRespective(tile.inventory, i, 80, 17 + i * 18) {
                override fun isItemValid(itemstack: ItemStack): Boolean {
                    return AEApi.instance().registries().cell().isCellHandled(itemstack)
                }
            })
        }

        bindPlayerInventory()
    }

    protected fun bindPlayerInventory() {
        for (i in 0..2) {
            for (j in 0..8) {
                addSlotToContainer(Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18))
            }
        }

        for (i in 0..8) {
            addSlotToContainer(Slot(inventory, i, 8 + i * 18, 142))
        }
    }

    override fun transferStackInSlot(playerIn: EntityPlayer, index: Int): ItemStack {
        var itemStack = ItemStack.EMPTY
        val slot = inventorySlots[0]

        if (slot != null && slot.hasStack) {
            val itemStack1 = slot.stack

            itemStack = itemStack1.copy()

            if (AEApi.instance().registries().cell().isCellHandled(itemStack)) {
                if (index < 3) {
                    if (!mergeItemStack(itemStack1, 3, 38, false)) {
                        return ItemStack.EMPTY
                    }
                } else if (!mergeItemStack(itemStack1, 0, 3, false)) {
                    return ItemStack.EMPTY
                }

                if (itemStack1.count == 0) {
                    slot.putStack(ItemStack.EMPTY)
                } else {
                    slot.onSlotChanged()
                }
            } else {
                return ItemStack.EMPTY
            }
        }

        return itemStack
    }

    override fun canInteractWith(player: EntityPlayer): Boolean {
        if (tile.hasWorld()) {
            return tile.isUseableByPlayer(player)
        }
        return false
    }
}