package extracells.tileentity

import appeng.api.AEApi
import appeng.api.config.Actionable
import appeng.api.networking.IGridNode
import appeng.api.networking.energy.IEnergyGrid
import appeng.api.networking.security.IActionHost
import appeng.api.util.AECableType
import appeng.api.util.AEPartLocation
import appeng.api.util.DimensionalCoord
import extracells.api.IECTileEntity
import extracells.container.ContainerVibrationChamberFluid
import extracells.gridblock.ECGridBlockVibrantChamber
import extracells.gui.GuiVibrationChamberFluid
import extracells.network.IGuiProvider
import extracells.util.FuelBurnTime
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraft.util.ITickable
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.FluidTank
import net.minecraftforge.fluids.capability.CapabilityFluidHandler
import net.minecraftforge.fluids.capability.IFluidHandler
import net.minecraftforge.fluids.capability.IFluidTankProperties
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

class TileEntityVibrationChamberFluid : TileBase(), IECTileEntity, IActionHost, IPowerStorage, ITickable, IGuiProvider  {

    internal var isFirstGridNode = true
    private final val gridBlock = ECGridBlockVibrantChamber(this)
    internal var node: IGridNode? = null
    @JvmField var burnTime = 0
    private var burnTimeTotal = 0
    private var timer = 0
    private var timerEnergy = 0
    private var energyLeft: Double = .0
    var tank = object : FluidTank(16000) {
        override fun readFromNBT(nbt: NBTTagCompound?): FluidTank {

            if (nbt == null) {
                return this
            }

            if (!nbt.hasKey("Empty")) {
                val fluid = FluidStack.loadFluidStackFromNBT(nbt)
                setFluid(fluid)
            } else {
                setFluid(null)
            }

            return this
        }
    }

    var fluidHandler = FluidHandler()

    override fun getGridNode(p0: AEPartLocation): IGridNode? {
        if (isFirstGridNode && hasWorld() && !world.isRemote) {
            isFirstGridNode = false

            try {
                node = AEApi.instance().grid().createGridNode(gridBlock)
                node!!.updateState()
            } catch (e: Exception) {
                isFirstGridNode = true
            }
        }

        return node
    }

    override fun getCableConnectionType(p0: AEPartLocation): AECableType {
        return AECableType.SMART
    }

    override fun securityBreak() {
        // Do Nothing
    }

    override fun getLocation(): DimensionalCoord {
        return DimensionalCoord(this)
    }

    override fun getPowerUsage(): Double {
        return 0.00
    }

    override fun getActionableNode(): IGridNode {
        return getGridNode(AEPartLocation.INTERNAL)!!
    }

    override fun update() {
        if (hasWorld()) return
        var fluidStack1 = tank.fluid
        if (fluidStack1 != null) {
            fluidStack1 = fluidStack1.copy()
        }
        if (getWorld().isRemote) return
        if (burnTime == burnTimeTotal) {
            if (timer >= 40) {
                updateBlock()
                val fluidStack = tank.fluid
                var localBurnTime = 0
                if (fluidStack != null) {
                    localBurnTime = FuelBurnTime.getBurnTime(fluidStack.fluid)
                } else {
                    localBurnTime = 0
                }
                if (fluidStack != null && localBurnTime > 0) {
                    if (fluidStack.amount >= 250) {
                        if (energyLeft <= 0) {
                            burnTime = 0
                            burnTimeTotal = localBurnTime / 4
                            tank.drain(250, true)
                        }
                    }
                }

                timer = 0
            } else {
                timer += 1
            }
        } else {
            burnTime += 1
            if (timerEnergy == 4) {
                if (energyLeft == 0.00) {
                    val energy = getGridNode(AEPartLocation.INTERNAL)!!.grid.getCache<IEnergyGrid>(IEnergyGrid::class.java)
                    energyLeft = energy.injectPower(24.00, Actionable.MODULATE)
                } else {
                    val energy = getGridNode(AEPartLocation.INTERNAL)!!.grid.getCache<IEnergyGrid>(IEnergyGrid::class.java)
                    energyLeft = energy.injectPower(energyLeft, Actionable.MODULATE)
                }
                timerEnergy = 0
            } else {
                timerEnergy += 1
            }
        }
        if (fluidStack1 == null && tank.fluid == null) return
        if (fluidStack1 == null || tank.fluid == null) {
            updateBlock()
            return
        }

        if (!(fluidStack1 == tank.fluid)) {
            updateBlock()
            return
        }

        if (fluidStack1.amount == tank.fluid!!.amount) {
            updateBlock()
            return
        }
    }

    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
        super.writeToNBT(compound)
        writePowerToNBT(compound)
        compound.setInteger("burnTime", this.burnTime)
        compound.setInteger("burnTimeTotal", this.burnTimeTotal)
        compound.setInteger("timer", this.timer)
        compound.setInteger("timerEnergy", this.timerEnergy)
        compound.setDouble("energyLeft", this.energyLeft)
        tank.writeToNBT(compound)
        return compound
    }

    override fun readFromNBT(compound: NBTTagCompound) {
        super.readFromNBT(compound)
        readPowerFromNBT(compound)
        if (compound.hasKey("burnTime")) this.burnTime = compound.getInteger("burnTime")
        if (compound.hasKey("burnTimeTotal")) this.burnTimeTotal = compound.getInteger("burnTimeTotal")
        if (compound.hasKey("timer")) this.timer = compound.getInteger("timer")
        if (compound.hasKey("timerEnergy")) this.timerEnergy = compound.getInteger("timerEnergy")
        if (compound.hasKey("energyLeft")) this.energyLeft = compound.getDouble("energyLeft")
        tank.readFromNBT(compound)
    }

    fun getBurntTimeScaled(scale: Int): Int {
        return if (burnTime != 0) burnTime * scale / burnTimeTotal else 0
    }

    override fun getUpdateTag(): NBTTagCompound {
        return writeToNBT(NBTTagCompound())
    }

    @SideOnly(Side.CLIENT)
    override fun getClientGuiElement(player: EntityPlayer?, vararg args: Any?): GuiContainer = GuiVibrationChamberFluid(player, this)

    override fun getServerGuiElement(player: EntityPlayer?, vararg args: Any?): Container = ContainerVibrationChamberFluid(player!!.inventory, this)

    override fun <T : Any?> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(fluidHandler)
        }

        return super.getCapability(capability, facing)
    }

    override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return true
        }

        return super.hasCapability(capability, facing)
    }

    inner class FluidHandler : IFluidHandler {
        override fun getTankProperties(): Array<IFluidTankProperties> {
            return tank.tankProperties
        }

        override fun fill(resource: FluidStack?, doFill: Boolean): Int {
            if (resource == null || resource.fluid == null || FuelBurnTime.getBurnTime(resource.fluid) == 0) {
                return 0
            }

            val filled = tank.fill(resource, doFill)

            if (filled != 0 && hasWorld()) {
                updateBlock()
            }

            return filled
        }

        override fun drain(resource: FluidStack?, doDrain: Boolean): FluidStack? {
            return null
        }

        override fun drain(maxDrain: Int, doDrain: Boolean): FluidStack? {
            return null
        }

    }

}