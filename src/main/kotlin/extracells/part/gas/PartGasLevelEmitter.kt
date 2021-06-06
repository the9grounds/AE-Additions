package extracells.part.gas

import extracells.api.gas.IAEGasStack
import extracells.part.fluid.PartFluidLevelEmitter
import extracells.util.GasUtil

class PartGasLevelEmitter : PartFluidLevelEmitter() {
    val isGas = true

    override fun onStackChangeGas(fullStack: IAEGasStack?, diffStack: IAEGasStack?) {
        if (diffStack != null && (diffStack.gas == GasUtil.getGas(this.selectedFluid))) {
            this.currentAmount = fullStack?.stackSize ?: 0

            val node = gridNode

            if (node != null) {
                isActive = node.isActive
                host.markForUpdate()
                notifyTargetBlock(hostTile, facing)
            }
        }
    }
}