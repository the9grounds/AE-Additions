package com.the9grounds.aeadditions.forge.items.storage

import appeng.api.config.FuzzyMode
import appeng.api.stacks.AEKeyType
import appeng.api.storage.StorageCells
import appeng.api.storage.cells.ICellWorkbenchItem
import appeng.api.upgrades.IUpgradeInventory
import appeng.api.upgrades.UpgradeInventories
import appeng.items.contents.CellConfig
import appeng.util.ConfigInventory
import com.the9grounds.aeadditions.api.IAEAdditionsStorageCell
import com.the9grounds.aeadditions.me.storage.AEAdditionsCellHandler.addCellInformationToTooltip
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.Level

class StorageCell(properties: Properties, val component: ItemLike, val housingItem: ItemLike, val _idleDrain: Double, val _bytesPerType: Int, val kiloBytes: Int, val numberOfTypes: Int, val _keyType: AEKeyType) : Item(properties), IAEAdditionsStorageCell,
    ICellWorkbenchItem {
    override fun getBytes(cellItem: ItemStack): Int = kiloBytes * 1024

    override fun getBytesPerType(cellItem: ItemStack): Int  = _bytesPerType

    override fun getTotalTypes(cellItem: ItemStack): Int = numberOfTypes

    override fun getIdleDrain(): Double = _idleDrain

    override fun isEditable(`is`: ItemStack?): Boolean = true

    override fun getKeyType(): AEKeyType = _keyType

    override fun getConfigInventory(`is`: ItemStack?): ConfigInventory = CellConfig.create(_keyType.filter(), `is`)

    override fun getUpgrades(stack: ItemStack?): IUpgradeInventory = UpgradeInventories.forItem(stack, 4)

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

    // Forge relic, this method is called when activating a block, but this function is called first
//    override fun onItemUseFirst(stack: ItemStack?, context: UseOnContext?): InteractionResult {
//        return if (this.disassembleDrive(stack, context!!.level, context.player!!)) {
//            InteractionResult.sidedSuccess(context.level.isClientSide)
//        } else {
//            InteractionResult.PASS
//        }
//    }

    override fun appendHoverText(
        stack: ItemStack?,
        level: Level?,
        lines: MutableList<Component?>,
        advancedTooltips: TooltipFlag?
    ) {
        addCellInformationToTooltip(stack, lines)
    }
}