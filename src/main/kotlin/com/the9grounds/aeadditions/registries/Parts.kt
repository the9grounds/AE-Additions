package com.the9grounds.aeadditions.registries

import appeng.core.Api
import appeng.items.parts.PartModelsHelper
import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.item.AEAPartItem
import com.the9grounds.aeadditions.parts.AEABasePart
import com.the9grounds.aeadditions.parts.chemical.ChemicalConversionMonitorPart
import com.the9grounds.aeadditions.parts.chemical.ChemicalStorageMonitorPart
import com.the9grounds.aeadditions.parts.chemical.ChemicalTerminalPart
import com.the9grounds.aeadditions.parts.fluid.FluidConversionMonitorPart
import com.the9grounds.aeadditions.parts.fluid.FluidStorageMonitorPart
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.KDeferredRegister

object Parts {

    val REGISTRY = KDeferredRegister(ForgeRegistries.ITEMS, AEAdditions.ID)
    
    val CHEMICAL_TERMINAL = createPart(Ids.CHEMICAL_TERMINAL_PART, ChemicalTerminalPart::class.java) { ChemicalTerminalPart(it) }
    val CHEMICAL_STORAGE_MONITOR = createPart(Ids.CHEMICAL_STORAGE_MONITOR, ChemicalStorageMonitorPart::class.java) { ChemicalStorageMonitorPart(it) }
    val CHEMICAL_CONVERSION_MONITOR = createPart(Ids.CHEMICAL_CONVERSION_MONITOR, ChemicalConversionMonitorPart::class.java) { ChemicalConversionMonitorPart(it) }
    
    val FLUID_STORAGE_MONITOR = createPart(Ids.FLUID_STORAGE_MONITOR, FluidStorageMonitorPart::class.java) { FluidStorageMonitorPart(it) }
    val FLUID_CONVERSION_MONITOR = createPart(Ids.FLUID_CONVERSION_MONITOR, FluidConversionMonitorPart::class.java) { FluidConversionMonitorPart(it) }
    
    fun init() {
        
    }
    
    private fun <T: AEABasePart> createPart(id: ResourceLocation, partClass: Class<T>, factory: (ItemStack) -> T) : AEAPartItem<T> {
        Api.instance().registries().partModels().registerModels(PartModelsHelper.createModels(partClass))
        return Items.createItem(id, { properties -> AEAPartItem(properties, factory) }, REGISTRY)
    }
}