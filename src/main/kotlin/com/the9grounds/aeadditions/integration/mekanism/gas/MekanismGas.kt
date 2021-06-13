package com.the9grounds.aeadditions.integration.mekanism.gas

import appeng.api.AEApi
import com.the9grounds.aeadditions.api.AEAApi
import com.the9grounds.aeadditions.api.gas.IGasStorageChannel
import com.the9grounds.aeadditions.integration.Integration
import com.the9grounds.aeadditions.integration.jei.Jei
import mekanism.api.gas.Gas
import mekanism.api.gas.GasRegistry
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fluids.FluidStack

object MekanismGas {
    @JvmField val fluidGas = mutableMapOf<Gas, Fluid>()

    @JvmStatic fun preInit() {
        AEApi.instance().storage().registerStorageChannel(IGasStorageChannel::class.java, GasStorageChannel())
    }

    @JvmStatic fun init() {
        // Do Nothing
    }

    @JvmStatic fun postInit() {
        val iterator = GasRegistry.getRegisteredGasses().iterator()

        while(iterator.hasNext()) {
            val gas = iterator.next()

            val fluid = GasFluid(gas)

            if ((!FluidRegistry.isFluidRegistered(fluid)) && FluidRegistry.registerFluid(fluid)) {
                fluidGas.set(gas, fluid)

                if (Integration.Mods.JEI.isEnabled) {
                    Jei.addFluidToBlacklist(FluidStack(fluid, 1000))
                }
            }
        }

        AEAApi.instance().addFluidToShowBlacklist(GasFluid::class.java)
        AEAApi.instance().addFluidToStorageBlacklist(GasFluid::class.java)
    }

    fun getGasResourceLocation(gasName: String) = GasRegistry.getGas(gasName).icon

    class GasFluid(@JvmField val gas: Gas): Fluid("ec.internal." + gas.name, gas.icon, gas.icon) {
        override fun getLocalizedName(stack: FluidStack?): String = gas.localizedName
    }
}