package com.the9grounds.aeadditions.me

import appeng.api.networking.GridFlags
import appeng.api.networking.GridNotification
import appeng.api.networking.IGridBlock
import appeng.api.networking.IGridHost
import appeng.api.util.AEColor
import appeng.api.util.DimensionalCoord
import com.the9grounds.aeadditions.tile.AbstractNetworkedTile
import net.minecraft.item.ItemStack
import net.minecraft.util.Direction
import java.util.*

class AEAGridBlock(val host: AbstractNetworkedTile) : IGridBlock {
    override fun getIdlePowerUsage(): Double = host.powerUsage

    override fun getFlags(): EnumSet<GridFlags> = EnumSet.noneOf(GridFlags::class.java)

    override fun isWorldAccessible(): Boolean = true

    override fun getLocation(): DimensionalCoord = DimensionalCoord(host)

    override fun getGridColor(): AEColor = AEColor.TRANSPARENT

    override fun onGridNotification(notification: GridNotification) {
        
    }

    override fun getConnectableSides(): EnumSet<Direction> {
        return EnumSet.allOf(Direction::class.java)
    }

    override fun getMachine(): IGridHost = host

    override fun gridChanged() {
        //
    }

    override fun getMachineRepresentation(): ItemStack {
        val location = location
        
        val blockState = location.world.getBlockState(location.pos)
        
        return ItemStack(blockState.block, 1)
    }
}