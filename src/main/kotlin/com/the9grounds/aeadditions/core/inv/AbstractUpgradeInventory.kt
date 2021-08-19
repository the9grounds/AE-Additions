package com.the9grounds.aeadditions.core.inv

import appeng.api.config.Upgrades
import appeng.api.implementations.items.IUpgradeModule
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.CompoundNBT
import net.minecraftforge.items.IItemHandler
import kotlin.math.min

abstract class AbstractUpgradeInventory(private val parent: IAEAInventory?, size: Int) : AEAInternalInventory(null, size, 1), IAEAInventory {
    init {
        host = this
        
        filter = UpgradeFilter()
    }
    
    var capacityUpgrades = 0
    var fuzzyUpgrades = 0
    var redstoneUpgrades = 0
    var speedUpgrades = 0
    var inverterUpgrades = 0
    var craftingUpgrades = 0
    
    var cached = false
    
    fun getInstalledUpgrades(upgrade: Upgrades): Int {
        
        if (!cached) {
            gatherUpgrades()
        }
        
        return when(upgrade) {
            Upgrades.CAPACITY -> capacityUpgrades
            Upgrades.FUZZY -> fuzzyUpgrades
            Upgrades.REDSTONE -> redstoneUpgrades
            Upgrades.SPEED -> speedUpgrades
            Upgrades.INVERTER -> inverterUpgrades
            Upgrades.CRAFTING -> craftingUpgrades
            else -> 0
        }
    }
    
    fun gatherUpgrades() {
        cached = true
        
        capacityUpgrades = 0
        fuzzyUpgrades = 0
        redstoneUpgrades = 0
        speedUpgrades = 0
        inverterUpgrades = 0
        craftingUpgrades = 0
        
        for (stack in this) {
            if (stack == null || stack.item == Items.AIR || stack.item !is IUpgradeModule) {
                continue
            }
            
            val upgrade = (stack.item as IUpgradeModule).getType(stack)
            
            when(upgrade) {
                Upgrades.CAPACITY -> capacityUpgrades++
                Upgrades.FUZZY -> fuzzyUpgrades++
                Upgrades.REDSTONE -> redstoneUpgrades++
                Upgrades.SPEED -> speedUpgrades++
                Upgrades.INVERTER -> inverterUpgrades++
                Upgrades.CRAFTING -> craftingUpgrades++
            }
        }
        
        capacityUpgrades = min(capacityUpgrades, getMaxInstalled(Upgrades.CAPACITY))
        fuzzyUpgrades = min(fuzzyUpgrades, getMaxInstalled(Upgrades.FUZZY))
        redstoneUpgrades = min(redstoneUpgrades, getMaxInstalled(Upgrades.REDSTONE))
        speedUpgrades = min(speedUpgrades, getMaxInstalled(Upgrades.SPEED))
        inverterUpgrades = min(inverterUpgrades, getMaxInstalled(Upgrades.INVERTER))
        craftingUpgrades = min(craftingUpgrades, getMaxInstalled(Upgrades.CRAFTING))
    }
    
    abstract fun getMaxInstalled(upgrade: Upgrades): Int

    override fun saveChanges() {
        if (parent != null) {
            parent.saveChanges()
        }
    }

    override fun onInventoryChanged(
        inv: IItemHandler,
        slot: Int,
        operation: Operation,
        removedStack: ItemStack,
        newStack: ItemStack
    ) {
        cached = false
        
        if (!isRemote) {
            parent?.onInventoryChanged(inv, slot, operation, removedStack, newStack)
        }
    }

    override fun readFromNBT(data: CompoundNBT, name: String) {
        super.readFromNBT(data, name)
        gatherUpgrades()
    }

    override val isRemote: Boolean
    get() = parent?.isRemote ?: false

    inner class UpgradeFilter: ItemFilter {
        override fun allowExtract(inv: IItemHandler, slot: Int, amount: Int): Boolean = true

        override fun allowInsert(inv: IItemHandler, slot: Int, itemStack: ItemStack): Boolean {
            if (itemStack.isEmpty) {
                return false
            }
            
            val item = itemStack.item
            if (item is IUpgradeModule) {
                val type = item.getType(itemStack) ?: return false
                
                return getInstalledUpgrades(type) < getMaxInstalled(type)
            }
            
            return false
        }
    }
}