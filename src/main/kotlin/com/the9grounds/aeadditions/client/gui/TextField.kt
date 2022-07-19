package com.the9grounds.aeadditions.client.gui

import net.minecraft.client.gui.Font
import net.minecraft.client.gui.components.EditBox
import net.minecraft.network.chat.TextComponent

class TextField(font: Font, xPos: Int, yPos: Int, width: Int, height: Int) : EditBox(font, xPos, yPos, width, height, TextComponent("Test123")) {
    
}