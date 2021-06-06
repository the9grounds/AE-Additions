package extracells.part.gas

import appeng.api.config.AccessRestriction
import extracells.api.gas.IAEGasStack
import extracells.integration.Integration
import extracells.inventory.cell.HandlerPartStorageGas
import extracells.inventory.cell.IHandlerPartBase
import extracells.part.fluid.PartFluidStorage
import extracells.util.StorageChannels
import mekanism.api.gas.GasStack
import net.minecraftforge.fml.common.Optional

class PartGasStorage : PartFluidStorage() {
    init {
        handler = HandlerPartStorageGas(this)
        channel = StorageChannels.GAS!!
    }

    val isMekanismGasEnabled = Integration.Mods.MEKANISMGAS.isEnabled

    var gasList = mutableMapOf<Any, Int>()

    override fun updateNeighbor() {
        if (isMekanismGasEnabled) {
            updateNeighborGases()
        }
    }

    @Optional.Method(modid = "MekanismAPI|gas")
    private fun updateNeighborGases() {
        gasList = mutableMapOf()

        if ((access == AccessRestriction.READ) || (access == AccessRestriction.READ_WRITE)) {
            for (stack in (handler as IHandlerPartBase<IAEGasStack>).getAvailableItems(StorageChannels.GAS!!.createList())) {
                val gasStack = stack.gas as GasStack

                gasList.set(gasStack, gasStack.amount)
            }
        }
    }

    @Optional.Method(modid = "MekanismAPI|gas")
    override fun wasChanged(): Boolean {
        var fluids = mutableMapOf<Any, Int>()

        for (stack in (handler as IHandlerPartBase<IAEGasStack>).getAvailableItems(StorageChannels.GAS!!.createList())) {
            val gasStack = stack.gasStack as GasStack

            fluids.set(gasStack, gasStack.amount)
        }

        return !fluids.equals(gasList)
    }

}