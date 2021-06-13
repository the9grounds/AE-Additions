package com.the9grounds.aeadditions.item

import com.the9grounds.aeadditions.Constants
import com.the9grounds.aeadditions.api.AEAApi
import com.the9grounds.aeadditions.api.IWirelessGasTermHandler
import com.the9grounds.aeadditions.models.ModelManager
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

object ItemWirelessTerminalGas : WirelessTermBase(), IWirelessGasTermHandler {

    init {
        AEAApi.instance().registerWirelessTermHandler(this)
    }

    override fun getTranslationKey(stack: ItemStack): String {
        return super.getTranslationKey(stack).replace("item." + Constants.MOD_ID, "com.the9grounds.aeadditions.item")
    }

    override fun isItemNormalWirelessTermToo(`is`: ItemStack?): Boolean = false

    override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        return ActionResult(EnumActionResult.SUCCESS, AEAApi.instance().openWirelessGasTerminal(player, hand, world))
    }

    @SideOnly(Side.CLIENT)
    override fun registerModel(item: Item?, manager: ModelManager?) {
        manager?.registerItemModel(item, 0, "terminals/fluid_wireless")
    }
}