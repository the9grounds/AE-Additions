package com.the9grounds.aeadditions.parts

import appeng.api.storage.data.IAEStack
import com.the9grounds.aeadditions.util.Utils
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.math.vector.Vector3d

abstract class AbstractConversionMonitorPart<T: IAEStack<T>>(itemStack: ItemStack) : AbstractMonitorPart<T>(itemStack) {
    override fun onPartActivate(player: PlayerEntity?, hand: Hand?, pos: Vector3d?): Boolean {
        if (isRemote) {
            return true
        }

        if (!proxy.isActive) {
            return false
        }

        if (!Utils.hasPermissions(location, player!!)) {
            return false
        }
        
        val stack = getStackFromHeldItem(player, hand)
        
        if (!canExtractOrInsertFromHeldItem(player, hand, false)) {
            return super.onPartActivate(player, hand, pos)
        }

        if (locked) {
            insertStack(player, hand)
        } else {
            if (stack == watchedItem || stack == null) {
                insertStack(player, hand)
            } else {
                return super.onPartActivate(player, hand, pos)
            }
        }
        
        return true
    }

    override fun onClicked(player: PlayerEntity?, hand: Hand?, pos: Vector3d?): Boolean {
        if (isRemote) {
            return true
        }

        if (!proxy.isActive) {
            return false
        }

        if (!Utils.hasPermissions(location, player!!)) {
            return false
        }

        if (watchedItem == null) {
            return super.onClicked(player, hand, pos)
        }
        
        if (!canExtractOrInsertFromHeldItem(player, hand, true)) {
            return super.onClicked(player, hand, pos)
        }
        
        extractStack(player, hand)
        
        return true
    }
    
    protected abstract fun canExtractOrInsertFromHeldItem(player: PlayerEntity?, hand: Hand?, extract: Boolean): Boolean
    
    protected abstract fun insertStack(player: PlayerEntity?, hand: Hand?)
    
    protected abstract fun extractStack(player: PlayerEntity?, hand: Hand?)
}