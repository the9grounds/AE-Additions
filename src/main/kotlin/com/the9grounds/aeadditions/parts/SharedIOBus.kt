package com.the9grounds.aeadditions.parts

import appeng.api.config.RedstoneMode
import appeng.api.config.Upgrades
import appeng.api.networking.ticking.TickRateModulation
import appeng.me.GridAccessException
import com.the9grounds.aeadditions.api.IAEAChemicalConfig
import com.the9grounds.aeadditions.api.IAEAHasChemicalConfig
import com.the9grounds.aeadditions.core.AEAChemicalConfig
import com.the9grounds.aeadditions.util.StorageChannels
import mekanism.api.chemical.Chemical
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockReader
import kotlin.math.floor

abstract class SharedIOBus(itemStack: ItemStack) : AbstractUpgradablePart(itemStack), IAEAHasChemicalConfig {

    val _chemicalConfig: IAEAChemicalConfig = AEAChemicalConfig(9)

    override fun getChemicalConfig(): IAEAChemicalConfig {
        return _chemicalConfig
    }
    
    var lastRedstone = false

    override fun readFromNBT(data: CompoundNBT?) {
        super.readFromNBT(data)
        _chemicalConfig.readFromNbt(data!!, "config")
    }

    override fun writeToNBT(data: CompoundNBT?) {
        super.writeToNBT(data)
        _chemicalConfig.writeToNbt(data!!, "config")
    }
    
    abstract fun doWork(): TickRateModulation

    override fun onUpgradesChanged() {
        updateState()
    }

    override fun onNeighborChanged(w: IBlockReader?, pos: BlockPos?, neighbor: BlockPos?) {
        updateState()
        
        if (lastRedstone != host!!.hasRedstone(side)) {
            lastRedstone = !lastRedstone
            if (lastRedstone && rsMode == RedstoneMode.SIGNAL_PULSE) {
                doWork()
            }
        }
        
        super.onNeighborChanged(w, pos, neighbor)
    }
    
    protected fun updateState() {
        try {
            if (isSleeping) {
                proxy.tick.sleepDevice(proxy.node)
            } else {
                proxy.tick.wakeDevice(proxy.node)
            }
        } catch (e: GridAccessException) {
            //
        }
    }
    
    protected fun canDoBusWork(): Boolean {
        val self: TileEntity = this.host!!.getTile()
        val selfPos = self.pos.offset(this.side!!.getFacing())
        val world = self.world

        return world != null && world.chunkProvider.canTick(selfPos) && proxy.isActive
    }
    
    protected fun calculateThroughput(): Long {
        var amount = StorageChannels.CHEMICAL!!.transferFactor().toDouble()
        
        amount =  when(getInstalledUpgrades(Upgrades.SPEED)) {
            4 -> amount * 8 * 4 * 2 * 1.5
            3 -> amount * 8 * 4 * 2
            2 -> amount * 8 * 4
            1 -> amount * 8
            else -> floor(amount)
        }
        
        return floor(amount).toLong()
    }
    
    protected fun isInFilter(chemical: Chemical<*>): Boolean {
        for (item in _chemicalConfig) {
            if (chemical == item) {
                return true
            }
        }
        
        return false
    }
    
    protected fun filterEnabled(): Boolean {
        for (chemical in _chemicalConfig) {
            if (chemical != null) {
                return true
            }
        }
        
        return false
    }
}