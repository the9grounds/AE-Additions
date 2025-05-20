package com.the9grounds.aeadditions.me.storage

import appeng.api.config.IncludeExclude
import appeng.api.storage.cells.ICellHandler
import appeng.api.storage.cells.ISaveProvider
import appeng.core.localization.GuiText
import appeng.core.localization.Tooltips
import com.the9grounds.aeadditions.item.storage.DiskCell
import io.github.projectet.ae2things.AE2Things
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

object ExtendedDiskCellHandler : ICellHandler {
    override fun isCell(`is`: ItemStack?): Boolean {
        return `is`!!.item is DiskCell
    }

    override fun getCellInventory(`is`: ItemStack?, container: ISaveProvider?): ExtendedDiskCellInventory? {
        if (`is` == null) return null
        return ExtendedDiskCellInventory.createInventory(`is`, container, AE2Things.currentStorageManager())
    }

    fun addCellInformationToTooltip(stack: ItemStack, lines: MutableList<Component>) {
        val handler: ExtendedDiskCellInventory? = this.getCellInventory(stack, null as ISaveProvider?)
        if (handler != null) {
            if (handler.hasDiskUUID()) {
                lines.add(
                    Component.literal("Disk UUID: ").withStyle(ChatFormatting.GRAY)
                        .append(Component.literal(handler.diskUUID.toString()).withStyle(ChatFormatting.AQUA))
                )
                lines.add(Tooltips.bytesUsed(handler.usedSpace, handler.totalBytes))
            }

            if (handler.isPreformatted) {
                val list =
                    (if (handler.partitionListMode == IncludeExclude.WHITELIST) GuiText.Included else GuiText.Excluded).text()
                if (handler.isFuzzy) {
                    lines.add(
                        GuiText.Partitioned.withSuffix(" - ").append(list).append(" ").append(GuiText.Fuzzy.text())
                    )
                } else {
                    lines.add(
                        GuiText.Partitioned.withSuffix(" - ").append(list).append(" ").append(GuiText.Precise.text())
                    )
                }
            }
        }
    }
}