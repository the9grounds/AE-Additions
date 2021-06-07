package extracells.part.gas

import appeng.api.config.Actionable
import extracells.integration.Integration
import extracells.integration.mekanism.gas.Capabilities
import extracells.integration.mekanism.gas.MekanismGas
import extracells.part.fluid.PartFluidImport
import extracells.util.GasUtil
import extracells.util.MachineSource
import extracells.util.StorageChannels
import mekanism.api.gas.Gas
import mekanism.api.gas.GasStack
import mekanism.api.gas.IGasHandler
import mekanism.api.gas.ITubeConnection
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fml.common.Optional
import kotlin.math.min

@Optional.InterfaceList(value = [
    Optional.Interface(iface = "mekanism.api.gas.IGasHandler", modid = "MekanismAPI|gas", striprefs = true),
    Optional.Interface(iface = "mekanism.api.gas.ITubeConnection", modid = "MekanismAPI|gas", striprefs = true)
])
class PartGasImport: PartFluidImport(), IGasHandler, ITubeConnection {

    private val isMekanismEnabled = Integration.Mods.MEKANISMGAS.isEnabled

    override fun doWork(rate: Int, ticksSinceLastCall: Int): Boolean {
        if ((!isMekanismEnabled) || facingGasTank == null || !isActive) {
            return false
        }

        var empty = true
        val filter = mutableListOf<Fluid>()

        if (this.filterFluids[4] != null) {
            filter.add(this.filterFluids[4])
        }

        if (this.filterSize >= 1) {
            var i = 1.toByte()
            while (i < 9) {
                if (i != 4.toByte()) {
                    filter.add(this.filterFluids[i.toInt()])
                }
                i = (i.toInt() + 2).toByte()
            }
        }

        if (this.filterSize >= 2) {
            var i = 1.toByte()
            while (i < 9) {
                if (i != 4.toByte()) {
                    filter.add(this.filterFluids[i.toInt()])
                }
                i = (i.toInt() + 2).toByte()
            }
        }

        for (fluid in filter) {
            if (fluid != null) {
                empty = false

                if (fillToNetwork(fluid, rate * ticksSinceLastCall)) {
                    return true
                }
            }
        }

        return empty && fillToNetwork(null, rate * ticksSinceLastCall)
    }

    @Optional.Method(modid = "MekanismAPI|gas")
    override fun fillToNetwork(fluid: Fluid?, toDrain: Int): Boolean {
        var drained: GasStack? = null
        val facingTank = facingGasTank
        val side = facing

        val gasType = if (fluid == null) {
            null
        } else {
            val gasStack = GasUtil.getGasStack(FluidStack(fluid, toDrain))

            gasStack?.gas
        }

        if (gasType == null) {
            drained = facingTank.drawGas(side.opposite, toDrain, false)
        } else if (facingTank.canDrawGas(side.opposite, gasType)) {
            drained = facingTank.drawGas(side.opposite, toDrain, false)
        }

        if (drained == null || drained.amount <= 0 || drained.gas == null) {
            return false
        }

        val toFill = StorageChannels.GAS!!.createStack(drained)

        if (toFill == null) {
            return false
        }

        val notInjected = injectGas(toFill, Actionable.MODULATE)

        if (notInjected != null) {
            val amount = (toFill.stackSize - notInjected.stackSize).toInt()

            if (amount > 0) {
                facingTank.drawGas(side.opposite, amount, true)

                return true
            }

            return false
        } else {
            val gasStack = toFill.gasStack
            if (gasStack is GasStack) {
                facingTank.drawGas(side.opposite, gasStack.amount, true)

                return true
            }
        }

        return false
    }


    @Optional.Method(modid = "MekanismAPI|gas")
    override fun receiveGas(side: EnumFacing?, stack: GasStack?, doTransfer: Boolean): Int {
        if (stack == null || stack.amount <= 0 || !canReceiveGas(side, stack.gas)) {
            return 0
        }

        val amount = min(stack.amount, 125 + speedState * 125)

        val gasStack = StorageChannels.GAS!!.createStack(GasStack(stack.gas, amount))

        val notInjected = if (gridBlock == null) {
            gasStack
        } else {
            val monitor = gridBlock.gasMonitor

            if (monitor == null) {
                gasStack
            } else {
                monitor.injectItems(gasStack, Actionable.MODULATE, MachineSource(this))
            }
        }

        if (notInjected == null) {
            return amount
        }

        return amount - notInjected.stackSize.toInt()
    }

    @Optional.Method(modid = "MekanismAPI|gas")
    override fun drawGas(p0: EnumFacing?, p1: Int, p2: Boolean): GasStack? = null

    @Optional.Method(modid = "MekanismAPI|gas")
    override fun canReceiveGas(side: EnumFacing?, gas: Gas?): Boolean {
        val fluid = MekanismGas.fluidGas.get(gas)

        var isEmpty = true

        for (filter in filterFluids) {
            if (filter != null) {
                isEmpty = false
                if (filter == fluid) {
                    return true
                }
            }
        }

        return isEmpty
    }

    @Optional.Method(modid = "MekanismAPI|gas")
    override fun canDrawGas(p0: EnumFacing?, p1: Gas?): Boolean = false

    @Optional.Method(modid = "MekanismAPI|gas")
    override fun hasCapability(capabilityClass: Capability<*>?): Boolean {
        return (capabilityClass == Capabilities.GAS_HANDLER_CAPABILITY || capabilityClass == Capabilities.TUBE_CONNECTION_CAPABILITY)
    }

    @Optional.Method(modid = "MekanismAPI|gas")
    override fun <T : Any?> getCapability(capabilityClass: Capability<T>?): T {
        if (capabilityClass == Capabilities.GAS_HANDLER_CAPABILITY) {
            return Capabilities.GAS_HANDLER_CAPABILITY.cast(this)
        } else if (capabilityClass == Capabilities.TUBE_CONNECTION_CAPABILITY) {
            return Capabilities.TUBE_CONNECTION_CAPABILITY.cast(this)
        }

        return super.getCapability(capabilityClass)
    }

    override fun canTubeConnect(enumFacing: EnumFacing?): Boolean = if (enumFacing == null) false else enumFacing == this.side.facing
}