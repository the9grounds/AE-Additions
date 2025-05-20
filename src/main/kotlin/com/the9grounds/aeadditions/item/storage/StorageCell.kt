package com.the9grounds.aeadditions.item.storage

import appeng.api.config.FuzzyMode
import appeng.api.ids.AEComponents
import appeng.api.stacks.AEKeyType
import appeng.api.storage.StorageCells
import appeng.api.storage.cells.IBasicCellItem
import appeng.api.upgrades.IUpgradeInventory
import appeng.api.upgrades.UpgradeInventories
import appeng.items.contents.CellConfig
import appeng.util.ConfigInventory
import com.the9grounds.aeadditions.Logger
import com.the9grounds.aeadditions.api.IAEAdditionsStorageCell
import com.the9grounds.aeadditions.me.storage.AEAdditionsCellHandler
import net.minecraft.network.chat.Component
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
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

class StorageCell(properties: Properties, val component: ItemLike, val housingItem: ItemLike, val _idleDrain: Double, val _bytesPerType: Int, val kiloBytes: Int, val numberOfTypes: Int, val _keyType: AEKeyType) : Item(properties),
    IBasicCellItem, IAEAdditionsStorageCell {
    override fun getBytes(cellItem: ItemStack): Int = kiloBytes * 1024

    override fun getBytesPerType(cellItem: ItemStack): Int  = _bytesPerType

    override fun getTotalTypes(cellItem: ItemStack): Int = numberOfTypes

    override fun getIdleDrain(): Double = _idleDrain

    override fun isEditable(`is`: ItemStack?): Boolean = true

    override fun getKeyType(): AEKeyType = _keyType

    override fun getConfigInventory(`is`: ItemStack?): ConfigInventory = CellConfig.create(mutableSetOf(_keyType), `is`)

    override fun getUpgrades(stack: ItemStack?): IUpgradeInventory = UpgradeInventories.forItem(stack, 4)

    override fun getFuzzyMode(`is`: ItemStack?): FuzzyMode {
        return `is`?.getOrDefault(AEComponents.STORAGE_CELL_FUZZY_MODE, FuzzyMode.IGNORE_ALL) ?: FuzzyMode.IGNORE_ALL
    }

    override fun setFuzzyMode(`is`: ItemStack?, fzMode: FuzzyMode?) {
        if (`is` == null) {
            Logger.error("itemStack is null?")
            return
        }

        `is`.set(AEComponents.STORAGE_CELL_FUZZY_MODE, fzMode ?: FuzzyMode.IGNORE_ALL)
    }

    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        this.disassembleDrive(player.getItemInHand(hand), level, player);
        return InteractionResultHolder(InteractionResult.sidedSuccess(level.isClientSide), player.getItemInHand(hand));
    }
    
    private fun disassembleDrive(stack: ItemStack?, level: Level, player: Player): Boolean {
        if (player.isCrouching) {
            if (level.isClientSide) {
                return false
            }
            
            val inventory = player.inventory
            val inv = StorageCells.getCellInventory(stack, null);
            if (inv !== null && inventory.getSelected() == stack) {
                var list = inv.getAvailableStacks();
                
                if (list.isEmpty) {
                    inventory.setItem(inventory.selected, ItemStack.EMPTY);
                    
                    inventory.placeItemBackInInventory(ItemStack(component))
                    
                    this.getUpgrades(stack).forEach { inventory.placeItemBackInInventory(it) }
                    
                    inventory.placeItemBackInInventory(ItemStack(housingItem))
                    
                    return true;
                }
            }
        }
        
        return false;
    }

    override fun onItemUseFirst(stack: ItemStack?, context: UseOnContext?): InteractionResult {
        return if (this.disassembleDrive(stack, context!!.level, context.player!!)) {
            InteractionResult.sidedSuccess(context.level.isClientSide)
        } else {
            InteractionResult.PASS
        }
    }

    @OnlyIn(Dist.CLIENT)
    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltipComponents: MutableList<Component>,
        tooltipFlag: TooltipFlag
    ) {
        AEAdditionsCellHandler.addCellInformationToTooltip(stack, tooltipComponents)
    }
}