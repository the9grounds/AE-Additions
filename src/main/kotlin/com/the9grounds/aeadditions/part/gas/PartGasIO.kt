package com.the9grounds.aeadditions.part.gas

import com.the9grounds.aeadditions.part.PartECBase
import appeng.api.networking.ticking.IGridTickable
import com.the9grounds.aeadditions.inventory.IInventoryListener
import com.the9grounds.aeadditions.gui.widget.fluid.IFluidSlotListener
import appeng.api.config.RedstoneMode
import net.minecraft.item.ItemStack
import appeng.api.parts.PartItemStack
import appeng.api.util.AECableType
import appeng.api.parts.IPartCollisionHelper
import net.minecraft.entity.player.EntityPlayer
import com.the9grounds.aeadditions.gui.gas.GuiBusGasIO
import com.the9grounds.aeadditions.container.gas.ContainerBusGasIO
import appeng.api.networking.IGridNode
import appeng.api.networking.ticking.TickingRequest
import com.the9grounds.aeadditions.inventory.InventoryPlain
import net.minecraft.nbt.NBTTagCompound
import com.the9grounds.aeadditions.network.packet.part.PacketPartConfig
import net.minecraft.util.EnumHand
import net.minecraft.util.math.Vec3d
import appeng.api.AEApi
import appeng.api.parts.IPartHost
import appeng.api.util.DimensionalCoord
import net.minecraftforge.fluids.FluidRegistry
import java.io.IOException
import io.netty.buffer.ByteBuf
import com.the9grounds.aeadditions.network.packet.other.PacketFluidSlotUpdate
import appeng.api.util.AEPartLocation
import appeng.api.networking.ticking.TickRateModulation
import com.the9grounds.aeadditions.container.IUpgradeable
import com.the9grounds.aeadditions.inventory.UpgradeInventory
import com.the9grounds.aeadditions.util.NetworkUtil
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraftforge.fluids.Fluid
import java.util.*

abstract class PartGasIO : PartECBase(), IGridTickable, IInventoryListener, IFluidSlotListener, IUpgradeable {
    @JvmField val filterFluids = arrayOfNulls<Fluid>(9)
    val filterOrder = listOf(4,1,3,5,7,0,2,6,8)
    private val upgradeInventory: UpgradeInventory = object : UpgradeInventory(this) {
        override fun onContentsChanged() {
            saveData()
        }
    }
    var redstoneMode = RedstoneMode.IGNORE
        private set
    var lastRedstone = false
    protected var filterSize: Byte = 0
    var speedState: Int = 0
        protected set
    protected var redstoneControlled = false

    //private boolean lastRedstone;
    override fun getDrops(drops: MutableList<ItemStack>, wrenched: Boolean) {
        for (stack in upgradeInventory.slots) {
            if (stack == null) {
                continue
            }
            drops.add(stack)
        }
    }

    override fun getItemStack(type: PartItemStack): ItemStack {
        val stack = super.getItemStack(type)
        if (type == PartItemStack.WRENCH) {
            stack.tagCompound!!.removeTag("upgradeInventory")
        }
        return stack
    }

    open override fun getCableConnectionLength(aeCableType: AECableType?): Float {
        return 5.0f
    }

    protected fun canDoWork(): Boolean {
        val redstonePowered = isRedstonePowered
        return if (!redstoneControlled) {
            true
        } else when (redstoneMode) {
            RedstoneMode.IGNORE -> true
            RedstoneMode.LOW_SIGNAL -> !redstonePowered
            RedstoneMode.HIGH_SIGNAL -> redstonePowered
            RedstoneMode.SIGNAL_PULSE -> false
        }
        return false
    }

    abstract fun doWork(rate: Int, TicksSinceLastCall: Int): Boolean
    abstract override fun getBoxes(bch: IPartCollisionHelper)
    override fun getClientGuiElement(player: EntityPlayer): Any {
        return GuiBusGasIO(this, player)
    }

    override fun getLightLevel(): Int {
        return 0
    }

    override fun getServerGuiElement(player: EntityPlayer): Any {
        return ContainerBusGasIO(this, player)
    }

    override fun getTickingRequest(node: IGridNode): TickingRequest {
        return TickingRequest(1, 20, false, false)
    }

    override fun getUpgradeInventory(): InventoryPlain {
        return upgradeInventory
    }

    override fun getWailaBodey(tag: NBTTagCompound, oldList: MutableList<String>): List<String> {
        if (tag.hasKey("speed")) {
            oldList.add(tag.getInteger("speed").toString() + "mB/t")
        } else {
            oldList.add("125mB/t")
        }
        return oldList
    }

    override fun getWailaTag(tag: NBTTagCompound): NBTTagCompound {
        tag.setInteger("speed", maxAmountToTransfer)
        return tag
    }

    fun loopRedstoneMode(player: EntityPlayer?) {
        if (redstoneMode.ordinal + 1 < RedstoneMode.values().size) {
            redstoneMode = RedstoneMode.values()[redstoneMode.ordinal + 1]
        } else {
            redstoneMode = RedstoneMode.values()[0]
        }
        NetworkUtil.sendToPlayer(
            PacketPartConfig(
                this,
                PacketPartConfig.FLUID_IO_REDSTONE_MODE,
                redstoneMode.toString()
            ), player
        )
        saveData()
    }

    open override fun onActivate(player: EntityPlayer?, enumHand: EnumHand?, pos: Vec3d?): Boolean {
        val activate = super.onActivate(player, enumHand, pos)
        onInventoryChanged()
        return activate
    }

