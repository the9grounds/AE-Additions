package com.the9grounds.aeadditions.container.gas

import com.the9grounds.aeadditions.container.ContainerBase
import com.the9grounds.aeadditions.container.IContainerListener
import com.the9grounds.aeadditions.gui.gas.GuiGasInterface
import com.the9grounds.aeadditions.network.packet.PacketGasInterface
import com.the9grounds.aeadditions.tileentity.TileEntityGasInterface
import com.the9grounds.aeadditions.util.NetworkUtil
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

class ContainerGasInterface(val player: EntityPlayer, @JvmField var gasInterface: TileEntityGasInterface): ContainerBase(), IContainerListener {

    @SideOnly(Side.CLIENT)
    @JvmField var gui: GuiGasInterface? = null

    override fun canInteractWith(playerIn: EntityPlayer): Boolean = true

    override fun updateContainer() {
        NetworkUtil.sendToPlayer(PacketGasInterface(gasInterface.gasTanks, gasInterface.gasConfig), player)
    }

    override fun onContainerClosed(playerIn: EntityPlayer) {
        gasInterface.removeListener(this)
    }

    init {
        bindPlayerInventory(player.inventory, 8, 149)

        gasInterface.registerListener(this)
    }
}