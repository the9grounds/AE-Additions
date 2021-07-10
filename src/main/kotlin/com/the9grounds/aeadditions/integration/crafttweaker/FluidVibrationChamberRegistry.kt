package com.the9grounds.aeadditions.integration.crafttweaker

import com.the9grounds.aeadditions.util.FuelBurnTime
import crafttweaker.api.item.IIngredient
import net.minecraftforge.fluids.FluidStack
import stanhebben.zenscript.annotations.ZenClass
import stanhebben.zenscript.annotations.ZenMethod
import stanhebben.zenscript.annotations.ZenMethodStatic

@ZenClass("mods.aeadditions.FluidVibrationChamber")
class FluidVibrationChamberRegistry {

    companion object {
        @ZenMethod
        @JvmStatic fun registerFuel(item: IIngredient, burnTime: Int) {
            val fluidStack: FluidStack = item.liquids[0].internal as? FluidStack ?: return

            val fluid = fluidStack.fluid

            FuelBurnTime.registerFuel(fluid, burnTime)
        }
    }
}