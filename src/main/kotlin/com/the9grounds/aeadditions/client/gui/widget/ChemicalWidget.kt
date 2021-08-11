package com.the9grounds.aeadditions.client.gui.widget

import com.the9grounds.aeadditions.client.gui.me.chemical.ChemicalTerminalScreen
import mekanism.api.chemical.Chemical
import net.minecraft.client.gui.AbstractGui

class ChemicalWidget(val guiTerminal: ChemicalTerminalScreen, val height: Int, val width: Int, val chemical: Chemical<*>) : AbstractGui() {
    
}