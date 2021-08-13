package com.the9grounds.aeadditions.helpers

import net.minecraft.util.text.ITextComponent

interface ICustomNameObject {
    val customInventoryName: ITextComponent
    
    val hasCustomInventoryName: Boolean
}