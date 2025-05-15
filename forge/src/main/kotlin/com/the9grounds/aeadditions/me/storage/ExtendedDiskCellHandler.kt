package com.the9grounds.aeadditions.me.storage

import com.the9grounds.aeadditions.forge.items.storage.DiskCell
import io.github.projectet.ae2things.storage.DISKCellHandler
import net.minecraft.world.item.ItemStack

class ExtendedDiskCellHandler : DISKCellHandler() {
    override fun isCell(`is`: ItemStack?): Boolean {
        return `is`!!.item is DiskCell
    }
}