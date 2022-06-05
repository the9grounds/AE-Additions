package com.the9grounds.aeadditions.registries

import appeng.core.Api
import appeng.items.parts.PartModelsHelper
import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.integration.Mods
import com.the9grounds.aeadditions.item.AEAPartItem
import com.the9grounds.aeadditions.parts.AEABasePart
import com.the9grounds.aeadditions.parts.chemical.*
import com.the9grounds.aeadditions.parts.fluid.FluidConversionMonitorPart
import com.the9grounds.aeadditions.parts.fluid.FluidStorageMonitorPart
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.DIST
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import kotlin.reflect.KClass

object Parts {

    val REGISTRY = KDeferredRegister(ForgeRegistries.ITEMS, AEAdditions.ID)
    
    val CHEMICAL_TERMINAL = createPart(Ids.CHEMICAL_TERMINAL_PART, ChemicalTerminalPart::class, Mods.MEKANISM) { ChemicalTerminalPart(it) }
    val CHEMICAL_STORAGE_MONITOR = createPart(Ids.CHEMICAL_STORAGE_MONITOR, ChemicalStorageMonitorPart::class, Mods.MEKANISM) { ChemicalStorageMonitorPart(it) }
    val CHEMICAL_CONVERSION_MONITOR = createPart(Ids.CHEMICAL_CONVERSION_MONITOR, ChemicalConversionMonitorPart::class, Mods.MEKANISM) { ChemicalConversionMonitorPart(it) }
    val CHEMICAL_EXPORT_BUS = createPart(Ids.CHEMICAL_EXPORT_BUS, ChemicalExportBus::class, Mods.MEKANISM) { ChemicalExportBus(it) }
    val CHEMICAL_IMPORT_BUS = createPart(Ids.CHEMICAL_IMPORT_BUS, ChemicalImportBus::class, Mods.MEKANISM) { ChemicalImportBus(it) }
    
    val FLUID_STORAGE_MONITOR = createPart(Ids.FLUID_STORAGE_MONITOR, FluidStorageMonitorPart::class) { FluidStorageMonitorPart(it) }
    val FLUID_CONVERSION_MONITOR = createPart(Ids.FLUID_CONVERSION_MONITOR, FluidConversionMonitorPart::class) { FluidConversionMonitorPart(it) }
    
    fun init() {
        
    }
    
    private fun <T: AEABasePart> createPart(id: ResourceLocation, partClass: KClass<T>, factory: (ItemStack) -> T) : AEAPartItem<T> {
        
        if (DIST.isClient) {
            Api.instance().registries().partModels().registerModels(PartModelsHelper.createModels(partClass.java))
        }
        
        return Items.createItem(id, { properties -> AEAPartItem(properties, factory) }, REGISTRY)
    }

    private fun <T: AEABasePart> createPart(id: ResourceLocation, partClass: KClass<T>, requiredMod: Mods, factory: (ItemStack) -> T) : AEAPartItem<T> {
        if (requiredMod.isEnabled && DIST.isClient) {
            Api.instance().registries().partModels().registerModels(PartModelsHelper.createModels(partClass.java))
        }
        return Items.createItem(id, { properties -> AEAPartItem(properties, factory) }, REGISTRY, requiredMod)
    }
}