package com.the9grounds.aeadditions.integration.wct

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import p455w0rd.ae2wtlib.api.WTApi
import p455w0rd.wct.api.WCTApi

object WirelessCrafting {

    @JvmStatic fun openCraftingTerminal(player: EntityPlayer, slot: Int) = WCTApi.instance()?.openWCTGui(player, false, slot)

    @JvmStatic fun openCraftingTerminal(player: EntityPlayer, isBauble: Boolean, slot: Int) = WCTApi.instance()?.openWCTGui(player, isBauble, slot)

    @JvmStatic fun getBoosterItem() = ItemStack(Item.getByNameOrId("wct:infinity_booster_card")!!)

    @JvmStatic fun isBoosterEnabled() = WTApi.instance()?.config?.isInfinityBoosterCardEnabled

    @JvmStatic fun getCraftingTerminal() = ItemStack(Item.getByNameOrId("wct:wct")!!)
}