    val activeFilters: List<Fluid?>
        get() {
            val filters: MutableList<Fluid?> = ArrayList()
            for (order in filterOrder) {
                val filter = filterFluids[order]
                if (filter != null) {
                    filters.add(filter)
                }
            }
            return filters
        }

    override fun onInventoryChanged() {
        filterSize = 0
        redstoneControlled = false
        speedState = 0
        for (i in 0 until upgradeInventory.sizeInventory) {
            val currentStack = upgradeInventory.getStackInSlot(i)
            if (currentStack != null) {
                if (AEApi.instance().definitions().materials().cardCapacity().isSameAs(currentStack)) {
                    filterSize++
                }
                if (AEApi.instance().definitions().materials().cardRedstone().isSameAs(currentStack)) {
                    redstoneControlled = true
                }
                if (AEApi.instance().definitions().materials().cardSpeed().isSameAs(currentStack)) {
                    speedState++
                }
            }
        }
        val host = host
        val coord = location
        if (host == null || coord == null || coord.world == null || coord.world.isRemote) {
            return
        }
        NetworkUtil.sendNetworkPacket(
            PacketPartConfig(
                this,
                PacketPartConfig.FLUID_IO_FILTER,
                java.lang.Byte.toString(filterSize)
            ), coord.pos, coord.world
        )
        NetworkUtil.sendNetworkPacket(
            PacketPartConfig(
                this,
                PacketPartConfig.FLUID_IO_REDSTONE,
                java.lang.Boolean.toString(redstoneControlled)
            ), coord.pos, coord.world
        )
        saveData()
    }
    
    override fun onNeighborChanged(var1: IBlockAccess?, var2: BlockPos?, var3: BlockPos?) {
        super.onNeighborChanged(var1, var2, var3)
        if (lastRedstone != host.hasRedstone(side)) {
            lastRedstone = isRedstonePowered

            if (lastRedstone && redstoneMode === RedstoneMode.SIGNAL_PULSE) {
                doWork(maxAmountToTransfer, 1)
            }
        }
    }

    override fun readFromNBT(data: NBTTagCompound) {
        super.readFromNBT(data)
        redstoneMode = RedstoneMode.values()[data
            .getInteger("redstoneMode")]
        for (i in 0..8) {
            filterFluids[i] = FluidRegistry.getFluid(
                data
                    .getString("FilterFluid#$i")
            )
        }
        upgradeInventory.readFromNBT(
            data.getTagList(
                "upgradeInventory",
                10
            )
        )
        onInventoryChanged()
    }

    @Throws(IOException::class)
    override fun readFromStream(data: ByteBuf): Boolean {
        return super.readFromStream(data)
    }

    fun sendInformation(player: EntityPlayer?) {
        NetworkUtil.sendToPlayer(PacketFluidSlotUpdate(Arrays.asList(*filterFluids)), player)
        NetworkUtil.sendToPlayer(
            PacketPartConfig(
                this,
                PacketPartConfig.FLUID_IO_FILTER,
                java.lang.Byte.toString(filterSize)
            ), player
        )
        NetworkUtil.sendToPlayer(
            PacketPartConfig(
                this,
                PacketPartConfig.FLUID_IO_REDSTONE,
                java.lang.Boolean.toString(redstoneControlled)
            ), player
        )
        NetworkUtil.sendToPlayer(
            PacketPartConfig(
                this,
                PacketPartConfig.FLUID_IO_REDSTONE_MODE,
                redstoneMode.toString()
            ), player
        )
    }

    override fun setFluid(index: Int, fluid: Fluid, player: EntityPlayer) {
        filterFluids[index] = fluid
        NetworkUtil.sendToPlayer(PacketFluidSlotUpdate(Arrays.asList(*filterFluids)), player)
        saveData()
    }

    override fun setPartHostInfo(location: AEPartLocation, iPartHost: IPartHost, tileEntity: TileEntity) {
        super.setPartHostInfo(location, iPartHost, tileEntity)
        onInventoryChanged()
    }

    override fun tickingRequest(
        node: IGridNode,
        TicksSinceLastCall: Int
    ): TickRateModulation {
        return if (canDoWork()) {
            if (doWork(
                    maxAmountToTransfer,
                    TicksSinceLastCall
                )
            ) TickRateModulation.FASTER else TickRateModulation.SLOWER
        } else TickRateModulation.SLOWER
    }

    override fun writeToNBT(data: NBTTagCompound) {
        super.writeToNBT(data)
        data.setInteger("redstoneMode", redstoneMode.ordinal)
        for (i in filterFluids.indices) {
            val fluid = filterFluids[i]
            if (fluid != null) {
                data.setString("FilterFluid#$i", fluid.name)
            } else {
                data.setString("FilterFluid#$i", "")
            }
        }
        data.setTag("upgradeInventory", upgradeInventory.writeToNBT())
    }

    @Throws(IOException::class)
    override fun writeToStream(data: ByteBuf) {
        super.writeToStream(data)
    }
    
    protected val maxAmountToTransfer: Int
    get() {
        // Base
        var amount = 125.00
        if (speedState == 4) {
            amount *= 1.50
        }
        if (speedState >= 3) {
            amount *= 2.0
        }
        if (speedState >= 2) {
            amount *= 4.0
        }
        if (speedState >= 1) {
            amount *= 8.0
        }
        return amount.toInt()
    }
}