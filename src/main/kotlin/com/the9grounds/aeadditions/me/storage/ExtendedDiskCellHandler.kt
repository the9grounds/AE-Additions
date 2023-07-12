package com.the9grounds.aeadditions.me.storage

import appeng.api.storage.cells.ISaveProvider
import com.the9grounds.aeadditions.item.storage.DiskCell
import io.github.projectet.ae2things.storage.DISKCellHandler
import io.github.projectet.ae2things.storage.DISKCellInventory
import net.minecraft.world.item.ItemStack

class ExtendedDiskCellHandler : DISKCellHandler() {
    override fun isCell(`is`: ItemStack?): Boolean {
        return `is`!!.item is DiskCell
    }

    override fun getCellInventory(`is`: ItemStack?, container: ISaveProvider?): DISKCellInventory? {
        return super.getCellInventory(`is`, container)
    }
}