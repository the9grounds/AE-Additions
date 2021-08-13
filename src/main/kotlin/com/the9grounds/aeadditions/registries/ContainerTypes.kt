package com.the9grounds.aeadditions.registries

import com.the9grounds.aeadditions.container.chemical.ChemicalTerminalContainer
import net.minecraft.inventory.container.ContainerType
import net.minecraftforge.registries.IForgeRegistry

object ContainerTypes {
    
    fun init(registry: IForgeRegistry<ContainerType<*>>) {
        registry.registerAll(
            ChemicalTerminalContainer.TYPE
        )
    }
}