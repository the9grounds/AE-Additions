package com.the9grounds.aeadditions.container.fluid

import net.minecraft.inventory.IInventory
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import com.the9grounds.aeadditions.container.slot.SlotRespective
import com.the9grounds.aeadditions.api.IFluidInterface
import net.minecraftforge.fml.relauncher.SideOnly
import com.the9grounds.aeadditions.gui.gas.GuiFluidInterface
import appeng.api.util.AEPartLocation
import com.the9grounds.aeadditions.tileentity.TileEntityFluidInterface
import net.minecraftforge.fluids.FluidStack
import com.the9grounds.aeadditions.network.packet.part.PacketFluidInterface
import com.the9grounds.aeadditions.container.IContainerListener
import com.the9grounds.aeadditions.part.fluid.PartFluidInterface
import com.the9grounds.aeadditions.util.NetworkUtil
import net.minecraft.inventory.Container
import net.minecraft.inventory.Slot
import net.minecraftforge.fml.relauncher.Side

class ContainerFluidInterface(
    var player: EntityPlayer,
    @JvmField var fluidInterface: IFluidInterface
) : Container(), IContainerListener {
    @SideOnly(Side.CLIENT)
    @JvmField var gui: GuiFluidInterface? = null
    protected fun bindPlayerInventory(inventoryPlayer: IInventory?) {
        for (i in 0..2) {
            for (j in 0..8) {
                addSlotToContainer(
                    Slot(
                        inventoryPlayer, j + i * 9 + 9,
                        8 + j * 18, i * 18 + 149
                    )
                )
            }
        }
        for (i in 0..8) {
            addSlotToContainer(Slot(inventoryPlayer, i, 8 + i * 18, 207)) // 173
        }
    }

    override fun canInteractWith(entityplayer: EntityPlayer): Boolean {
        return true
    }

    private fun getFluidName(side: AEPartLocation): String {
        val fluid = fluidInterface.getFilter(side) ?: return ""
        return fluid.name
    }

    override fun onContainerClosed(player: EntityPlayer) {
        super.onContainerClosed(player)
        if (fluidInterface is TileEntityFluidInterface) {
            (fluidInterface as TileEntityFluidInterface)
                .removeListener(this)
        } else if (fluidInterface is PartFluidInterface) {
            (fluidInterface as PartFluidInterface).removeListener(this)
        }
    }

    //	@Override
    //	protected void retrySlotClick(int p_75133_1_, int p_75133_2_,
    //		boolean p_75133_3_, EntityPlayer p_75133_4_) {
    //
    //	}
    override fun transferStackInSlot(player: EntityPlayer, slotnumber: Int): ItemStack {
        var transferStack = ItemStack.EMPTY
        val slot = inventorySlots[slotnumber]
        if (slot != null && slot.hasStack) {
            val itemStack = slot.stack
            transferStack = itemStack.copy()
            if (slotnumber < 9) {
                if (!mergeItemStack(itemStack, 9, inventorySlots.size, false)) {
                    return ItemStack.EMPTY
                }
            } else if (slotnumber < 36) {
                if (!mergeItemStack(itemStack, 0, 9, false) &&
                    !mergeItemStack(itemStack, 36, inventorySlots.size, false)
                ) {
                    return ItemStack.EMPTY
                }
            } else if (!mergeItemStack(itemStack, 0, 9, false) &&
                !mergeItemStack(itemStack, 9, 36, false)
            ) {
                return ItemStack.EMPTY
            }
            if (itemStack.count == 0) {
                slot.putStack(ItemStack.EMPTY)
            } else {
                slot.onSlotChanged()
            }
        }
        return transferStack
    }

    override fun updateContainer() {
        val fluidStacks = arrayOfNulls<FluidStack>(6)
        val fluidNames = arrayOfNulls<String>(6)
        for (i in 0..5) {
            val location = AEPartLocation.fromOrdinal(i)
            fluidStacks[i] = fluidInterface.getFluidTank(location).fluid
            fluidNames[i] = getFluidName(location)
        }
        NetworkUtil.sendToPlayer(PacketFluidInterface(fluidStacks, fluidNames), player)
    }

    init {
        for (j in 0..8) {
            addSlotToContainer(
                SlotRespective(
                    fluidInterface.patternInventory, j, 8 + j * 18, 115
                )
            )
        }
        bindPlayerInventory(player.inventory)
        if (fluidInterface is TileEntityFluidInterface) {
            (fluidInterface as TileEntityFluidInterface).registerListener(this)
        } else if (fluidInterface is PartFluidInterface) {
            (fluidInterface as PartFluidInterface).registerListener(this)
        }
        if (fluidInterface is TileEntityFluidInterface) {
            (fluidInterface as TileEntityFluidInterface).doNextUpdate = true
        } else if (fluidInterface is PartFluidInterface) {
            (fluidInterface as PartFluidInterface).doNextUpdate = true
        }
    }
}