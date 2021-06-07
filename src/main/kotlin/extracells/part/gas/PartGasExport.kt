package extracells.part.gas

import appeng.api.config.Actionable
import extracells.integration.Integration
import extracells.integration.mekanism.gas.Capabilities
import extracells.part.fluid.PartFluidExport
import extracells.util.StorageChannels
import mekanism.api.gas.GasStack
import mekanism.api.gas.IGasHandler
import mekanism.api.gas.ITubeConnection
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fml.common.Optional

@Optional.Interface(iface = "mekanism.api.gas.ITubeConnection", modid = "MekanismAPI|gas", striprefs = true)
class PartGasExport : PartFluidExport(), ITubeConnection {

    private val isMekanismEnabled = Integration.Mods.MEKANISMGAS.isEnabled

    override fun doWork(rate: Int, ticksSinceLastCall: Int): Boolean {
        if (isMekanismEnabled) {
            return work(rate, ticksSinceLastCall)
        }

        return false
    }

    protected fun work(rate: Int, ticksSinceLastCall: Int): Boolean {
        val facingTank: IGasHandler = facingGasTank ?: return false

        if (!isActive) {
            return false
        }

        val filter = mutableListOf<Fluid?>()

        if (this.filterFluids[4] != null) {
            filter.add(this.filterFluids[4])
        }

        if (this.filterSize >= 1) {
            var i: Byte = 1.toByte()

            while (i < 9) {
                if (i != 4.toByte()) {
                    filter.add(this.filterFluids[i.toInt()])
                }

                i = (i + 2).toByte()
            }
        }

        if (this.filterSize >= 2) {
            var i: Byte = 1.toByte()

            while (i < 9) {
                if (i != 4.toByte()) {
                    filter.add(this.filterFluids[i.toInt()])
                }

                i = (i + 2).toByte()
            }
        }

        for (fluid in filter) {
            if (fluid != null) {
                val stack = extractGas(StorageChannels.GAS!!.createStack(FluidStack(fluid, rate * ticksSinceLastCall)), Actionable.SIMULATE)

                if (stack != null) {
                    val gasStack = stack.gas as GasStack

                    if (gasStack != null && facingTank.canReceiveGas(facing.opposite, gasStack.gas)) {
                        val filled = facingTank.receiveGas(facing.opposite, gasStack, true)

                        if (filled > 0) {
                            extractGas(StorageChannels.GAS!!.createStack(FluidStack(fluid, filled)), Actionable.MODULATE)
                            return true
                        }
                    }
                }
            }
        }

        return false
    }

    @Optional.Method(modid = "MekanismAPI|gas")
    override fun hasCapability(capabilityClass: Capability<*>?): Boolean = capabilityClass == Capabilities.TUBE_CONNECTION_CAPABILITY

    @Optional.Method(modid = "MekanismAPI|gas")
    override fun <T : Any?> getCapability(capabilityClass: Capability<T>?): T {
        if (capabilityClass == Capabilities.TUBE_CONNECTION_CAPABILITY) {
            return Capabilities.TUBE_CONNECTION_CAPABILITY.cast(this)
        }

        return super.getCapability(capabilityClass)
    }


    override fun canTubeConnect(enumFacing: EnumFacing?): Boolean = if (enumFacing == null) false else enumFacing === this.side.facing
}