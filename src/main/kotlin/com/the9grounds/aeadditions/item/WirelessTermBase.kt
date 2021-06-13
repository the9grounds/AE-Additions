package com.the9grounds.aeadditions.item

import appeng.api.config.AccessRestriction
import appeng.api.config.Actionable
import com.sun.org.apache.xpath.internal.operations.Bool
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.NonNullList
import net.minecraft.util.text.translation.I18n
import net.minecraft.world.World
import kotlin.math.floor

abstract class WirelessTermBase(override val MAX_POWER: Double = 1600000.0) : PowerItem() {

    override fun getPowerFlow(itemStack: ItemStack): AccessRestriction {
        return AccessRestriction.READ_WRITE
    }

    override fun getDurabilityForDisplay(stack: ItemStack): Double {
        return 1 - getAECurrentPower(stack) / MAX_POWER
    }


    fun canHandle(itemStack: ItemStack?): Boolean {
        return if (itemStack == null) false else itemStack.item == this
    }

    fun getEncryptionKey(itemStack: ItemStack): String {
        return ensureTagCompound(itemStack).getString("key")
    }

    fun setEncryptionKey(itemStack: ItemStack, encKey: String, name: String?) {
        ensureTagCompound(itemStack).setString("key", encKey)
    }

    // TODO: Rename this
    fun hasPower(player: EntityPlayer, amount: Double, itemStack: ItemStack): Boolean {
        return getAECurrentPower(itemStack) >= amount
    }

    fun usePower(player: EntityPlayer, amount: Double, itemStack: ItemStack): Boolean {
        extractAEPower(itemStack, amount, Actionable.MODULATE)

        return true
    }

    override fun getSubItems(tab: CreativeTabs, items: NonNullList<ItemStack>) {
        if (!this.isInCreativeTab(tab)) return

        items.add(ItemStack(this))
        val itemStack = ItemStack(this)
        injectAEPower(itemStack, this.MAX_POWER, Actionable.MODULATE)
        items.add(itemStack)
    }

    override fun showDurabilityBar(stack: ItemStack): Boolean {
        return true
    }

    override fun addInformation(stack: ItemStack, worldIn: World?, tooltip: MutableList<String>, flagIn: ITooltipFlag) {
        val encryptionKey = this.getEncryptionKey(stack)
        val aeCurrentPower = getAECurrentPower(stack)
        tooltip.add(I18n.translateToLocal("gui.appliedenergistics2.StoredEnergy") + ": " + aeCurrentPower + " AE - " + floor(aeCurrentPower / this.MAX_POWER * 1e4) / 1e2 +  "%")
        tooltip.add(I18n.translateToLocal(if (!encryptionKey.isEmpty()) "gui.appliedenergistics2.Linked" else "gui.appliedenergistics2.Unlinked"))
    }

//    abstract fun isInCreativeTab2(targetTab: CreativeTabs): Boolean
}