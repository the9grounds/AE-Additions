package com.the9grounds.aeadditions.item

import com.the9grounds.aeadditions.Constants
import com.the9grounds.aeadditions.integration.opencomputers.UpgradeItemAEBase
import com.the9grounds.aeadditions.models.ModelManager
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

object ItemOCUpgrade: UpgradeItemAEBase() {

    init {
        setHasSubtypes(true)
    }

    override fun getTranslationKey(): String = super.getTranslationKey().replace("item." + Constants.MOD_ID, "com.the9grounds.aeadditions.item")

    override fun getTranslationKey(stack: ItemStack): String = translationKey

    override fun getItemStackDisplayName(stack: ItemStack): String {
        val tier = 3 - stack.itemDamage

        return super.getItemStackDisplayName(stack) + " (Tier " + tier + ")"
    }

    @SideOnly(Side.CLIENT)
    override fun getSubItems(tab: CreativeTabs, items: NonNullList<ItemStack>) {
        if (!this.isInCreativeTab(tab)) {
            return
        }

        items.add(ItemStack(this, 1, 2))
        items.add(ItemStack(this, 1, 1))
        items.add(ItemStack(this, 1, 0))
    }

    @SideOnly(Side.CLIENT)
    override fun registerModel(item: Item?, manager: ModelManager?) {
        var i = 0
        while (i < 3) {
            manager?.registerItemModel(item, i)
            i += 1
        }
    }
}