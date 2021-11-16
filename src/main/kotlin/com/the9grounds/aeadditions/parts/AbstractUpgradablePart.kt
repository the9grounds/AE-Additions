package com.the9grounds.aeadditions.parts

import appeng.api.config.RedstoneMode
import appeng.api.config.Upgrades
import appeng.core.sync.network.TargetPoint
import com.the9grounds.aeadditions.core.inv.AbstractUpgradeInventory
import com.the9grounds.aeadditions.core.inv.IAEAInventory
import com.the9grounds.aeadditions.core.inv.Operation
import com.the9grounds.aeadditions.core.inv.StackUpgradeInventory
import com.the9grounds.aeadditions.network.NetworkManager
import com.the9grounds.aeadditions.network.packets.UpgradesUpdatedPacket
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraftforge.items.IItemHandler

abstract class AbstractUpgradablePart(itemStack: ItemStack) : AbstractBasicStatePart(itemStack), IAEAInventory {

    open var upgradeSlots: Int = 4

    val upgrades: AbstractUpgradeInventory

    init {
        upgrades = StackUpgradeInventory(itemStack, this, upgradeSlots)
    }

    open val rsMode: RedstoneMode? = null

    val isSleeping: Boolean
        get() {
            if (getInstalledUpgrades(Upgrades.REDSTONE) > 0) {
                when(rsMode) {
                    RedstoneMode.IGNORE -> return false
                    RedstoneMode.HIGH_SIGNAL -> {
                        if (host!!.hasRedstone(side)) {
                            return false
                        }
                    }
                    RedstoneMode.LOW_SIGNAL -> {
                        if (!host!!.hasRedstone(side)) {
                            return false
                        }
                    }
                }

                return true
            }

            return false
        }

    override val isRemote: Boolean
        get() = location.world.isRemote

    override fun getInstalledUpgrades(upgrade: Upgrades): Int {
        return upgrades.getInstalledUpgrades(upgrade)
    }

    override fun readFromNBT(data: CompoundNBT?) {
        super.readFromNBT(data)
        upgrades.readFromNBT(data!!, "upgrades")
    }

    override fun writeToNBT(data: CompoundNBT?) {
        super.writeToNBT(data)
        upgrades.writeToNBT(data!!, "upgrades")
    }

    override fun getDrops(drops: MutableList<ItemStack>?, wrenched: Boolean) {
        require(drops != null)
        for (upgrade in upgrades) {
            if (!upgrade!!.isEmpty) {
                drops.add(upgrade)
            }
        }
    }

    override fun getInventoryByName(name: String): IItemHandler? {
        if (name == "upgrades") {
            return upgrades
        }

        return null
    }

    override fun canConnectRedstone(): Boolean = getInstalledUpgrades(Upgrades.REDSTONE) > 0

    override fun onInventoryChanged(
        inv: IItemHandler,
        slot: Int,
        operation: Operation,
        removedStack: ItemStack,
        newStack: ItemStack
    ) {
        if (inv == upgrades) {
            onUpgradesChanged()
            
            sendUpgradesToClient()
        }
    }
    
    override fun sendUpgradesToClient() {
        NetworkManager.sendToAllAround(UpgradesUpdatedPacket(upgrades.getUpgradeMap()), TargetPoint(location.x.toDouble(),
            location.y.toDouble(), location.z.toDouble(), 32.toDouble(), location.world))
    }

    open fun onUpgradesChanged() {
        
    }
}