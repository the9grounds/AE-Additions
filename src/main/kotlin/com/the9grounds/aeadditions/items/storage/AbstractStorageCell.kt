package com.the9grounds.aeadditions.items.storage

import appeng.api.config.FuzzyMode
import appeng.api.implementations.items.IStorageCell
import appeng.api.storage.data.IAEStack
import appeng.items.contents.CellConfig
import appeng.items.contents.CellUpgrades
import com.the9grounds.aeadditions.api.IAEAdditionsStorageCell
import com.the9grounds.aeadditions.integration.appeng.AppEng
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.text.ITextComponent
import net.minecraft.world.World
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.items.IItemHandler

abstract class AbstractStorageCell<T>(props: Properties, val component: Item, kiloBytes: Int, val numberOfTypes: Int) : Item(props), IAEAdditionsStorageCell<T> where T : IAEStack<T> {

    private val totalBytes: Int = kiloBytes * 1024

    override fun isEditable(`is`: ItemStack?): Boolean = true

    override fun getUpgradesInventory(`is`: ItemStack?): IItemHandler = CellUpgrades(`is`, 2)

    override fun getConfigInventory(`is`: ItemStack?): IItemHandler = CellConfig(`is`)

    override fun getFuzzyMode(itemStack: ItemStack?): FuzzyMode {

        if (itemStack == null) {
            return FuzzyMode.IGNORE_ALL
        }

        val fuzzy = itemStack.getOrCreateTag().getString("FuzzyMode")

        if (fuzzy.isEmpty()) {
            return FuzzyMode.IGNORE_ALL
        }

        return try {
            FuzzyMode.valueOf(fuzzy)
        } catch(e: Throwable) {
            FuzzyMode.IGNORE_ALL
        }
    }

    override fun setFuzzyMode(itemStack: ItemStack?, fzMode: FuzzyMode?) {
        itemStack?.getOrCreateTag()?.putString("FuzzyMode", fzMode!!.name)
    }

    override fun getBytes(cellItem: ItemStack): Int = totalBytes

    override fun getTotalTypes(cellItem: ItemStack): Int = numberOfTypes

    override fun isBlackListed(cellItem: ItemStack, requestedAddition: T): Boolean = false

    override fun storableInStorageCell(): Boolean = false

    override fun isStorageCell(i: ItemStack): Boolean = true

    @OnlyIn(Dist.CLIENT)
    override fun addInformation(
        stack: ItemStack,
        worldIn: World?,
        tooltip: MutableList<ITextComponent>,
        flagIn: ITooltipFlag
    ) {
        if (AppEng.API == null) {
            return
        }
        AppEng.API!!.client().addCellInformation(AppEng.API!!.registries().cell().getCellInventory(stack, null, this.getChannel()), tooltip)
    }
}