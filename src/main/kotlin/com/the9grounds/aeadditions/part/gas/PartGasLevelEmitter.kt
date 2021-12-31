package com.the9grounds.aeadditions.part.gas

import appeng.api.config.RedstoneMode
import appeng.api.config.SecurityPermissions
import appeng.api.networking.security.IActionSource
import appeng.api.networking.storage.IStackWatcher
import appeng.api.networking.storage.IStackWatcherHost
import appeng.api.parts.IPart
import appeng.api.parts.IPartCollisionHelper
import appeng.api.parts.IPartModel
import appeng.api.storage.IStorageChannel
import appeng.api.storage.data.IAEStack
import appeng.api.storage.data.IItemList
import appeng.api.util.AECableType
import appeng.api.util.AEPartLocation
import com.google.common.collect.ImmutableList
import com.the9grounds.aeadditions.api.gas.IAEGasStack
import com.the9grounds.aeadditions.container.gas.ContainerGasEmitter
import com.the9grounds.aeadditions.gui.gas.GuiGasEmitter
import com.the9grounds.aeadditions.gui.widget.fluid.IFluidSlotListener
import com.the9grounds.aeadditions.integration.mekanism.gas.AEGasStack
import com.the9grounds.aeadditions.models.PartModels
import com.the9grounds.aeadditions.network.packet.other.PacketFluidSlotUpdate
import com.the9grounds.aeadditions.network.packet.part.PacketPartConfig
import com.the9grounds.aeadditions.part.PartECBase
import com.the9grounds.aeadditions.util.GasUtil
import com.the9grounds.aeadditions.util.NetworkUtil
import com.the9grounds.aeadditions.util.PermissionUtil
import com.the9grounds.aeadditions.util.StorageChannels
import io.netty.buffer.ByteBuf
import mekanism.api.gas.GasStack
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fml.common.FMLCommonHandler
import java.io.IOException
import java.util.*

class PartGasLevelEmitter : PartECBase(), IStackWatcherHost, IFluidSlotListener {
    protected var selectedFluid: Fluid? = null
    private var mode = RedstoneMode.HIGH_SIGNAL
    private var watcher: IStackWatcher? = null
    private var wantedAmount: Long = 0
    protected var currentAmount: Long = 0
    private var previousState: Boolean = false
    private var clientRedstoneOutput = false

    override fun getCableConnectionLength(aeCableType: AECableType?): Float {
        return 16.0f
    }

    override fun getCableConnectionType(dir: AEPartLocation): AECableType {
        return AECableType.SMART
    }

    fun changeWantedAmount(modifier: Int, player: EntityPlayer?) {
        setWantedAmount(this.wantedAmount + modifier, player)
    }

    override fun getBoxes(bch: IPartCollisionHelper) {
        bch.addBox(7.0, 7.0, 11.0, 9.0, 9.0, 16.0)
    }

    override fun getClientGuiElement(player: EntityPlayer?): Any? {
        return GuiGasEmitter(this, player)
    }

    override fun getPowerUsage(): Double {
        return 1.0
    }

    override fun getServerGuiElement(player: EntityPlayer?): Any? {
        return ContainerGasEmitter(this, player)
    }

    private fun isLevelEmitterOn(): Boolean {
        if (FMLCommonHandler.instance().effectiveSide.isClient) {
            return clientRedstoneOutput
        }
        val gridNode = gridNode
        return if (gridNode == null || !gridNode.isActive) {
            false
        } else when (this.mode) {
            RedstoneMode.LOW_SIGNAL -> this.wantedAmount >= this.currentAmount
            RedstoneMode.HIGH_SIGNAL -> this.wantedAmount <= this.currentAmount
            else -> false
        }
    }

    override fun isProvidingStrongPower(): Int {
        return if (isLevelEmitterOn()) 15 else 0
    }

    override fun isProvidingWeakPower(): Int {
        return isProvidingStrongPower
    }

    override fun canConnectRedstone(): Boolean = true

    fun notifyTargetBlock(tileEntity: TileEntity, facing: EnumFacing?) {
        // note - params are always the same
        tileEntity.world.notifyNeighborsOfStateChange(tileEntity.pos, Blocks.AIR, true)
        tileEntity.world.notifyNeighborsOfStateChange(tileEntity.pos.offset(facing), Blocks.AIR, true)
    }

    override fun onActivate(player: EntityPlayer?, hand: EnumHand?, pos: Vec3d?): Boolean {
        return if (PermissionUtil.hasPermission(player, SecurityPermissions.BUILD, this as IPart)) {
            super.onActivate(player, hand, pos)
        } else false
    }

    override fun onStackChange(
        o: IItemList<*>?,
        fullStack: IAEStack<*>?,
        diffStack: IAEStack<*>?,
        src: IActionSource?,
        chan: IStorageChannel<*>
    ) {
        fullStack as IAEGasStack?

        if (chan == StorageChannels.GAS!! && fullStack != null && (fullStack.gas == GasUtil.getGas(this.selectedFluid))) {
            this.currentAmount = fullStack.stackSize ?: 0

            val isOn = isLevelEmitterOn()

            if (previousState != isOn) {

                val node = gridNode

                if (node != null) {

                    isActive = node.isActive
                    host?.markForUpdate()
                    notifyTargetBlock(hostTile, facing)
                    previousState = isOn
                }
            }
        }
    }

