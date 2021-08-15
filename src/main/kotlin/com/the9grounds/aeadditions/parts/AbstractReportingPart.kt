package com.the9grounds.aeadditions.parts

import appeng.api.implementations.IPowerChannelState
import appeng.api.implementations.parts.IMonitorPart
import appeng.api.networking.GridFlags
import appeng.api.networking.events.MENetworkBootingStatusChange
import appeng.api.networking.events.MENetworkEventSubscribe
import appeng.api.networking.events.MENetworkPowerStatusChange
import appeng.api.parts.IPartCollisionHelper
import appeng.api.parts.IPartModel
import appeng.api.util.AEPartLocation
import appeng.me.GridAccessException
import appeng.parts.reporting.ReportingModelData
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.PacketBuffer
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.world.IBlockReader
import net.minecraftforge.client.model.data.IModelData
import kotlin.experimental.and

abstract class AbstractReportingPart(itemStack: ItemStack, requireChannel: Boolean) : AEABasePart(itemStack),
    IMonitorPart, IPowerChannelState {

    companion object {
        @JvmStatic protected val POWERED_FLAG = 4
        @JvmStatic protected val CHANNEL_FLAG = 16
        private val BOOTING_FLAG = 8
    }

    // Rotation of the part
    var rotation: Byte = 0
    var clientFlags = 0
    var opacity = 0

    init {
        if (requireChannel) {
            this.proxy.setFlags(GridFlags.REQUIRE_CHANNEL)
            this.proxy.idlePowerUsage = 1.0 / 2.0
        } else {
            this.proxy.idlePowerUsage = 1.0 / 16.0
        }
    }

    constructor(itemStack: ItemStack) : this(itemStack, false) {}

    @MENetworkEventSubscribe
    fun bootingRender(event: MENetworkBootingStatusChange) {
        if (this.isLightSource()) {
            this.host!!.markForUpdate()
        }
    }

    @MENetworkEventSubscribe
    fun powerRender(event: MENetworkPowerStatusChange) {
        this.host!!.markForUpdate()
    }

    override fun getBoxes(bch: IPartCollisionHelper?) {
        bch!!.addBox(2.0, 2.0, 14.0, 14.0, 14.0, 16.0)
        bch.addBox(4.0, 4.0, 13.0, 12.0, 12.0, 14.0)
    }

    override fun onNeighborChanged(w: IBlockReader?, pos: BlockPos?, neighbor: BlockPos?) {
        if (pos!!.offset(side!!.facing).equals(neighbor)) {
            this.opacity = -1
            this.host!!.markForUpdate()
        }
    }

    override fun readFromNBT(data: CompoundNBT?) {
        super.readFromNBT(data)

        this.rotation = data!!.getByte("rotation")
    }

    override fun writeToNBT(data: CompoundNBT?) {
        super.writeToNBT(data)

        data!!.putByte("rotation", this.rotation)
    }

    override fun writeToStream(data: PacketBuffer?) {
        super.writeToStream(data)
        clientFlags = (rotation.and(3)).toInt()

        try {
            if (proxy.energy.isNetworkPowered) {
                clientFlags = clientFlags or POWERED_FLAG
            }
            if (proxy.path.isNetworkBooting) {
                clientFlags = clientFlags or BOOTING_FLAG
            }
            if (proxy.node.meetsChannelRequirements()) {
                clientFlags = clientFlags or CHANNEL_FLAG
            }
        } catch (e: GridAccessException) {
            // um.. nothing.
        }

        data!!.writeByte(clientFlags)
        data.writeInt(opacity)
    }

    override fun readFromStream(data: PacketBuffer?): Boolean {
        super.readFromStream(data)

        val oldFlags = clientFlags
        val oldOpacity = opacity

        clientFlags = data!!.readByte().toInt()
        opacity = data.readInt()

        rotation = clientFlags.toByte() and 3
        if (clientFlags == oldFlags && opacity == oldOpacity) {
            return false
        }

        return true
    }

    override fun getLightLevel(): Int {
        return blockLight(
            if (isPowered)
                if (isLightSource()) 15
                else 9
            else 0
        )
    }

    override fun onPartActivate(player: PlayerEntity?, hand: Hand?, pos: Vector3d?): Boolean {
        // Handle various wrenches
        return super.onPartActivate(player, hand, pos)
    }

    override fun onPlacement(player: PlayerEntity?, hand: Hand?, held: ItemStack?, side: AEPartLocation?) {
        super.onPlacement(player, hand, held, side)

        val rotation = MathHelper.floor(player!!.rotationYaw * 4f / 360f + 2.5).toByte().and(3)

        if (side == AEPartLocation.UP || side == AEPartLocation.DOWN) {
            this.rotation = rotation
        }
    }

    override fun isActive(): Boolean {
        return if (isLightSource()) {
            this.clientFlags.and(CHANNEL_FLAG or POWERED_FLAG) == CHANNEL_FLAG or POWERED_FLAG
        } else {
            isPowered
        }
    }

    override fun isPowered(): Boolean {
        return try {
            if (!isRemote) {
                this.proxy.energy.isNetworkPowered
            } else {
                this.clientFlags.and(POWERED_FLAG) == POWERED_FLAG
            }
        } catch (e: Throwable) {
            false
        }
    }

    protected fun selectModel(offModels: IPartModel, onModels: IPartModel, hasChannelModels: IPartModel): IPartModel {
        return when {
            isActive -> {
                hasChannelModels
            }
            isPowered -> {
                onModels
            }
            else -> {
                offModels
            }
        }
    }

    // Re-use from AE2
    override fun getModelData(): IModelData {
        return ReportingModelData(rotation)
    }

    abstract fun isLightSource(): Boolean

    private fun blockLight(emit: Int): Int {
        if (opacity < 0) {
            val tileEntity = tile
            val world = tileEntity!!.world
            val pos = tileEntity.pos.offset(side!!.getFacing())
            opacity = 255 - world!!.getBlockState(pos).getOpacity(world, pos)
        }
        return (emit * (opacity / 255.0f)).toInt()
    }
}