package com.the9grounds.aeadditions.container

import com.the9grounds.aeadditions.Logger
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.container.ContainerType

object ContainerOpener {
    val openers = mutableMapOf<ContainerType<out AbstractContainer<*>>, Opener>()
    
    fun <T : AbstractContainer<T>> add(container: ContainerType<T>, opener: Opener) {
        openers[container] = opener
    }
    
    fun openContainer(type: ContainerType<*>, playerEntity: PlayerEntity, locator: Locator): Boolean {
        val opener = openers[type]
        
        if (opener == null) {
            Logger.warn("Tried opening unregistered container $type")
            
            return false
        }
        
        return opener.open(playerEntity, locator)
    }

    fun interface Opener {
        fun open(playerEntity: PlayerEntity, locator: Locator): Boolean
    }
}