package com.the9grounds.aeadditions.util

import buildcraft.api.fuels.BuildcraftFuelRegistry
import com.the9grounds.aeadditions.integration.Integration
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fml.common.Optional


object FuelBurnTime {
    private val fluidBurnTimes = mutableMapOf<Fluid, Int>()

    @JvmStatic fun registerFuel(fluid: Fluid, burnTime: Int) {
        if (!fluidBurnTimes.contains(fluid)) {
            fluidBurnTimes.put(fluid, burnTime)
        }
    }

    @JvmStatic fun getBurnTime(fluid: Fluid): Int {
        if (fluidBurnTimes.contains(fluid)) {
            return fluidBurnTimes.get(fluid)!!
        }
        if (Integration.Mods.BCFUEL.isEnabled) {
            return getBCBurnTime(fluid)
        }

        return 0
    }

    @Optional.Method(modid = "BuildCraftAPI|fuels")
    private fun getBCBurnTime(fluid: Fluid): Int {
        val iterator = BuildcraftFuelRegistry.fuel.fuels.iterator()
        while (iterator.hasNext()) {
            val fuel = iterator.next()
            if (fuel.fluid.fluid == fluid) {
                return fuel.totalBurningTime
            }
        }
        return 0
    }
}