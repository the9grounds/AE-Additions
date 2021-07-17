package com.the9grounds.aeadditions.container.gas

import com.the9grounds.aeadditions.container.ContainerBase
import com.the9grounds.aeadditions.tileentity.TileEntityGasInterface
import net.minecraft.entity.player.EntityPlayer

class ContainerGasInterface(val player: EntityPlayer, @JvmField var gasInterface: TileEntityGasInterface): ContainerBase() {
    override fun canInteractWith(playerIn: EntityPlayer): Boolean {
        TODO("Not yet implemented")
    }
}