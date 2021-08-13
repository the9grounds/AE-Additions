package com.the9grounds.aeadditions.registries

import appeng.api.parts.IPart
import appeng.core.Api
import appeng.items.parts.PartItem
import appeng.items.parts.PartModelsHelper
import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.integration.appeng.AppEng
import com.the9grounds.aeadditions.item.AEAPartItem
import com.the9grounds.aeadditions.parts.AEABasePart
import com.the9grounds.aeadditions.parts.chemical.ChemicalTerminalPart
import com.the9grounds.aeadditions.registries.client.PartModels
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import java.util.function.Function
import kotlin.reflect.KClass

object Parts {

    val REGISTRY = KDeferredRegister(ForgeRegistries.ITEMS, AEAdditions.ID)
    
    val CHEMICAL_TERMINAL = createPart(Ids.CHEMICAL_TERMINAL_PART, ChemicalTerminalPart::class.java) { ChemicalTerminalPart(it) }
    
    fun init() {
        
    }
    
    private fun <T: AEABasePart> createPart(id: ResourceLocation, partClass: Class<T>, factory: (ItemStack) -> T) : AEAPartItem<T> {
        Api.instance().registries().partModels().registerModels(PartModelsHelper.createModels(partClass))
        return Items.createItem(id, { properties -> AEAPartItem(properties, factory) }, REGISTRY)
    }
}