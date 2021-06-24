package com.the9grounds.aeadditions.inventory

import com.the9grounds.aeadditions.item.ItemFluid.Companion.getFluid
import com.the9grounds.aeadditions.item.ItemFluid.Companion.setFluid
import com.the9grounds.aeadditions.registries.ItemEnum
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.FluidUtil

class ECFluidFilterInventory(customName: String?, size: Int, private val cellItem: ItemStack) :
    InventoryPlain(customName, size, 1) {
    override fun isItemValidForSlot(index: Int, itemStack: ItemStack): Boolean {
        if (itemStack == null || itemStack.isEmpty) {
            return false
        }
        if (itemStack.item === ItemEnum.FLUIDITEM.item) {
            val fluidStack = getFluid(itemStack)
            var fluidName = fluidStack?.fluid?.name ?: ""
            for (slotStack in slots) {
                if (slotStack == null || slotStack.isEmpty) {
                    continue
                }
                val itemFluidName: String = getFluid(itemStack)?.fluid?.name ?: ""
                if (itemFluidName == fluidName) {
                    return false
                }
            }
            return true
        }
        val stack = FluidUtil.getFluidContained(itemStack) ?: return false
        val fluidName = stack.fluid.name
        for (slotStack in slots) {
            if (slotStack == null || slotStack.isEmpty) {
                continue
            }
            val itemFluidName: String = getFluid(itemStack)?.fluid?.name ?: ""
            if (itemFluidName == fluidName) {
                return false
            }
        }
        return true
    }

    override fun markDirty() {
        val tag: NBTTagCompound?
        tag = if (cellItem.hasTagCompound()) {
            cellItem.tagCompound
        } else {
            NBTTagCompound()
        }
        tag!!.setTag("filter", writeToNBT())
    }

    override fun setInventorySlotContents(index: Int, itemStack: ItemStack) {
        if (itemStack == null || itemStack.isEmpty) {
            super.setInventorySlotContents(index, ItemStack.EMPTY)
            return
        }
        var fluidStack: FluidStack? = null
        if (itemStack.item === ItemEnum.FLUIDITEM.item) {
            fluidStack = getFluid(itemStack) ?: return
        } else {
            if (!isItemValidForSlot(index, itemStack)) {
                return
            }
            fluidStack = FluidUtil.getFluidContained(itemStack)
            if (fluidStack == null) {
                super.setInventorySlotContents(index, ItemStack.EMPTY)
                return
            }
            if (fluidStack.fluid == null) {
                super.setInventorySlotContents(index, ItemStack.EMPTY)
                return
            }
        }
        if (fluidStack == null) {
            super.setInventorySlotContents(index, ItemStack.EMPTY)
            return
        }
        val stack = ItemStack(ItemEnum.FLUIDITEM.item)
        setFluid(stack, fluidStack)
        super.setInventorySlotContents(index, stack)
    }

    init {
        if (cellItem.hasTagCompound()) {
            if (cellItem.tagCompound!!.hasKey("filter")) {
                readFromNBT(cellItem.tagCompound!!.getTagList("filter", 10))
            }
        }
    }
}