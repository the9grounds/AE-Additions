package com.the9grounds.aeadditions.parts

import com.the9grounds.aeadditions.api.IAEAChemicalConfig
import com.the9grounds.aeadditions.api.IAEAHasChemicalConfig
import com.the9grounds.aeadditions.core.AEAChemicalConfig
import net.minecraft.item.ItemStack

class SharedIOBus(itemStack: ItemStack) : AEABasePart(itemStack), IAEAHasChemicalConfig {
    override val chemicalConfig: IAEAChemicalConfig
        get() = AEAChemicalConfig(9)
    
    

}