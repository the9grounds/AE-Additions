package com.the9grounds.aeadditions.registries

import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.tile.GasInterfaceTileEntity
import net.minecraft.tileentity.TileEntityType
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.KDeferredRegister

object Tiles {
    val REGISTRY = KDeferredRegister(ForgeRegistries.TILE_ENTITIES, AEAdditions.ID)
    
    val GAS_INTERFACE = REGISTRY.registerObject("gas_interface_tile") { TileEntityType.Builder.create({GasInterfaceTileEntity()}, Blocks.GAS_INTERFACE.block).build(null) }
    
    fun init () {
        
    }
}