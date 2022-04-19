package com.the9grounds.aeadditions.block

import com.the9grounds.aeadditions.tileentity.TileEntityAEWirelessTransceiver
import net.minecraft.block.material.Material
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

class BlockAEWirelessTransceiver : BlockAE(Material.IRON, 2.0f, 10.0f) {
    override fun createNewTileEntity(worldIn: World, meta: Int): TileEntity? {
        return TileEntityAEWirelessTransceiver()
    }
}