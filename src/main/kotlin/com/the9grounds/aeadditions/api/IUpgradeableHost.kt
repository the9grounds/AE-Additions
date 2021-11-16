package com.the9grounds.aeadditions.api

import appeng.api.config.Upgrades
import appeng.api.util.IConfigurableObject
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.items.IItemHandler

interface IUpgradeableHost: IConfigurableObject, ISegmentedInventory {
    
    var tile: TileEntity?
    
    fun getInstalledUpgrades(upgrade: Upgrades): Int
    
    fun getUpgradeInventory(): IItemHandler? = getInventoryByName("upgrades")
    
    fun sendUpgradesToClient()
}