package com.the9grounds.aeadditions.client.gui.widget

import net.minecraft.inventory.container.ClickType
import net.minecraft.util.text.ITextComponent

interface IWidget {
    
    fun drawWidget(posX: Int, posY: Int)
    
    fun getTooltip(): List<ITextComponent>
    
    fun mouseClicked(mouesButton: Int, clickType: ClickType)
}