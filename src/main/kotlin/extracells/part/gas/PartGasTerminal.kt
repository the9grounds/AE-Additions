package extracells.part.gas

import appeng.api.config.Actionable
import extracells.container.ContainerTerminal
import extracells.container.StorageType
import extracells.gui.GuiTerminal
import extracells.integration.Integration
import extracells.part.fluid.PartFluidTerminal
import extracells.util.GasUtil
import extracells.util.StorageChannels
import mekanism.api.gas.GasStack
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fml.common.Optional
import org.apache.commons.lang3.tuple.MutablePair
import kotlin.math.min

class PartGasTerminal : PartFluidTerminal() {
    val isMekanismLoaded = Integration.Mods.MEKANISMGAS.isEnabled

    var doNextFill = false

    override fun isItemValidForInputSlot(i: Int, itemStack: ItemStack?): Boolean = GasUtil.isGasContainer(itemStack)

    override fun doWork() {
        if (isMekanismLoaded) {
            doWorkGas()
        }
    }

    @Optional.Method(modid = "MekanismAPI|gas")
    fun doWorkGas() {
        val secondSlot: ItemStack? = inventory.getStackInSlot(1)

        if (secondSlot != null && !secondSlot.isEmpty && secondSlot.count >= secondSlot.maxStackSize) {
            return
        }

        var container: ItemStack? = inventory.getStackInSlot(0)

        if (container == null || container.isEmpty) {
            doNextFill = false
        }

        if (!GasUtil.isGasContainer(container) || container == null) {
            return
        }

        container = container.copy()
        container.count = 1

        val gridBlock = gridBlock ?: return
        val monitor = gridBlock.gasMonitor ?: return

        val gasStack = GasUtil.getGasFromContainer(container)

        if (GasUtil.isEmpty(container) || (gasStack.amount < GasUtil.getCapacity(container) && GasUtil.getFluidStack(gasStack).fluid == currentFluid && doNextFill)) {
            if (currentFluid == null) {
                return
            }

            val capacity = GasUtil.getCapacity(container)

            val result = monitor.extractItems(StorageChannels.GAS!!.createStack(GasStack(GasUtil.getGas(currentFluid), capacity)), Actionable.SIMULATE, this.machineSource)

            var proposedAmount = 0
            if (result == null) {
                proposedAmount = 0
            } else if (gasStack == null) {
                proposedAmount = min(capacity, result.stackSize.toInt())
            } else {
                proposedAmount = min(capacity - gasStack.amount, result.stackSize.toInt())
            }

            val filledContainer: MutablePair<Int, ItemStack> = GasUtil.fillStack(container, GasUtil.getGasStack(FluidStack(currentFluid, proposedAmount)))

            val filledContainerItemStack = filledContainer.right

            val gasStack2 = GasUtil.getGasFromContainer(filledContainerItemStack)
            if (gasStack2 == null) {
                doNextFill = false
            } else if (container.count == 1 && gasStack2.amount < GasUtil.getCapacity(filledContainerItemStack)) {
                inventory.setInventorySlotContents(0, filledContainerItemStack)
                monitor.extractItems(StorageChannels.GAS.createStack(GasStack(GasUtil.getGas(currentFluid), filledContainer.left)), Actionable.MODULATE, machineSource)
                doNextFill = true
            } else if (fillSecondSlot(filledContainerItemStack)) {
                monitor.extractItems(StorageChannels.GAS.createStack(GasStack(GasUtil.getGas(currentFluid), filledContainer.left)), Actionable.MODULATE, machineSource)
                decreaseFirstSlot()
                doNextFill = false
            }
        } else {
            val containerGas = GasUtil.getGasFromContainer(container)

            val drainedContainer: MutablePair<Int, ItemStack> = GasUtil.drainStack(container.copy(), containerGas)
            val gasStack = containerGas.copy()

            gasStack.amount = drainedContainer.left

            val notInjected = monitor.injectItems(StorageChannels.GAS!!.createStack(gasStack), Actionable.SIMULATE, machineSource)

            if (notInjected != null) {
                return
            }

            val emptyContainer = drainedContainer.right

            if (emptyContainer != null && !emptyContainer.isEmpty && GasUtil.getGasFromContainer(emptyContainer) != null && emptyContainer.count == 1) {
                monitor.injectItems(StorageChannels.GAS.createStack(gasStack), Actionable.MODULATE, this.machineSource)
                inventory.setInventorySlotContents(0, emptyContainer)
            } else if (emptyContainer == null || emptyContainer.isEmpty || fillSecondSlot(emptyContainer)) {
                monitor.injectItems(StorageChannels.GAS!!.createStack(containerGas), Actionable.MODULATE, machineSource)
                decreaseFirstSlot()
            }
        }
    }

    override fun getServerGuiElement(player: EntityPlayer?): Any? {
        if (isMekanismLoaded) {
            return ContainerTerminal(this, player, StorageType.GAS)
        }

        return null
    }

    override fun getClientGuiElement(player: EntityPlayer?): Any? {
        if (isMekanismLoaded) {
            return GuiTerminal(this, player, StorageType.GAS)
        }

        return null
    }
}