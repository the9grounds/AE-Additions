package com.the9grounds.aeadditions.part.gas

import appeng.api.config.Actionable
import appeng.api.config.SecurityPermissions
import appeng.api.parts.IPart
import appeng.api.parts.IPartCollisionHelper
import appeng.api.parts.IPartModel
import appeng.api.util.AECableType
import com.the9grounds.aeadditions.integration.Integration
import com.the9grounds.aeadditions.integration.mekanism.gas.Capabilities
import com.the9grounds.aeadditions.integration.mekanism.gas.MekanismGas
import com.the9grounds.aeadditions.models.PartModels
import com.the9grounds.aeadditions.util.*
import mekanism.api.gas.Gas
import mekanism.api.gas.GasStack
import mekanism.api.gas.IGasHandler
import mekanism.api.gas.ITubeConnection
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.Vec3d
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fml.common.Optional
import kotlin.math.min

@Optional.InterfaceList(value = [
    Optional.Interface(iface = "mekanism.api.gas.IGasHandler", modid = "MekanismAPI|gas", striprefs = true),
    Optional.Interface(iface = "mekanism.api.gas.ITubeConnection", modid = "MekanismAPI|gas", striprefs = true)
])
class PartGasImport: PartGasIO(), IGasHandler, ITubeConnection {

    override fun getCableConnectionLength(aeCableType: AECableType? ): Float {
        return 5.0f
    }

    override fun getBoxes(bch: IPartCollisionHelper) {
        bch.addBox(4.0, 4.0, 14.0, 12.0, 12.0, 16.0)
        bch.addBox(5.0, 5.0, 13.0, 11.0, 11.0, 14.0)
        bch.addBox(6.0, 6.0, 12.0, 10.0, 10.0, 13.0)
        bch.addBox(6.0, 6.0, 11.0, 10.0, 10.0, 12.0)
    }

    override fun getPowerUsage(): Double {
        return 1.0
    }

    override fun onActivate(player: EntityPlayer?, enumHand: EnumHand?, pos: Vec3d?): Boolean {
        return PermissionUtil.hasPermission(
            player,
            SecurityPermissions.BUILD,
            this as IPart
        ) && super.onActivate(player, enumHand, pos)
    }

    override fun getStaticModels(): IPartModel {
        return if (isActive && isPowered) {
            PartModels.IMPORT_HAS_CHANNEL
        } else if (isPowered) {
            PartModels.IMPORT_ON
        } else {
            PartModels.IMPORT_OFF
        }
    }

    private val isMekanismEnabled = Integration.Mods.MEKANISMGAS.isEnabled

    override fun doWork(rate: Int, ticksSinceLastCall: Int): Boolean {
        if ((!isMekanismEnabled) || facingGasTank == null || !isActive) {
            return false
        }

        var empty = true
        val activeFilters = activeFilters

        for (fluid in activeFilters) {
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
    fun fillToNetwork(fluid: Fluid?, toDrain: Int): Boolean {
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
                facingTank.drawGas(side.opposite, toDrain, true)

                return true
            }
        }

        return false
    }


    // This is bugged, it duplicates the amount
    @Optional.Method(modid = "MekanismAPI|gas")
    override fun receiveGas(side: EnumFacing?, stack: GasStack?, doTransfer: Boolean): Int {
        if (stack == null || stack.amount <= 0 || !canReceiveGas(side, stack.gas) || !isActive) {
            return 0
        }

        val action = if (doTransfer) {
            Actionable.MODULATE
        } else {
            Actionable.SIMULATE
        }

        // Workaround to fix duplicate gas when the gas tank auto ejects
        val amount = min(stack.amount, 125 + speedState * 125)

        val gasStack = StorageChannels.GAS!!.createStack(GasStack(stack.gas, amount))

        val notInjected = injectGas(gasStack, action) ?: return amount

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