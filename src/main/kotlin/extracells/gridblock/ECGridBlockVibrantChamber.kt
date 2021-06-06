package extracells.gridblock

import appeng.api.networking.*
import appeng.api.util.AEColor
import appeng.api.util.DimensionalCoord
import extracells.tileentity.TileEntityVibrationChamberFluid
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import java.util.*

class ECGridBlockVibrantChamber(val host: TileEntityVibrationChamberFluid) : IGridBlock {

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
        // Do Nothing
    }

    override fun setNetworkStatus(grid: IGrid?, usedChannels: Int) {
        this.grid = grid
        this.usedChannels = usedChannels
    }

    override fun getConnectableSides(): EnumSet<EnumFacing> {
        return EnumSet.of(EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH,
            EnumFacing.WEST)
    }

    override fun getMachine(): IGridHost {
        return host
    }

    override fun gridChanged() {
        // Do Nothing
    }

    override fun getMachineRepresentation(): ItemStack {
        val location = location

        val blockState = location.world.getBlockState(location.pos)

        return ItemStack(blockState.block, 1, blockState.block.getMetaFromState(blockState))
    }
}