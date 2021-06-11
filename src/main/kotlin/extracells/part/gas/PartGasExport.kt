package extracells.part.gas

import appeng.api.config.Actionable
import appeng.api.config.SecurityPermissions
import appeng.api.parts.IPart
import appeng.api.parts.IPartCollisionHelper
import appeng.api.parts.IPartModel
import appeng.api.util.AECableType
import extracells.integration.Integration
import extracells.integration.mekanism.gas.Capabilities
import extracells.models.PartModels
import extracells.util.PermissionUtil
import extracells.util.StorageChannels
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

@Optional.Interface(iface = "mekanism.api.gas.ITubeConnection", modid = "MekanismAPI|gas", striprefs = true)
class PartGasExport : PartGasIO(), ITubeConnection {

    override fun getCableConnectionLength(aeCableType: AECableType?): Float {
        return 5.0f
    }

    override fun getBoxes(bch: IPartCollisionHelper) {
        bch.addBox(6.0, 6.0, 12.0, 10.0, 10.0, 13.0)
        bch.addBox(4.0, 4.0, 13.0, 12.0, 12.0, 14.0)
        bch.addBox(5.0, 5.0, 14.0, 11.0, 11.0, 15.0)
        bch.addBox(6.0, 6.0, 15.0, 10.0, 10.0, 16.0)
        bch.addBox(6.0, 6.0, 11.0, 10.0, 10.0, 12.0)
    }

    override fun getPowerUsage(): Double {
        return 1.0
    }

    override fun onActivate(player: EntityPlayer?, hand: EnumHand?, pos: Vec3d?): Boolean {
        return if (PermissionUtil.hasPermission(player, SecurityPermissions.BUILD, this as IPart)) {
            super.onActivate(player, hand, pos)
        } else false
    }

    override fun getStaticModels(): IPartModel {
        if (isActive && isPowered) {
            return PartModels.EXPORT_HAS_CHANNEL
        } else if (isPowered) {
            return PartModels.EXPORT_ON
        }
        return PartModels.EXPORT_OFF
    }

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

        val activeFilters = activeFilters

        for (fluid in activeFilters) {
            if (fluid != null) {
                val stack = extractGas(StorageChannels.GAS!!.createStack(FluidStack(fluid, rate * ticksSinceLastCall)), Actionable.SIMULATE)

                if (stack != null) {
                    val gasStack = stack.gasStack as GasStack

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