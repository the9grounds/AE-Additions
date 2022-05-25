package com.the9grounds.aeadditions.util

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvent
import net.minecraftforge.fluids.FluidStack

object FluidSounds {

    fun playFillSound(player: PlayerEntity, fluidStack: FluidStack) {
        if (fluidStack.isEmpty) {
            return
        }
        val fillSound = fluidStack.fluid.attributes.getFillSound(fluidStack) ?: return
        playSound(player, fillSound)
    }

    fun playEmptySound(player: PlayerEntity, fluidStack: FluidStack) {
        if (fluidStack.isEmpty) {
            return
        }
        val fillSound = fluidStack.fluid.attributes.getEmptySound(fluidStack) ?: return
        playSound(player, fillSound)
    }
    
    private fun playSound(player: PlayerEntity, fillSound: SoundEvent) {
        // This should just play the sound for the player themselves
        player.playSound(fillSound, SoundCategory.PLAYERS, 1.0f, 1.0f)
    }
}