package com.the9grounds.aeadditions.tile

import appeng.api.networking.IGridHost
import appeng.api.networking.IGridNode
import appeng.api.networking.energy.IEnergyGrid
import appeng.api.networking.energy.IEnergySource
import appeng.api.networking.security.IActionHost
import appeng.api.util.AECableType
import appeng.api.util.AEPartLocation
import appeng.me.helpers.ChannelPowerSrc
import com.the9grounds.aeadditions.integration.appeng.AppEng
import com.the9grounds.aeadditions.me.AEAGridBlock
import net.minecraft.block.BlockState
import net.minecraft.nbt.CompoundNBT
import net.minecraft.tileentity.TileEntityType
import net.minecraftforge.fml.common.thread.SidedThreadGroups

abstract class AbstractNetworkedTile(tileEntityTypeIn: TileEntityType<*>) : AbstractAEATileEntity(tileEntityTypeIn), IGridHost,
    IActionHost {

    val gridBlock = AEAGridBlock(this)

    protected var node: IGridNode? = null
    private var isFirstGetGridNode = true
    
    val powerUsage: Double
        get() = 1.0

    protected val powerSource: IEnergySource?
        get() {
            if (node == null || node!!.grid == null) {
                return null
            }

            return ChannelPowerSrc(node, node!!.grid.getCache(IEnergyGrid::class.java))
        }

    override fun read(state: BlockState, nbt: CompoundNBT) {
        super.read(state, nbt)
        if (hasWorld()) {
            if (node !== null) {
                node!!.loadFromNBT("node0", nbt)
                node!!.updateState()
            }
        }
    }

    override fun write(compound: CompoundNBT): CompoundNBT {
        if (node != null) {
            node!!.saveToNBT("node0", compound)
        }
        
        return super.write(compound)
    }

    override fun getGridNode(dir: AEPartLocation): IGridNode? {
        if (Thread.currentThread().threadGroup == SidedThreadGroups.CLIENT) {
            return null
        }

        if (isFirstGetGridNode) {
            isFirstGetGridNode = false
            actionableNode?.updateState()
        }

        return node
    }

    override fun remove() {
        super.remove()
        if (node != null) {
            node!!.destroy()
            node = null
        }
    }

    override fun getCableConnectionType(dir: AEPartLocation): AECableType = AECableType.SMART

    override fun securityBreak() {
        //
    }

    override fun getActionableNode(): IGridNode? {
        if (Thread.currentThread().threadGroup === SidedThreadGroups.CLIENT) {
            return node
        }

        if (node == null) {
            node = AppEng.API!!.grid().createGridNode(gridBlock)
        }

        return node
    }
    
}