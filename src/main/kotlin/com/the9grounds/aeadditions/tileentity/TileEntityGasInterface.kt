package com.the9grounds.aeadditions.tileentity

import appeng.api.AEApi
import appeng.api.config.Actionable
import appeng.api.networking.IGridNode
import appeng.api.networking.security.IActionHost
import appeng.api.networking.ticking.IGridTickable
import appeng.api.networking.ticking.TickRateModulation
import appeng.api.networking.ticking.TickingRequest
import appeng.api.storage.IMEMonitor
import appeng.api.util.AECableType
import appeng.api.util.AEPartLocation
import appeng.api.util.DimensionalCoord
import com.the9grounds.aeadditions.api.IECTileEntity
import com.the9grounds.aeadditions.api.gas.IAEGasStack
import com.the9grounds.aeadditions.container.IContainerListener
import com.the9grounds.aeadditions.container.gas.ContainerGasInterface
import com.the9grounds.aeadditions.gridblock.AEGridBlockGasInterface
import com.the9grounds.aeadditions.gui.gas.GuiGasInterface
import com.the9grounds.aeadditions.gui.widget.fluid.IFluidSlotListener
import com.the9grounds.aeadditions.integration.Integration
import com.the9grounds.aeadditions.integration.mekanism.gas.Capabilities
import com.the9grounds.aeadditions.integration.waila.IWailaTile
import com.the9grounds.aeadditions.network.IGuiProvider
import com.the9grounds.aeadditions.network.packet.PacketGasInterface
import com.the9grounds.aeadditions.util.GasUtil
import com.the9grounds.aeadditions.util.MachineSource
import com.the9grounds.aeadditions.util.NetworkUtil
import com.the9grounds.aeadditions.util.StorageChannels
import crazypants.enderio.conduits.conduit.TileConduitBundle
import gg.galaxygaming.gasconduits.common.conduit.ender.EnderGasConduit
import mekanism.api.gas.*
import mekanism.common.util.GasUtils
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraft.util.text.translation.I18n
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fml.common.FMLCommonHandler
import java.util.*

