package com.the9grounds.aeadditions.registries.client

import appeng.client.gui.ScreenRegistration
import com.the9grounds.aeadditions.client.gui.me.chemical.ChemicalTerminalScreen
import com.the9grounds.aeadditions.container.AbstractContainer
import com.the9grounds.aeadditions.container.chemical.ChemicalTerminalContainer
import net.minecraft.client.gui.ScreenManager
import net.minecraft.client.gui.screen.inventory.ContainerScreen
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.container.Container
import net.minecraft.inventory.container.ContainerType
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
object Screens {
    
    fun init() {
        register(ChemicalTerminalContainer.TYPE) { container, playerInventory, iTextComponent -> ChemicalTerminalScreen(container, playerInventory, iTextComponent) }
    }
    
    
    private fun <T: AbstractContainer<T>>register(type: ContainerType<T>, factory: (T, PlayerInventory, ITextComponent) -> ContainerScreen<T>) {
        ScreenManager.registerFactory(type) { container, playerInventory, title ->
            factory(container, playerInventory, title)
        }
    }
}