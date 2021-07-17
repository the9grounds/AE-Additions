package com.the9grounds.aeadditions.gui.gas

import com.the9grounds.aeadditions.Constants
import com.the9grounds.aeadditions.container.gas.ContainerGasInterface
import com.the9grounds.aeadditions.gui.GuiBase
import com.the9grounds.aeadditions.tileentity.TileEntityGasInterface
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation

class GuiGasInterface(val player: EntityPlayer, val gasInterface: TileEntityGasInterface) : GuiBase<ContainerGasInterface>(
    ResourceLocation(Constants.MOD_ID, "textures/gui/gui_interface.png"), ContainerGasInterface(player, gasInterface))
{
    init {

    }
}