class TileEntityGasInterface : TileBase(), IECTileEntity, IActionHost, IGridTickable, IGuiProvider,
    IGasHandler, IFluidSlotListener, IWailaTile {

    var listeners: MutableList<IContainerListener> = mutableListOf()

    private var node: IGridNode? = null
    private var isFirstGetGridNode = true
    private var doUpdate = false
    private var gridBlock: AEGridBlockGasInterface = AEGridBlockGasInterface(this)
    var gasTanks: MutableList<GasTank> = mutableListOf()
    var gasConfig: MutableList<Gas?> = mutableListOf()
    var useSides = false

    private var previousGas: Gas? = null
    private var previousGasIndex: Int? = null

    init {
        for (i in 0 until 6) {
            gasTanks.add(i, GasTank(10000))
        }
        for (i in 0 until 6 ) {
            gasConfig.add(i, null)
        }
    }

    override fun getGridNode(dir: AEPartLocation): IGridNode? {
        if (FMLCommonHandler.instance().side.isClient && (getWorld() == null || getWorld().isRemote)) {
            return null
        }
        if (isFirstGetGridNode) {
            isFirstGetGridNode = false
            actionableNode!!.updateState()
        }
        return node
    }

    override fun getCableConnectionType(p0: AEPartLocation): AECableType = AECableType.SMART

    override fun securityBreak() {}

    override fun getLocation(): DimensionalCoord {
        return DimensionalCoord(this)
    }

    override fun receiveGas(direction: EnumFacing?, gas: GasStack?, doTransfer: Boolean): Int {
        if (gas == null) {
            return 0
        }

        val originalAmount = gas.amount

        var gasAmount = gas.amount

        val action = if (doTransfer) {
            Actionable.MODULATE
        } else {
            Actionable.SIMULATE
        }

        val gasTankIndexesForGas = mutableListOf<Int>()

        gasConfig.forEachIndexed { index, item ->
            if (gas.gas.equals(item)) {
                gasTankIndexesForGas.add(index)
            }
        }

        for (gasTankIndex in gasTankIndexesForGas) {
            val tank = gasTanks[gasTankIndex]

            if (tank.canReceive(gas.gas)) {
                val amtReceived = tank.receive(gas, doTransfer)

                gasAmount -= amtReceived

                if (gasAmount <= 0) {
                    break
                }
            }
        }

        if (gasAmount <= 0) {
            return gas.amount
        }

        gas.amount = gasAmount

        val aeGasStack = StorageChannels.GAS!!.createStack(gas) ?: return 0

        val amount = aeGasStack.stackSize

        var notInjected = injectGas(aeGasStack, action) ?: return originalAmount

        var didFillGasTanks = false

        run loop@{
            gasTanks.forEach {
                val amountInserted = it.receive(notInjected.gasStack as GasStack, doTransfer)

                if (amountInserted != 0) {
                    didFillGasTanks = true
                }

                notInjected.stackSize = notInjected.stackSize - amountInserted.toLong()

                if (notInjected.stackSize <= 0) {
                    return@loop
                }
            }
        }

        if (didFillGasTanks) {
            doUpdate = true
        }

        return (originalAmount - notInjected.stackSize).toInt()
    }

    override fun drawGas(direction: EnumFacing?, amount: Int, doDrain: Boolean): GasStack? {
        if (direction == null) {
            return null
        }

        val sideIndex = direction.ordinal

        val gasTank = gasTanks[sideIndex]

        val gas = gasTank.gasType

        if (!gasTank.canDraw(gas)) {
            return null
        }

        val action = if (doDrain) {
            Actionable.MODULATE
        } else {
            Actionable.SIMULATE
        }

        val stack = gasTank.draw(amount, doDrain)

        // Attempt to fill from network

        val gasTankDiff = gasTank.needed

        if (gasTankDiff > 0) {
            val gasStack = StorageChannels.GAS!!.createStack(gas)!!
            gasStack.stackSize = gasTankDiff.toLong()
            val extracted = extractGas(gasStack, action)

            if (extracted != null) {
                gasTank.receive(extracted.gasStack as GasStack, doDrain)

                if (extracted.stackSize.toInt() != gasTankDiff) {
                    doUpdate = true
                }
            }
        }

        previousGas = null
        previousGasIndex = null

        return stack
    }

    private fun getGasTankForGas(gas: Gas): GasTank? {
        var tankIndex: Int? = null
        gasConfig.forEachIndexed { index, gasInTank ->
            if (gasInTank == gas) {
                tankIndex = index
            }
        }

        if (tankIndex == null) {
            return null
        }

        return gasTanks[tankIndex!!]
    }

    private fun getFirstTankWithGas(): GasTank? {
        for (gasTank in gasTanks) {
            if (gasTank.gasType != null && gasTank.getStored() != 0) {
                return gasTank
            }
        }

        return null
    }

    override fun canReceiveGas(direction: EnumFacing?, gas: Gas?): Boolean {
        var canReceive = false

        gasTanks.forEach {
            if (it.gasType == null) {
                canReceive = true

                return@forEach
            }

            if (it.gasType.equals(gas) && it.canReceive(gas)) {
                canReceive = true
            }
        }

        return canReceive
    }

    override fun canDrawGas(direction: EnumFacing?, gas: Gas?): Boolean {
        if (direction == null) {
            return false
        }

        val sideIndex = direction.ordinal

        val tank = gasTanks[sideIndex]

        return tank.gasType != null
    }

    private fun getAmountOfGasInConfig(): Int {
        return gasConfig.filterNotNull().count()
    }

    override fun getPowerUsage(): Double {
        return 1.0
    }

    override fun getActionableNode(): IGridNode {
        if (FMLCommonHandler.instance().effectiveSide.isClient) {
            return node!!
        }
        if (node == null) {
            node = AEApi.instance().grid().createGridNode(gridBlock)
        }
        return node!!
    }

    override fun getTickingRequest(p0: IGridNode): TickingRequest {
        return TickingRequest(1, 20, false, false)
    }

    override fun tickingRequest(node: IGridNode, ticksSinceLastCall: Int): TickRateModulation {

        if (doUpdate) {
            forceUpdate()
        }

        // 1. Attempt to fill config slots
        // 2. Empty gas tanks that aren't attached to a config or if the gas doesn't match.
        // Example: Config is hydrogen and there is oxygen, we want to empty the oxygen

        val storageChannel = StorageChannels.GAS!!

        run {
            var didFillTank = false
            gasConfig.forEachIndexed { index, gas ->
                if (gas == null) {
                    return@forEachIndexed
                }

                val tank = gasTanks[index]

                if (!tank.canReceive(gas)) {
                    return@forEachIndexed
                }

                val stack = storageChannel.createStack(gas)!!

                var amountToExtract = 1500

                if (tank.needed < amountToExtract) {
                    amountToExtract = tank.needed
                }

                stack.stackSize = amountToExtract.toLong()

                val extracted = extractGas(stack, Actionable.MODULATE)

                if (extracted != null) {
                    val amt = tank.receive(extracted.gasStack as GasStack, true)

                    if (amt > 0) {
                        didFillTank = true
                    }
                }
            }

            if (didFillTank) {
                doUpdate = true
            }
        }

        run {
            var didEmptyTank = false

            gasTanks.forEachIndexed { index, gasTank ->
                if (gasTank.getStored() == 0) {
                    return@forEachIndexed
                }

                val config = gasConfig[index]

                if (config == null || !gasTank.gasType.equals(config)) {
                    val stack = storageChannel.createStack(gasTank.stored)!!

                    val notInjected = injectGas(stack, Actionable.MODULATE)

                    var toDraw = stack.stackSize.toInt()

                    if (notInjected != null) {
                        toDraw -= notInjected.stackSize.toInt()
                    }

                    if (toDraw > 0) {
                        val amt = gasTank.draw(toDraw, true)

                        if (amt.amount > 0) {
                            didEmptyTank = true
                        }
                    }
                }

                if (gasTank.getStored() > 0) {
                    val stack = gasTank.stored

                    val set = EnumSet.of(EnumFacing.byIndex(index))

                    set.addAll(getSidesWithGasConduits())

                    val drained = gasTank.draw(GasUtils.emit(stack, this, set), true);

                    if (drained != null && drained.amount > 0) {
                        didEmptyTank = true
                    }
                }
            }

            if (didEmptyTank) {
                doUpdate = true
            }
        }

        return TickRateModulation.FASTER
    }

    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
        super.writeToNBT(compound)
        for (i in 0 until 6) {
            val tank = gasTanks[i]
            val config = gasConfig[i]

            compound.setTag("tank#${i}", tank.write(NBTTagCompound()))

            if (config != null) {
                compound.setString("gasConfig#${i}", config.name)
            }
        }

        return compound
    }

    override fun readFromNBT(compound: NBTTagCompound) {
        super.readFromNBT(compound)

        for (i in 0 until 6) {
            if (compound.hasKey("tank#${i}")) {
                val newTank = GasTank.readFromNBT(compound.getCompoundTag("tank#${i}"))
                gasTanks[i].gas = newTank.gas
            }

            if (compound.hasKey("gasConfig#${i}")) {
                gasConfig[i] = GasRegistry.getGas(compound.getString("gasConfig#${i}"))
            }
        }
    }

    override fun getClientGuiElement(player: EntityPlayer, vararg args: Any?): GuiContainer {
        return GuiGasInterface(player, this)
    }

    override fun getServerGuiElement(player: EntityPlayer, vararg args: Any?): Container {
        return ContainerGasInterface(player, this)
    }

    override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean {
        return (capability == Capabilities.GAS_HANDLER_CAPABILITY)
    }

    override fun <T : Any?> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
        if (capability == Capabilities.GAS_HANDLER_CAPABILITY) {
            return Capabilities.GAS_HANDLER_CAPABILITY.cast(this)
        }

        return super.getCapability(capability, facing)
    }

    protected fun extractGas(toExtract: IAEGasStack, action: Actionable?): IAEGasStack? {
        if (gridBlock == null) {
            return null
        }
        val monitor: IMEMonitor<IAEGasStack> = gridBlock.getGasMonitor() ?: return null
        return monitor.extractItems(toExtract, action, MachineSource(this))
    }

    protected fun injectGas(toInject: IAEGasStack, action: Actionable?): IAEGasStack? {
        if (gridBlock == null) {
            return toInject
        }
        val monitor: IMEMonitor<IAEGasStack> = gridBlock.getGasMonitor() ?: return toInject
        return monitor.injectItems(toInject, action, MachineSource(this))
    }

    fun registerListener(listener: IContainerListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: IContainerListener) {
        listeners.remove(listener)
    }

    private fun forceUpdate() {
        updateBlock()
        for (listener in listeners) {
            listener.updateContainer()
        }
        saveData()
        doUpdate = false
    }

    override fun setFluid(index: Int, fluid: Fluid?, player: EntityPlayer?) {
        gasConfig[index] = GasUtil.getGas(fluid)

        doUpdate = true
    }

    fun syncClientGui(player: EntityPlayer) {
        NetworkUtil.sendToPlayer(PacketGasInterface(gasTanks, gasConfig), player)
    }

    override fun getWailaBody(
        list: MutableList<String>,
        tag: NBTTagCompound,
        side: EnumFacing?
    ): MutableList<String> {
        if (side == null) {
            return list
        }

        val sideIndex = side.ordinal

        list.add(
            I18n.translateToLocal(
                "com.the9grounds.aeadditions.tooltip.direction."
                        + sideIndex
            )
        )

        val tank = GasTank.readFromNBT(tag.getCompoundTag("tank#${sideIndex}"))

        if (tag.hasKey("gasConfig#${sideIndex}")) {
            val gas = GasRegistry.getGas(tag.getString("gasConfig#${sideIndex}"))

            list.add("Selected Gas: ${gas.localizedName}")
            list.add("Amount: ${tank.getStored()} / ${tank.maxGas}")
        } else {
            if (tank.gasType != null) {
                list.add("Filled Gas: ${tank.gasType.localizedName}")
                list.add("Amount: ${tank.getStored()} / ${tank.maxGas}")
            } else {
                list.add("Tank Empty")
            }
        }


        return list
    }

    override fun getWailaTag(tag: NBTTagCompound): NBTTagCompound {
        for (i in 0 until 6) {
            val tank = gasTanks[i]
            val gas = gasConfig[i]

            tag.setTag("tank#${i}", tank.write(NBTTagCompound()))

            if (gas != null) {
                tag.setString("gasConfig#${i}", gas.name)
            }
        }

        return tag
    }

    fun getSidesWithGasConduits(): EnumSet<EnumFacing> {
        if (Integration.Mods.ENDERIOGASCONDUITS.isEnabled) {
            val set = EnumSet.noneOf(EnumFacing::class.java)
            EnumFacing.values().forEach {
                val tile = world.getTileEntity(pos.offset(it))

                if (tile is TileConduitBundle) {
                    tile.serverConduits.forEach { conduit ->
                        if (conduit is EnderGasConduit) {
                            set.add(it)
                        }
                    }
                }
            }

            return set
        }

        return EnumSet.noneOf(EnumFacing::class.java)
    }
}