package com.the9grounds.aeadditions.registries.client

import com.the9grounds.aeadditions.client.gui.me.chemical.ChemicalIOBusScreen
import com.the9grounds.aeadditions.client.gui.me.chemical.ChemicalInterfaceScreen
import com.the9grounds.aeadditions.client.gui.me.chemical.ChemicalTerminalScreen
import com.the9grounds.aeadditions.container.AbstractContainer
import com.the9grounds.aeadditions.container.chemical.ChemicalIOContainer
import com.the9grounds.aeadditions.container.chemical.ChemicalInterfaceContainer
import com.the9grounds.aeadditions.container.chemical.ChemicalTerminalContainer
import net.minecraft.client.gui.ScreenManager
import net.minecraft.client.gui.screen.inventory.ContainerScreen
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.container.ContainerType
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
object Screens {
    
    fun init() {
        register(ChemicalTerminalContainer.TYPE) { container, playerInventory, iTextComponent -> ChemicalTerminalScreen(container, playerInventory, iTextComponent) }
        register(ChemicalIOContainer.IMPORT_BUS) { chemicalIOContainer, playerInventory, iTextComponent ->
            ChemicalIOBusScreen(
                chemicalIOContainer,
                playerInventory,
                iTextComponent
            )
        }
        register(ChemicalIOContainer.EXPORT_BUS) { chemicalIOContainer, playerInventory, iTextComponent ->
            ChemicalIOBusScreen(
                chemicalIOContainer,
                playerInventory,
                iTextComponent
            )
        }
        
        register(ChemicalInterfaceContainer.CHEMICAL_INTERFACE) { chemicalInterfaceContainer, playerInventory, iTextComponent -> 
            ChemicalInterfaceScreen(
                chemicalInterfaceContainer,
                playerInventory,
                iTextComponent
            )
        }
    }
    
    
    private fun <T: AbstractContainer<T>>register(type: ContainerType<T>, factory: (T, PlayerInventory, ITextComponent) -> ContainerScreen<T>) {
        ScreenManager.registerFactory(type) { container, playerInventory, title ->
            factory(container, playerInventory, title)
        }
    }
}