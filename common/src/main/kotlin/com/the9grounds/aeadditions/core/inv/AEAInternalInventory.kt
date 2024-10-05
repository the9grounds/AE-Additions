package com.the9grounds.aeadditions.core.inv

import net.minecraft.nbt.CompoundTag
import net.minecraft.world.item.ItemStack
import net.minecraftforge.items.ItemStackHandler

open class AEAInternalInventory(var host: IAEAInventory?, size: Int, maxStack: Int, var filter: ItemFilter?) : ItemStackHandler(size), Iterable<ItemStack?> {
    
    val maxStack = IntArray(size)
    var previousStack = ItemStack.EMPTY
    var isDirty = false
    
    var clientEventsEnabled = false
    
    init {
        this.maxStack.fill(maxStack)
    }
    
    constructor(host: IAEAInventory?, size: Int, maxStack: Int): this(host, size, maxStack, null)
    constructor(host: IAEAInventory?, size: Int): this(host, size, 64, null)
    
    val isEventsEnabled: Boolean
    get() = host != null && !host!!.isRemote || clientEventsEnabled
    
    fun fetchFirstEmptySlot(): Int {
        for (slot in 0 until slots) {
            val stack = getStackInSlot(slot)
            
            if (stack.isEmpty) {
                return slot
            }
        }
        
        return -1
    }

    override fun getSlotLimit(slot: Int): Int = maxStack[slot]

    override fun setStackInSlot(slot: Int, stack: ItemStack) {
        previousStack = getStackInSlot(slot).copy()
        super.setStackInSlot(slot, stack)
    }

    override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
        if (filter != null && !filter!!.allowInsert(this, slot, stack)) {
            return stack
        }
        
        if (!simulate) {
            previousStack = getStackInSlot(slot).copy()
        }
        
        return super.insertItem(slot, stack, simulate)
    }

    override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
        
        if (filter != null && !filter!!.allowExtract(this, slot, amount)) {
            return ItemStack.EMPTY
        }
        
        if (!simulate) {
            previousStack = getStackInSlot(slot).copy()
        }
        
        return super.extractItem(slot, amount, simulate)
    }

    override fun onContentsChanged(slot: Int) {
        
        if (host != null && isEventsEnabled && !isDirty) {
            isDirty = true
            var newStack = getStackInSlot(slot).copy()
            var oldStack = previousStack
            
            var operation = Operation.SET

            if (newStack.isEmpty || oldStack.isEmpty || ItemStack.isSameItem(newStack, oldStack)) {
                if (newStack.count > oldStack.count) {
                    newStack.shrink(oldStack.count)
                    oldStack = ItemStack.EMPTY
                    operation = Operation.INSERT
                } else {
                    oldStack.shrink(newStack.count)
                    newStack = ItemStack.EMPTY
                    operation = Operation.EXTRACT
                }
            }

            host?.onInventoryChanged(this, slot, operation, oldStack, newStack)
            host?.saveChanges()
            previousStack = ItemStack.EMPTY
            isDirty = false
        }
        
        super.onContentsChanged(slot)
    }
    
    fun setMaxStackSize(slot: Int, maxStack: Int) {
        this.maxStack[slot] = maxStack
    }

    override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
        if (this.maxStack[slot] == 0) {
            return false
        }
        
        if (filter != null) {
            return filter!!.allowInsert(this, slot, stack)
        }
        
        return true
    }
    
    open fun writeToNBT(data: CompoundTag, name: String) {
        data.put(name, serializeNBT())
    }
    
    open fun readFromNBT(data: CompoundTag, name: String) {
        deserializeNBT(data.getCompound(name))
    }

    override fun iterator(): Iterator<ItemStack?> {
        return stacks.toList().iterator()
    }
    
    
}