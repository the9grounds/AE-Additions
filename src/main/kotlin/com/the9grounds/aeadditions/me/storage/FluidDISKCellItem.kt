package com.the9grounds.aeadditions.me.storage

import appeng.api.stacks.AEItemKey
import appeng.api.stacks.AEKey
import appeng.api.stacks.AEKeyType
import appeng.api.storage.cells.IBasicCellItem
import appeng.api.storage.cells.ICellWorkbenchItem
import appeng.me.cells.BasicCellHandler
import appeng.util.ConfigInventory
import com.google.common.base.Preconditions
import io.github.projectet.ae2things.storage.DISKCellHandler
import io.github.projectet.ae2things.storage.IDISKCellItem
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

 interface IFluidDISKCellItem : IDISKCellItem {
    override fun isBlackListed(cellItem: ItemStack?, requestedAddition: AEKey): Boolean {
        return if ((requestedAddition as AEItemKey).item is IBasicCellItem) {
            BasicCellHandler.INSTANCE.getCellInventory(requestedAddition.toStack(), null)!!.usedBytes > 0
        } else false
    }

    override fun storableInStorageCell(): Boolean {
        return false
    }

    override fun isStorageCell(i: ItemStack?): Boolean {
        return true
    }

    override fun getConfigInventory(`is`: ItemStack): ConfigInventory

    override fun addCellInformationToTooltip(`is`: ItemStack, lines: List<Component?>?) {
        Preconditions.checkArgument(`is`.item === this)
        DISKCellHandler.INSTANCE.addCellInformationToTooltip(`is`, lines)
    }
}