package com.the9grounds.aeadditions.container.slot

import com.the9grounds.aeadditions.container.AbstractContainer
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.container.Slot
import net.minecraft.item.ItemStack
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.items.IItemHandler
import kotlin.math.min

open class AEASlot(val inv: IItemHandler, slot: Int, x: Int, y: Int) : Slot(Inventory(0), slot, x, y) {
    
    constructor(inv: IItemHandler, slot: Int): this(inv, slot, 0, 0)
    
    override fun onSlotChange(oldStackIn: ItemStack, newStackIn: ItemStack) {
        super.onSlotChange(oldStackIn, newStackIn)
    }
    
    lateinit var container: AbstractContainer<*>

    override fun isItemValid(stack: ItemStack): Boolean = inv.isItemValid(slotIndex, stack)

    override fun getStack(): ItemStack {
        if (!isSlotEnabled) {
            return ItemStack.EMPTY
        }
        
        if (inv.slots < slotIndex) {
            return ItemStack.EMPTY
        }
        
        return inv.getStackInSlot(slotIndex)
    }
    
    val isSlotEnabled: Boolean = true

    override fun putStack(stack: ItemStack) {
        inv.extractItem(slotIndex, Int.MAX_VALUE, false)
        inv.insertItem(slotIndex, stack, false)
    }

    override fun getSlotStackLimit(): Int = inv.getSlotLimit(slotIndex)

    override fun getItemStackLimit(stack: ItemStack): Int = min(slotStackLimit, stack.maxStackSize)

    override fun canTakeStack(playerIn: PlayerEntity): Boolean {
        if (isSlotEnabled) {
            return !inv.extractItem(slotIndex, 1, true).isEmpty
        }
        
        return false
    }

    override fun decrStackSize(amount: Int): ItemStack = inv.extractItem(slotIndex, amount, false)

    override fun isSameInventory(other: Slot): Boolean = other is AEASlot && (other as AEASlot).inv === inv

    @OnlyIn(Dist.CLIENT)
    override fun isEnabled(): Boolean = isSlotEnabled
}