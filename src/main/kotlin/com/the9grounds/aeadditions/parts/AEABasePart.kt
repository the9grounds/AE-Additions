package com.the9grounds.aeadditions.parts

import appeng.api.config.Upgrades
import appeng.api.implementations.IUpgradeableHost
import appeng.api.networking.IGridNode
import appeng.api.networking.energy.IEnergyGrid
import appeng.api.networking.energy.IEnergySource
import appeng.api.networking.security.IActionHost
import appeng.api.parts.*
import appeng.api.util.AECableType
import appeng.api.util.AEPartLocation
import appeng.api.util.DimensionalCoord
import appeng.api.util.IConfigManager
import appeng.me.helpers.AENetworkProxy
import appeng.me.helpers.ChannelPowerSrc
import appeng.me.helpers.IGridProxyable
import com.the9grounds.aeadditions.helpers.ICustomNameObject
import com.the9grounds.aeadditions.util.Utils
import net.minecraft.crash.CrashReportCategory
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.PacketBuffer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.Direction
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.util.text.ITextComponent
import net.minecraft.world.IBlockReader
import net.minecraft.world.World
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.items.IItemHandler
import java.util.*

abstract class AEABasePart(val itemStack: ItemStack) : IPart, IGridProxyable, IActionHost, IUpgradeableHost,
    ICustomNameObject {

    private val proxy: AENetworkProxy = AENetworkProxy(this, "part", itemStack, false)

    private var tile: TileEntity? = null
    var host: IPartHost? = null
    var side: AEPartLocation? = null

    init {
        proxy.setValidSides(EnumSet.noneOf(Direction::class.java))
    }
    
    protected val powerSource: IEnergySource
    get() = ChannelPowerSrc(proxy.node, proxy.node.grid.getCache(IEnergyGrid::class.java))

    val isRemote: Boolean
        get() = this.tile == null || this.tile!!.world == null || this.tile!!.world!!.isRemote

    override fun getTile(): TileEntity? = tile

    override fun getGridNode(): IGridNode? = proxy.node

    override fun getCableConnectionType(dir: AEPartLocation): AECableType = AECableType.GLASS

    override fun getProxy(): AENetworkProxy = proxy

    override fun securityBreak() {
        if (itemStack.count > 0 && gridNode != null) {
            host!!.removePart(side, false)
            Utils.spawnDrops(tile!!.world!!, tile!!.pos, listOf(itemStack.copy()))
            itemStack.count = 0
        }
    }

    override fun getBoxes(bch: IPartCollisionHelper?) {
        // Do nothing
    }

    override fun getInstalledUpgrades(u: Upgrades?): Int = 0

    override fun getLocation(): DimensionalCoord = DimensionalCoord(tile)

    override fun gridChanged() {
        // Do nothing
    }

    override fun getActionableNode(): IGridNode? = proxy.node

    override fun saveChanges() = host!!.markForSave()

    override val customInventoryName: ITextComponent
    get() = itemStack.displayName

    override val hasCustomInventoryName: Boolean
        get() = itemStack.hasDisplayName()

    override fun addEntityCrashInfo(section: CrashReportCategory?) {
        section!!.addDetail("Part Side", this.side)
    }

    override fun getItemStack(type: PartItemStack?): ItemStack {
        if (type === PartItemStack.NETWORK) {
            val copy = itemStack.copy()

            copy.tag = null

            return copy
        }

        return itemStack
    }

    override fun isSolid(): Boolean = false

    override fun onNeighborChanged(w: IBlockReader?, pos: BlockPos?, neighbor: BlockPos?) {
        //
    }

    override fun canConnectRedstone(): Boolean = false

    override fun readFromNBT(data: CompoundNBT?) {
        proxy.readFromNBT(data)
    }

    override fun writeToNBT(data: CompoundNBT?) {
        proxy.writeToNBT(data)
    }

    override fun isProvidingStrongPower(): Int = 0

    override fun isProvidingWeakPower(): Int = 0

    override fun writeToStream(data: PacketBuffer?) {
        //
    }

    override fun getGridNode(dir: AEPartLocation): IGridNode? = proxy.node

    override fun getCableConnectionLength(cable: AECableType?): Float = 3f

    override fun requireDynamicRender(): Boolean = false

    override fun getLightLevel(): Int = 0

    override fun isLadder(entity: LivingEntity?): Boolean = false

    override fun readFromStream(data: PacketBuffer?): Boolean = false

    override fun onEntityCollision(entity: Entity?) {
        //
    }

    override fun removeFromWorld() {
        this.proxy.remove()
    }

    override fun addToWorld() {
        this.proxy.onReady()
    }

    override fun getExternalFacingNode(): IGridNode? = null

    override fun setPartHostInfo(side: AEPartLocation?, host: IPartHost?, tile: TileEntity?) {
        this.side = side
        this.host = host
        this.tile = tile
    }

    override fun onActivate(player: PlayerEntity?, hand: Hand?, pos: Vector3d?): Boolean {
        return onPartActivate(player, hand, pos)
    }

    open fun onPartActivate(player: PlayerEntity?, hand: Hand?, pos: Vector3d?): Boolean {
        return false
    }

    override fun onShiftActivate(player: PlayerEntity?, hand: Hand?, pos: Vector3d?): Boolean {
        return onPartShiftActivate(player, hand, pos)
    }

    open fun onPartShiftActivate(player: PlayerEntity?, hand: Hand?, pos: Vector3d?): Boolean {
        return false
    }

    override fun getDrops(drops: MutableList<ItemStack>?, wrenched: Boolean) {
        //
    }

    @OnlyIn(Dist.CLIENT)
    override fun animateTick(world: World?, pos: BlockPos?, r: Random?) {
        //
    }

    override fun onPlacement(player: PlayerEntity?, hand: Hand?, held: ItemStack?, side: AEPartLocation?) {
        this.proxy.setOwner(player)
    }

    override fun canBePlacedOn(what: BusSupport?): Boolean = what == BusSupport.CABLE

    override fun getConfigManager(): IConfigManager? = null

    override fun getInventoryByName(name: String?): IItemHandler? = null
}