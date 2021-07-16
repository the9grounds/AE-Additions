package com.the9grounds.aeadditions.gridblock

import appeng.api.networking.*
import appeng.api.networking.storage.IStorageGrid
import appeng.api.storage.IMEMonitor
import appeng.api.util.AEColor
import appeng.api.util.AEPartLocation
import appeng.api.util.DimensionalCoord
import com.the9grounds.aeadditions.api.gas.IAEGasStack
import com.the9grounds.aeadditions.integration.Integration
import com.the9grounds.aeadditions.tileentity.TileEntityGasInterface
import com.the9grounds.aeadditions.util.StorageChannels
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import java.util.*

class AEGridBlockGasInterface(val host: TileEntityGasInterface) : IGridBlock {
    protected var grid: IGrid? = null
    protected var usedChannels = 0

    override fun getIdlePowerUsage(): Double {
        return host.powerUsage
    }

    override fun getFlags(): EnumSet<GridFlags> {
        return EnumSet.noneOf(GridFlags::class.java)
    }

    override fun isWorldAccessible(): Boolean {
        return true
    }

    override fun getLocation(): DimensionalCoord {
        return host.location
    }

    override fun getGridColor(): AEColor {
        return AEColor.TRANSPARENT
    }

    override fun onGridNotification(p0: GridNotification) {
        //
    }

    override fun setNetworkStatus(grid: IGrid?, usedChannels: Int) {
        this.grid = grid
        this.usedChannels = usedChannels
    }

    override fun getConnectableSides(): EnumSet<EnumFacing> {
        return EnumSet.allOf(EnumFacing::class.java)
    }

    override fun getMachine(): IGridHost {
        return host
    }

    override fun gridChanged() {
        // Do nothing
    }

    override fun getMachineRepresentation(): ItemStack {
        val location = location

        val blocKState = location.world.getBlockState(location.pos)

        return ItemStack(blocKState.block, 1, blocKState.block.getMetaFromState(blocKState))
    }

    fun getGasMonitor(): IMEMonitor<IAEGasStack>? {
        if (!Integration.Mods.MEKANISMGAS.isEnabled) return null
        val node = host.getGridNode(AEPartLocation.INTERNAL) ?: return null
        val grid = node.grid ?: return null
        val storageGrid = grid.getCache<IStorageGrid>(IStorageGrid::class.java) ?: return null
        return storageGrid.getInventory(StorageChannels.GAS)
    }
}