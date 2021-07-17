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
import com.the9grounds.aeadditions.gridblock.AEGridBlockGasInterface
import com.the9grounds.aeadditions.integration.mekanism.gas.Capabilities
import com.the9grounds.aeadditions.inventory.IInventoryListener
import com.the9grounds.aeadditions.network.IGuiProvider
import com.the9grounds.aeadditions.util.MachineSource
import com.the9grounds.aeadditions.util.StorageChannels
import mekanism.api.gas.Gas
import mekanism.api.gas.GasStack
import mekanism.api.gas.GasTank
import mekanism.api.gas.IGasHandler
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.fml.common.FMLCommonHandler

class TileEntityGasInterface : TileBase(), IECTileEntity, IActionHost, IGridTickable, IGuiProvider, IInventoryListener,
    IGasHandler {
    private var node: IGridNode? = null
    private var isFirstGetGridNode = true
    private var gridBlock: AEGridBlockGasInterface = AEGridBlockGasInterface(this)
    private var gasTanks: MutableList<GasTank> = mutableListOf()
    private var gasConfig: MutableList<Gas?> = mutableListOf()

    private var previousGas: Gas? = null
    private var previousGasIndex: Int? = null

    init {
        for (i in 0 until 5) {
            gasTanks.add(i, GasTank(5000))
        }
        for (i in 0 until 5) {
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

        val action = if (doTransfer) {
            Actionable.MODULATE
        } else {
            Actionable.SIMULATE
        }

        val aeGasStack = StorageChannels.GAS!!.createStack(gas) ?: return 0

        val amount = aeGasStack.stackSize

        var notInjected = injectGas(aeGasStack, action) ?: return amount.toInt()

        run loop@{
            gasTanks.forEach {
                val amountInserted = it.receive(notInjected.gasStack as GasStack, doTransfer)

                notInjected.stackSize = notInjected.stackSize - amountInserted.toLong()

                if (notInjected.stackSize <= 0) {
                    return@loop
                }
            }
        }

        return (amount - notInjected.stackSize).toInt()
    }

    override fun drawGas(direction: EnumFacing?, amount: Int, doDrain: Boolean): GasStack? {
        var gas : Gas? = if (previousGas == null) {
            if (getAmountOfGasInConfig() == 1) {
                gasConfig[0]
            } else {
                null
            }
        } else {
            previousGas
        } ?: return null

        val gasTank = getGasTankForGas(gas!!) ?: return null

        if (!gasTank.canDraw(gas)) {
            return null
        }

        val stack = gasTank.draw(amount, doDrain)

        previousGas = null
        previousGasIndex = null

        return stack
    }

    private fun getGasTankForGas(gas: Gas): GasTank? {
        var tankIndex: Int? = null
        gasConfig.forEachIndexed { index, gas ->
            if (gas == gas) {
                tankIndex = index
            }
        }

        if (tankIndex == null) {
            return null
        }

        return gasTanks[tankIndex!!]
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
        var foundGas: Gas? = null
        var foundGasIndex: Int? = null

        gasConfig.forEachIndexed { index, gasFromConfig ->
            if (gasFromConfig == gas) {
                foundGas = gasFromConfig
                foundGasIndex = index
            }
        }

        if (foundGas == null && getAmountOfGasInConfig() != 1) {
            return false
        }

        previousGas = foundGas
        previousGasIndex = foundGasIndex

        return true
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

        // 1. Attempt to fill config slots
        // 2. Empty gas tanks that aren't attached to a config or if the gas doesn't match.
        // Example: Config is hydrogen and there is oxygen, we want to empty the oxygen

        val storageChannel = StorageChannels.GAS!!

        run {
            gasConfig.forEachIndexed { index, gas ->
                if (gas == null) {
                    return@forEachIndexed
                }

                val tank = gasTanks[index]

                if (!tank.canReceive(gas)) {
                    return@forEachIndexed
                }

                val stack = storageChannel.createStack(gas)!!

                stack.stackSize = 3000

                val extracted = extractGas(stack, Actionable.MODULATE)

                if (extracted != null) {
                    tank.receive(extracted.gasStack as GasStack, true)
                }
            }
        }

        run {
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
                        gasTank.draw(toDraw, true)
                    }
                }
            }
        }

        return TickRateModulation.FASTER
    }

    override fun onInventoryChanged() {
//        TODO("Not yet implemented")
    }

    override fun getClientGuiElement(player: EntityPlayer?, vararg args: Any?): GuiContainer {
        TODO("Not yet implemented")
    }

    override fun getServerGuiElement(player: EntityPlayer?, vararg args: Any?): Container {
        TODO("Not yet implemented")
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
}