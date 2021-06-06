package extracells.integration.mekanism.gas

import appeng.api.implementations.IPowerChannelState
import appeng.api.networking.security.IActionHost
import appeng.api.util.AEPartLocation
import appeng.api.util.DimensionalCoord
import extracells.gui.widget.fluid.IFluidSlotListener
import extracells.integration.Integration
import extracells.part.PartECBase
import mekanism.api.gas.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.EnumFacing
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fml.common.Optional

@Optional.InterfaceList(value = [
    Optional.Interface(iface = "mekanism.api.gas.IGasHandler", modid = "MekanismAPI|gas", striprefs = true),
    Optional.Interface(iface = "mekanism.api.gas.ITubeConnection", modid = "MekanismAPI|gas", striprefs = true)
])
abstract class GasInterfaceBase: PartECBase(), IGasHandler, ITubeConnection, IPowerChannelState, IActionHost, IFluidSlotListener {
    val isMekanismLoaded = Integration.Mods.MEKANISMGAS.isEnabled

    @Optional.Method(modid = "MekanismAPI|gas")
    abstract fun getGasTank(side: EnumFacing?): GasTank

    @Optional.Method(modid = "MekanismAPI|gas")
    override fun receiveGas(side: EnumFacing?, gasStack: GasStack?, doTransfer: Boolean): Int = getGasTank(side).receive(gasStack, doTransfer)

    @Optional.Method(modid = "MekanismAPI|gas")
    override fun canReceiveGas(side: EnumFacing?, gasStack: Gas?): Boolean {
        return side != null && !hasFilter(AEPartLocation.fromFacing(side)) && getGasTank(side).canReceive(gasStack)
    }

    @Optional.Method(modid = "MekanismAPI|gas")
    override fun drawGas(side: EnumFacing?, amount: Int, doTransfer: Boolean): GasStack? {
        return getGasTank(side).draw(amount, doTransfer)
    }

    @Optional.Method(modid = "MekanismAPI|gas")
    override fun canDrawGas(side: EnumFacing?, gasStack: Gas?): Boolean {
        return getGasTank(side).canDraw(gasStack)
    }

    @Optional.Method(modid = "MekanismAPI|gas")
    override fun canTubeConnect(side: EnumFacing?): Boolean = isMekanismLoaded

    abstract fun getFilter(side: AEPartLocation): String

    open fun setFilter(side: AEPartLocation, fluid: Fluid?) {
        if (fluid == null) {
            setFilter(side, "")

            return
        }

        setFilter(side, fluid.name)
    }

    abstract fun setFilter(side: AEPartLocation, field: String)

    fun hasFilter(side: AEPartLocation): Boolean = getFilter(side) != ""

    @Optional.Method(modid = "MekanismAPI|gas")
    fun exportGas(side: EnumFacing?, gas: GasStack?, pos: DimensionalCoord?): Int {

        if (gas == null || pos == null || side == null) {
            return 0
        }

        val tank = getGasTank(side)

        val world = pos.world ?: return 0

        val tile = world.getTileEntity(pos.pos.offset(side)) ?: return 0

        if (tile !is IGasHandler) {
            return 0
        }

        if (tile.canReceiveGas(side.opposite, gas.gas)) {
            return tile.receiveGas(side.opposite, gas, true)
        }

        return 0
    }

    override fun setFluid(index: Int, fluid: Fluid?, player: EntityPlayer?) {
        setFilter(AEPartLocation.fromOrdinal(index), fluid)
    }
}