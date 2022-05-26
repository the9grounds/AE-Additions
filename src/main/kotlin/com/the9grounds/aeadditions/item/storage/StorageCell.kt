package com.the9grounds.aeadditions.item.storage

import appeng.api.config.FuzzyMode
import appeng.api.stacks.AEKeyType
import appeng.items.contents.CellConfig
import appeng.util.ConfigInventory
import com.the9grounds.aeadditions.api.IAEAdditionsStorageCell
import com.the9grounds.aeadditions.me.storage.AEAdditionsCellHandler.addCellInformationToTooltip
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.Level
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

class StorageCell(properties: Properties, val component: ItemLike, val housingItem: ItemLike, val _idleDrain: Double, val _bytesPerType: Int, val kiloBytes: Int, val numberOfTypes: Int, val _keyType: AEKeyType) : Item(properties), IAEAdditionsStorageCell {
    override fun getBytes(cellItem: ItemStack): Int = kiloBytes * 1024

    override fun getBytesPerType(cellItem: ItemStack): Int  = _bytesPerType

    override fun getTotalTypes(cellItem: ItemStack): Int = numberOfTypes

    override fun getIdleDrain(): Double = _idleDrain

    override fun isEditable(`is`: ItemStack?): Boolean = true

    override fun getKeyType(): AEKeyType = _keyType

    override fun getConfigInventory(`is`: ItemStack?): ConfigInventory = CellConfig.create(_keyType.filter(), `is`)

    override fun getFuzzyMode(`is`: ItemStack?): FuzzyMode {
        val fz = `is`!!.orCreateTag.getString("FuzzyMode")
        return if (fz.isEmpty()) {
            FuzzyMode.IGNORE_ALL
        } else try {
            FuzzyMode.valueOf(fz)
        } catch (t: Throwable) {
            FuzzyMode.IGNORE_ALL
        }
    }

    override fun setFuzzyMode(`is`: ItemStack?, fzMode: FuzzyMode?) {
        `is`!!.orCreateTag.putString("FuzzyMode", fzMode!!.name)
    }

    @OnlyIn(Dist.CLIENT)
    override fun appendHoverText(
        stack: ItemStack?,
        level: Level?,
        lines: MutableList<Component?>,
        advancedTooltips: TooltipFlag?
    ) {
        addCellInformationToTooltip(stack, lines)
    }
}