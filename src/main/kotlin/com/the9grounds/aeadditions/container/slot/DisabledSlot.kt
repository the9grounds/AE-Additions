package com.the9grounds.aeadditions.container.slot

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraftforge.items.IItemHandler

class DisabledSlot(inv: IItemHandler, slot: Int, x: Int, y: Int) :
    AEASlot(inv, slot, x, y) {
    override fun canTakeStack(playerIn: PlayerEntity): Boolean = false

    override fun isItemValid(stack: ItemStack): Boolean = false
}