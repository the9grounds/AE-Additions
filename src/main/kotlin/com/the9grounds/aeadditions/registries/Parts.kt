package com.the9grounds.aeadditions.registries

import appeng.api.parts.IPart
import appeng.items.parts.PartItem
import appeng.items.parts.PartModelsHelper
import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.integration.appeng.AppEng
import com.the9grounds.aeadditions.parts.chemical.ChemicalTerminalPart
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import java.util.function.Function

object Parts {

    val REGISTRY = KDeferredRegister(ForgeRegistries.ITEMS, AEAdditions.ID)
    
    val CHEMICAL_TERMINAL = createPart(Ids.CHEMICAL_TERMINAL_PART, ChemicalTerminalPart::class.java) { ChemicalTerminalPart(it) }
    
    
    private fun <T: IPart> createPart(id: ResourceLocation, partClass: Class<T>, factory: Function<ItemStack, T>) : PartItem<T> {
        val partModels = AppEng.API!!.registries().partModels()
        
        partModels.registerModels(PartModelsHelper.createModels(partClass))
        return Items.createItem(id, { properties -> PartItem(properties, factory) }, REGISTRY)
    }
}