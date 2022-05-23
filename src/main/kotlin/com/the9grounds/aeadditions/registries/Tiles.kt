package com.the9grounds.aeadditions.registries

import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.tile.ChemicalInterfaceTileEntity
import net.minecraft.tileentity.TileEntityType
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.KDeferredRegister

object Tiles {
    val REGISTRY = KDeferredRegister(ForgeRegistries.TILE_ENTITIES, AEAdditions.ID)
    
    val CHEMICAL_INTERFACE = REGISTRY.registerObject("chemical_interface_tile") { TileEntityType.Builder.create({ChemicalInterfaceTileEntity()}, Blocks.CHEMICAL_INTERFACE.block).build(null) }
    
    fun init () {
        
    }
}