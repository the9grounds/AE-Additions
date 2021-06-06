package extracells.integration.jei

import mezz.jei.api.IModRegistry
import net.minecraftforge.fluids.FluidStack

object Jei {

    val fluidBlackList = mutableListOf<FluidStack>()

    var registry: IModRegistry? = null

    fun addFluidToBlacklist(fluidStack: FluidStack) {
        if (registry != null) {
            registry!!.jeiHelpers.ingredientBlacklist.addIngredientToBlacklist(fluidStack)
        } else {
            fluidBlackList += fluidStack
        }
    }
}