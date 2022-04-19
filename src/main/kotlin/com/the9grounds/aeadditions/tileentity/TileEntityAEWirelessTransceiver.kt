package com.the9grounds.aeadditions.tileentity

import appeng.api.networking.*
import appeng.api.util.AECableType
import appeng.api.util.AEColor
import appeng.api.util.AEPartLocation
import appeng.api.util.DimensionalCoord
import com.the9grounds.aeadditions.block.BlockAEWirelessTransceiver
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import java.util.*

class TileEntityAEWirelessTransceiver: TileBase(), IGridHost, IGridBlock {
    
    var isActive = false
    
    override fun getIdlePowerUsage(): Double {
        if (!isActive) {
            return 0.0
        }
        
        // If mode is transmit, then 0.0
        // if mode is receive, get distance between this block and the transmitter, then divide by 10, multiply by multiplier
        
        return 0.0
    }

    override fun getFlags(): EnumSet<GridFlags> {
        return EnumSet.of(GridFlags.DENSE_CAPACITY)
    }

    override fun isWorldAccessible(): Boolean = true

    override fun getLocation(): DimensionalCoord = DimensionalCoord(this)

    override fun getGridColor(): AEColor = AEColor.TRANSPARENT

    override fun onGridNotification(p0: GridNotification) {
        TODO("Not yet implemented")
    }

    override fun setNetworkStatus(p0: IGrid?, p1: Int) {
        TODO("Not yet implemented")
    }

    override fun getConnectableSides(): EnumSet<EnumFacing> {
        return EnumSet.allOf(EnumFacing::class.java)
    }

    override fun getMachine(): IGridHost {
        return this
    }

    override fun gridChanged() {
        TODO("Not yet implemented")
    }

    override fun getMachineRepresentation(): ItemStack {
        return ItemStack(BlockAEWirelessTransceiver())
    }

    enum class AEWirelessTransceiverMode {
        TRANSMIT,
        RECEIVE
    }

    override fun getGridNode(p0: AEPartLocation): IGridNode? {
        TODO("Not yet implemented")
    }

    override fun getCableConnectionType(p0: AEPartLocation): AECableType {
        TODO("Not yet implemented")
    }

    override fun securityBreak() {
        TODO("Not yet implemented")
    }
}