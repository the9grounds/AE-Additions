package com.the9grounds.aeadditions.util

import appeng.api.features.IWirelessTermHandler
import appeng.api.util.IConfigManager
import com.the9grounds.aeadditions.api.IWirelessFluidTermHandler
import com.the9grounds.aeadditions.api.IWirelessGasTermHandler
import com.the9grounds.aeadditions.item.ItemWirelessTerminalUniversal
import com.the9grounds.aeadditions.registries.ItemEnum
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack

object HandlerUniversalWirelessTerminal : IWirelessTermHandler, IWirelessFluidTermHandler, IWirelessGasTermHandler {
    private val terminal
    get() = ItemEnum.UNIVERSALTERMINAL.item as ItemWirelessTerminalUniversal

    override fun getEncryptionKey(item: ItemStack): String = terminal.getEncryptionKey(item)

    override fun setEncryptionKey(item: ItemStack, encKey: String, name: String) = terminal.setEncryptionKey(item, encKey, name)

    override fun canHandle(item: ItemStack?): Boolean = terminal.canHandle(item)

    override fun hasPower(player: EntityPlayer, amount: Double, itemStack: ItemStack): Boolean = terminal.hasPower(player, amount, itemStack)

    override fun getConfigManager(itemStack: ItemStack?): IConfigManager = terminal.getConfigManager(itemStack)

    override fun isItemNormalWirelessTermToo(itemStack: ItemStack?): Boolean = terminal.isItemNormalWirelessTermToo(itemStack)

    override fun usePower(player: EntityPlayer, amount: Double, itemStack: ItemStack): Boolean = terminal.usePower(player, amount, itemStack)
}