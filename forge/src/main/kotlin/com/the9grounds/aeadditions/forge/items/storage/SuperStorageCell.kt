package com.the9grounds.aeadditions.forge.items.storage

import appeng.api.storage.StorageCells
import com.the9grounds.aeadditions.me.storage.SuperCellHandler.addCellInformationToTooltip
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

class SuperStorageCell(properties: Properties, val component: ItemLike, val housingItem: ItemLike, val _idleDrain: Double, val _bytesPerType: Int, val kiloBytes: Int, val numberOfTypes: Int) : Item(properties) {
    fun getBytes(cellItem: ItemStack): Int = kiloBytes * 1024

    fun getBytesPerType(cellItem: ItemStack): Int  = _bytesPerType

    fun getTotalTypes(cellItem: ItemStack): Int = numberOfTypes

    fun getIdleDrain(): Double = _idleDrain

    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        this.disassembleDrive(player.getItemInHand(hand), level, player);
        return InteractionResultHolder(InteractionResult.sidedSuccess(level.isClientSide), player.getItemInHand(hand));
    }
    
    fun storableInStorageCell(): Boolean = false
    
    fun isStorageCell(itemStack: ItemStack): Boolean = true
    
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