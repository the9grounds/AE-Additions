package com.the9grounds.aeadditions.registries

import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.block.GasInterfaceBlock
import com.the9grounds.aeadditions.core.BlockDefinition
import com.the9grounds.aeadditions.integration.Mods
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.item.BlockItem
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.KDeferredRegister

object Blocks {
    val REGISTRY = KDeferredRegister(ForgeRegistries.BLOCKS, AEAdditions.ID)
    
    val BLOCKS = mutableListOf<Block>()
    
    val GAS_INTERFACE = createBlock(Ids.GAS_INTERFACE, Material.IRON, Mods.MEKANISM) { properties -> GasInterfaceBlock(properties)}
    
    fun init () {
        
    }
    
    fun <T: Block> createBlock(id: ResourceLocation, material: Material, factory: (AbstractBlock.Properties) -> T): BlockDefinition<T> {
        val block = constructBlock(material, factory, id)
        
        val item = Items.createItem(id) { properties -> BlockItem(block, properties) }

        return BlockDefinition(block, item)
    }

    fun <T: Block> createBlock(id: ResourceLocation, material: Material, requiredMod: Mods, factory: (AbstractBlock.Properties) -> T): BlockDefinition<T> {
        val block = constructBlock(material, factory, id)

        val item = Items.createItem(id, { properties -> BlockItem(block, properties) }, requiredMod)

        return BlockDefinition(block, item)
    }

    private fun <T : Block> constructBlock(
        material: Material,
        factory: (AbstractBlock.Properties) -> T,
        id: ResourceLocation
    ): T {
        val props = AbstractBlock.Properties.create(material)

        val block = factory(props)

        BLOCKS.add(block)

        REGISTRY.registerObject(id.path) {
            block
        }
        return block
    }
}