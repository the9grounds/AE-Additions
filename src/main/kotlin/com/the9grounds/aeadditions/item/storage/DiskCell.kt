package com.the9grounds.aeadditions.item.storage

import appeng.api.config.FuzzyMode
import appeng.api.stacks.AEItemKey
import appeng.api.stacks.AEKey
import appeng.api.stacks.AEKeyType
import appeng.api.storage.StorageCells.getCellInventory
import appeng.api.storage.cells.CellState
import appeng.api.upgrades.IUpgradeInventory
import appeng.api.upgrades.UpgradeInventories
import appeng.hooks.AEToolItem
import appeng.items.contents.CellConfig
import appeng.util.ConfigInventory
import appeng.util.InteractionUtil
import io.github.projectet.ae2things.storage.DISKCellHandler
import io.github.projectet.ae2things.storage.IDISKCellItem
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextComponent
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.Level


/**
 * Need to overwrite as the original one puts it into the ae2things creative tab
 */
class DiskCell(properties: Item.Properties, private val _keyType: AEKeyType, val component: ItemLike, val housing: ItemLike, val kilobytes: Int, val _idleDrain: Double) : DiskCellWithoutMod(properties), IDISKCellItem, AEToolItem {
    override fun getKeyType(): AEKeyType {
        return _keyType
    }

    override fun isBlackListed(cellItem: ItemStack?, requestedAddition: AEKey?): Boolean {
        if (requestedAddition is AEItemKey) {
            return super.isBlackListed(cellItem, requestedAddition)
        }
        
        return false;
    }

    override fun getBytes(cellItem: ItemStack?): Int {
        return kilobytes * 1000
    }

    override fun getIdleDrain(): Double {
        return _idleDrain
    }

    override fun isEditable(`is`: ItemStack?): Boolean {
        return true
    }

    override fun getConfigInventory(`is`: ItemStack?): ConfigInventory? {
        return CellConfig.create(keyType.filter(), `is`)
    }

    override fun getFuzzyMode(`is`: ItemStack): FuzzyMode? {
        val fz = `is`.orCreateTag.getString("FuzzyMode")
        return if (fz.isEmpty()) {
            FuzzyMode.IGNORE_ALL
        } else try {
            FuzzyMode.valueOf(fz)
        } catch (t: Throwable) {
            FuzzyMode.IGNORE_ALL
        }
    }

    override fun setFuzzyMode(`is`: ItemStack, fzMode: FuzzyMode) {
        `is`.orCreateTag.putString("FuzzyMode", fzMode.name)
    }

    override fun use(level: Level, player: Player, hand: InteractionHand?): InteractionResultHolder<ItemStack>? {
        disassembleDrive(player.getItemInHand(hand), level, player)
        return InteractionResultHolder(
            InteractionResult.sidedSuccess(level.isClientSide()),
            player.getItemInHand(hand)
        )
    }
    
    override fun getUpgrades(`is`: ItemStack?): IUpgradeInventory {
        return UpgradeInventories.forItem(`is`, 2)
    }

    private fun disassembleDrive(stack: ItemStack, level: Level, player: Player?): Boolean {
        if (InteractionUtil.isInAlternateUseMode(player)) {
            if (level.isClientSide()) {
                return false
            }
            val playerInventory = player!!.inventory
            val inv = getCellInventory(stack, null)
            if (inv != null && playerInventory.getSelected() == stack) {
                val list = inv.availableStacks
                if (list.isEmpty) {
                    playerInventory.setItem(playerInventory.selected, ItemStack.EMPTY)

                    // drop core
                    playerInventory.placeItemBackInInventory(ItemStack(component))

                    // drop upgrades
                    for (upgrade in getUpgrades(stack)) {
                        playerInventory.placeItemBackInInventory(upgrade)
                    }

                    // drop empty storage cell case
                    playerInventory.placeItemBackInInventory(ItemStack(housing))
                    return true
                }
            }
        }
        return false
    }

    override fun onItemUseFirst(stack: ItemStack?, context: UseOnContext?): InteractionResult {
        return if (disassembleDrive(
                stack!!,
                context!!.level,
                context!!.player
            )
        ) InteractionResult.sidedSuccess(context.level.isClientSide()) else InteractionResult.PASS
    }

    override fun appendHoverText(
        stack: ItemStack?,
        world: Level?,
        tooltip: MutableList<Component?>,
        context: TooltipFlag?
    ) {
        tooltip.add(
            TextComponent("Deep Item Storage disK - Storage for dummies")
                .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC)
        )
        addCellInformationToTooltip(stack, tooltip)
    }

    fun getColor(stack: ItemStack?, tintIndex: Int): Int {
        return if (tintIndex == 1) {
            // Determine LED color
            val cellInv = DISKCellHandler.INSTANCE.getCellInventory(stack, null)
            val cellStatus = if (cellInv != null) cellInv.clientStatus else CellState.EMPTY
            cellStatus.stateColor
        } else {
            // White
            0xFFFFFF
        }
    }
    
}