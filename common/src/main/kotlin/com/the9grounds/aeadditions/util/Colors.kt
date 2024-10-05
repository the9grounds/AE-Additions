package com.the9grounds.aeadditions.util

object Colors {
    fun getRed(color: Int): Float {
        return (color shr 16 and 0xFF) / 255.0f
    }

    fun getGreen(color: Int): Float {
        return (color shr 8 and 0xFF) / 255.0f
    }

    fun getBlue(color: Int): Float {
        return (color and 0xFF) / 255.0f
    }
    
    fun getAlpha(color: Int): Float {
        return (color shr 24 and 0xFF) / 255.0f
    }
}