    override fun randomDisplayTick(world: World, blockPos: BlockPos, random: Random) {
        if (isLevelEmitterOn()) {
            val facing = facing
            // TODO: Make sure change works
            val d0 = facing.xOffset * 0.45f + (random.nextFloat() - 0.5f) * 0.2
            val d1 = facing.yOffset * 0.45f + (random.nextFloat() - 0.5f) * 0.2
            val d2 = facing.zOffset * 0.45f + (random.nextFloat() - 0.5f) * 0.2
            world.spawnParticle(
                EnumParticleTypes.REDSTONE,
                0.5 + blockPos.x + d0,
                0.5 + blockPos.y + d1,
                0.5 + blockPos.z + d2,
                0.0,
                0.0,
                0.0
            )
        }
    }

    override fun readFromNBT(data: NBTTagCompound) {
        super.readFromNBT(data)
        this.selectedFluid = FluidRegistry.getFluid(data.getString("fluid"))
        this.mode = RedstoneMode.values()[data.getInteger("mode")]
        this.wantedAmount = data.getLong("wantedAmount")
        this.previousState = data.getBoolean("previousState")
        if (this.wantedAmount < 0) {
            this.wantedAmount = 0
        }
    }

    @Throws(IOException::class)
    override fun readFromStream(data: ByteBuf): Boolean {
        super.readFromStream(data)
        this.clientRedstoneOutput = data.readBoolean()
        this.previousState = data.readBoolean()
        if (host != null) {
            host.markForUpdate()
        }
        return true
    }

    override fun writeToNBT(data: NBTTagCompound) {
        super.writeToNBT(data)
        if (this.selectedFluid != null) {
            data.setString("fluid", this.selectedFluid!!.getName())
        } else {
            data.removeTag("fluid")
        }
        data.setInteger("mode", this.mode.ordinal)
        data.setLong("wantedAmount", this.wantedAmount)
        data.setBoolean("previousState", previousState)
    }

    @Throws(IOException::class)
    override fun writeToStream(data: ByteBuf) {
        super.writeToStream(data)
        data.writeBoolean(isLevelEmitterOn())
        data.writeBoolean(previousState)
    }

    override fun setFluid(index: Int, fluid: Fluid?, player: EntityPlayer?) {
        this.selectedFluid = fluid
        if (this.watcher == null) {
            return
        }
        this.watcher!!.reset()
        updateWatcher(this.watcher)
        if (this.selectedFluid != null) NetworkUtil.sendToPlayer(
            PacketFluidSlotUpdate(ImmutableList.of(this.selectedFluid)),
            player
        )
        saveData()
    }

    fun setWantedAmount(wantedAmount: Long, player: EntityPlayer?) {
        this.wantedAmount = wantedAmount
        if (this.wantedAmount < 0) {
            this.wantedAmount = 0
        }
        notifyTargetBlock(hostTile, facing)
        if (host != null) {
            host.markForUpdate()
        }
        
        this.currentAmount = this.gridBlock.gasMonitor.storageList.findPrecise(AEGasStack(GasStack(GasUtil.getGas(this.selectedFluid), 1000))).stackSize

        val isOn = isLevelEmitterOn()

        if (previousState != isOn) {

            val node = gridNode

            if (node != null) {

                isActive = node.isActive
                host?.markForUpdate()
                notifyTargetBlock(hostTile, facing)
                previousState = isOn
            }
        }
        
        saveData()
    }

    fun syncClientGui(player: EntityPlayer?) {
        NetworkUtil.sendToPlayer(PacketPartConfig(this, PacketPartConfig.FLUID_EMITTER_MODE, mode.toString()), player)
        NetworkUtil.sendToPlayer(
            PacketPartConfig(
                this,
                PacketPartConfig.FLUID_EMITTER_AMOUNT,
                java.lang.Long.toString(wantedAmount)
            ), player
        )
        if (this.selectedFluid != null) NetworkUtil.sendToPlayer(
            PacketFluidSlotUpdate(ImmutableList.of(this.selectedFluid)),
            player
        )
    }

    fun getWantedAmount(): Long {
        return wantedAmount
    }

    fun toggleMode(player: EntityPlayer?) {
        when (this.mode) {
            RedstoneMode.LOW_SIGNAL -> this.mode = RedstoneMode.HIGH_SIGNAL
            else -> this.mode = RedstoneMode.LOW_SIGNAL
        }
        notifyTargetBlock(hostTile, facing)
        NetworkUtil.sendToPlayer(PacketPartConfig(this, PacketPartConfig.FLUID_EMITTER_MODE, mode.toString()), player)
        if (host != null) {
            host.markForUpdate()
        }
        saveData()
    }

    override fun updateWatcher(newWatcher: IStackWatcher?) {
        this.watcher = newWatcher
        if (this.selectedFluid != null) {
            this.watcher!!.add(
                StorageChannels.GAS!!.createStack(
                    FluidStack(
                        this.selectedFluid,
                        1000
                    )
                )
            )
        }
    }

    override fun getStaticModels(): IPartModel {
        return if (isActive && isPowered) {
            if (isLevelEmitterOn()) PartModels.EMITTER_ON_HAS_CHANNEL else PartModels.EMITTER_OFF_HAS_CHANNEL
        } else if (isPowered) {
            if (isLevelEmitterOn()) PartModels.EMITTER_ON_ON else PartModels.EMITTER_OFF_ON
        } else {
            if (isLevelEmitterOn()) PartModels.EMITTER_ON_OFF else PartModels.EMITTER_OFF_OFF
        }
    }
}