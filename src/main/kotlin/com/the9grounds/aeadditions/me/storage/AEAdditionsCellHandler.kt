package com.the9grounds.aeadditions.me.storage

import appeng.api.config.IncludeExclude
import appeng.api.storage.cells.ICellHandler
import appeng.api.storage.cells.ISaveProvider
import appeng.core.localization.GuiText
import appeng.core.localization.Tooltips
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

object AEAdditionsCellHandler : ICellHandler {
    override fun isCell(`is`: ItemStack?): Boolean = AEAdditionsCellInventory.isCell(`is`)

    override fun getCellInventory(`is`: ItemStack?, host: ISaveProvider?): AEAdditionsCellInventory? {
        return AEAdditionsCellInventory.createInventory(`is`!!, host)
    }

    fun addCellInformationToTooltip(`is`: ItemStack?, lines: MutableList<Component>) {
        val handler = getCellInventory(`is`, null) ?: return
        lines.add(Tooltips.bytesUsed(handler.getUsedBytes(), handler.getTotalBytes()))
        lines.add(Tooltips.typesUsed(handler.getStoredItemTypes(), handler.getTotalItemTypes()))
        if (handler.isPreformatted) {
            val list =
                (if (handler.partitionListMode == IncludeExclude.WHITELIST) GuiText.Included else GuiText.Excluded)
                    .text()
            if (handler.isFuzzy()) {
                lines.add(GuiText.Partitioned.withSuffix(" - ").append(list).append(" ").append(GuiText.Fuzzy.text()))
            } else {
                lines.add(
                    GuiText.Partitioned.withSuffix(" - ").append(list).append(" ").append(GuiText.Precise.text())
                )
            }
        }
    }
}