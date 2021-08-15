package com.the9grounds.aeadditions.core

import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslationTextComponent

enum class PlayerMessages {
    isNowLocked, isNowUnlocked;
    
    fun get(): ITextComponent = TranslationTextComponent(getTranslationKey())
        
    fun getTranslationKey(): String = "chat.aeadditions.${toString()}"
}