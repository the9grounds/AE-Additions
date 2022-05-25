package com.the9grounds.aeadditions.registries

import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.core.CreativeTab
import com.the9grounds.aeadditions.integration.Mods
import com.the9grounds.aeadditions.item.ChemicalDummyItem
import com.the9grounds.aeadditions.item.storage.ChemicalStorageCell
import com.the9grounds.aeadditions.item.storage.FluidStorageCell
import com.the9grounds.aeadditions.item.storage.PhysicalStorageCell
import com.the9grounds.aeadditions.item.storage.StorageComponentItem
import net.minecraft.item.Item
import net.minecraft.item.Rarity
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.KDeferredRegister

object Items {
    val REGISTRY = KDeferredRegister(ForgeRegistries.ITEMS, AEAdditions.ID)
    
    val ITEMS = mutableListOf<Item>()

    val ITEM_STORAGE_COMPONENT_256k = createItem(Ids.ITEM_STORAGE_COMPONENT_256) { properties ->  StorageComponentItem(properties, 256)}
    val ITEM_STORAGE_COMPONENT_1024k = createItem(Ids.ITEM_STORAGE_COMPONENT_1024) { properties ->  StorageComponentItem(properties, 1024)}
    val ITEM_STORAGE_COMPONENT_4096k = createItem(Ids.ITEM_STORAGE_COMPONENT_4096) { properties ->  StorageComponentItem(properties.rarity(Rarity.RARE), 4096)}
    val ITEM_STORAGE_COMPONENT_16384k = createItem(Ids.ITEM_STORAGE_COMPONENT_16384) { properties ->  StorageComponentItem(properties.rarity(Rarity.EPIC), 16384)}

    val FLUID_STORAGE_COMPONENT_256k = createItem(Ids.FLUID_STORAGE_COMPONENT_256) { properties -> StorageComponentItem(properties, 256) }
    val FLUID_STORAGE_COMPONENT_1024k = createItem(Ids.FLUID_STORAGE_COMPONENT_1024) { properties -> StorageComponentItem(properties.rarity(Rarity.RARE), 1024) }
    val FLUID_STORAGE_COMPONENT_4096k = createItem(Ids.FLUID_STORAGE_COMPONENT_4096) { properties -> StorageComponentItem(properties.rarity(Rarity.EPIC), 4096) }
    
    val CHEMICAL_STORAGE_COMPONENT_1k = createItem(Ids.CHEMICAL_STORAGE_COMPONENT_1, { properties -> StorageComponentItem(properties, 1) }, Mods.MEKANISM)
    val CHEMICAL_STORAGE_COMPONENT_4k = createItem(Ids.CHEMICAL_STORAGE_COMPONENT_4, { properties -> StorageComponentItem(properties, 4) }, Mods.MEKANISM)
    val CHEMICAL_STORAGE_COMPONENT_16k = createItem(Ids.CHEMICAL_STORAGE_COMPONENT_16, { properties -> StorageComponentItem(properties, 16) }, Mods.MEKANISM)
    val CHEMICAL_STORAGE_COMPONENT_64k = createItem(Ids.CHEMICAL_STORAGE_COMPONENT_64, { properties -> StorageComponentItem(properties, 64) }, Mods.MEKANISM)
    val CHEMICAL_STORAGE_COMPONENT_256k = createItem(Ids.CHEMICAL_STORAGE_COMPONENT_256, { properties -> StorageComponentItem(properties, 256) }, Mods.MEKANISM)
    val CHEMICAL_STORAGE_COMPONENT_1024k = createItem(Ids.CHEMICAL_STORAGE_COMPONENT_1024, { properties -> StorageComponentItem(properties.rarity(Rarity.RARE), 1024) }, Mods.MEKANISM)
    val CHEMICAL_STORAGE_COMPONENT_4096k = createItem(Ids.CHEMICAL_STORAGE_COMPONENT_4096, { properties -> StorageComponentItem(properties.rarity(Rarity.EPIC), 4096) }, Mods.MEKANISM)

    val ITEM_STORAGE_CELL_256k = createItem(Ids.ITEM_STORAGE_CELL_256) { properties ->  PhysicalStorageCell(properties.maxStackSize(1), ITEM_STORAGE_COMPONENT_256k, 256, 3.0, 1024)}
    val ITEM_STORAGE_CELL_1024k = createItem(Ids.ITEM_STORAGE_CELL_1024) { properties ->  PhysicalStorageCell(properties.maxStackSize(1), ITEM_STORAGE_COMPONENT_1024k, 256, 4.0, 4096)}
    val ITEM_STORAGE_CELL_4096k = createItem(Ids.ITEM_STORAGE_CELL_4096) { properties ->  PhysicalStorageCell(properties.maxStackSize(1).rarity(Rarity.RARE), ITEM_STORAGE_COMPONENT_4096k, 256, 5.0, 8192)}
    val ITEM_STORAGE_CELL_16384k = createItem(Ids.ITEM_STORAGE_CELL_16384) { properties ->  PhysicalStorageCell(properties.maxStackSize(1).rarity(Rarity.EPIC), ITEM_STORAGE_COMPONENT_16384k, 256, 6.0, 32768)}

    val FLUID_STORAGE_CELL_256k = createItem(Ids.FLUID_STORAGE_CELL_256) { properties ->  FluidStorageCell(properties.maxStackSize(1), FLUID_STORAGE_COMPONENT_256k, 256, 3.0, 1024)}
    val FLUID_STORAGE_CELL_1024k = createItem(Ids.FLUID_STORAGE_CELL_1024) { properties ->  FluidStorageCell(properties.maxStackSize(1).rarity(Rarity.RARE), FLUID_STORAGE_COMPONENT_1024k, 1024, 4.0, 4096)}
    val FLUID_STORAGE_CELL_4096k = createItem(Ids.FLUID_STORAGE_CELL_4096) { properties ->  FluidStorageCell(properties.maxStackSize(1).rarity(Rarity.EPIC), FLUID_STORAGE_COMPONENT_4096k, 4096, 5.0, 8192)}
    
    val CHEMICAL_STORAGE_CELL_1k = createItem(Ids.CHEMICAL_STORAGE_CELL_1, { properties ->  ChemicalStorageCell(properties.maxStackSize(1), CHEMICAL_STORAGE_COMPONENT_1k, 1, .5, 8)}, Mods.MEKANISM)
    val CHEMICAL_STORAGE_CELL_4k = createItem(Ids.CHEMICAL_STORAGE_CELL_4, { properties ->  ChemicalStorageCell(properties.maxStackSize(1), CHEMICAL_STORAGE_COMPONENT_4k, 4, 1.0, 32)}, Mods.MEKANISM)
    val CHEMICAL_STORAGE_CELL_16k = createItem(Ids.CHEMICAL_STORAGE_CELL_16, { properties ->  ChemicalStorageCell(properties.maxStackSize(1), CHEMICAL_STORAGE_COMPONENT_16k, 16, 1.5, 128)}, Mods.MEKANISM)
    val CHEMICAL_STORAGE_CELL_64k = createItem(Ids.CHEMICAL_STORAGE_CELL_64, { properties ->  ChemicalStorageCell(properties.maxStackSize(1), CHEMICAL_STORAGE_COMPONENT_64k, 64, 2.0, 512)}, Mods.MEKANISM)
    val CHEMICAL_STORAGE_CELL_256k = createItem(Ids.CHEMICAL_STORAGE_CELL_256, { properties ->  ChemicalStorageCell(properties.maxStackSize(1), CHEMICAL_STORAGE_COMPONENT_256k, 256, 3.0, 1024)}, Mods.MEKANISM)
    val CHEMICAL_STORAGE_CELL_1024k = createItem(Ids.CHEMICAL_STORAGE_CELL_1024, { properties ->  ChemicalStorageCell(properties.maxStackSize(1).rarity(Rarity.RARE), CHEMICAL_STORAGE_COMPONENT_1024k, 1024, 4.0, 4096)}, Mods.MEKANISM)
    val CHEMICAL_STORAGE_CELL_4096k = createItem(Ids.CHEMICAL_STORAGE_CELL_4096, { properties ->  ChemicalStorageCell(properties.maxStackSize(1).rarity(Rarity.EPIC), CHEMICAL_STORAGE_COMPONENT_4096k, 4096, 5.0, 8192)}, Mods.MEKANISM)
    
    val DUMMY_CHEMICAL_ITEM = createItem(Ids.DUMMY_CHEMICAL_ITEM) { properties -> ChemicalDummyItem(properties) }
    
    fun init() {
        
    }

    fun <T: Item> createItem(id: ResourceLocation, factory: (Item.Properties) -> T): T {
        val item = constructItem(factory, id)

        REGISTRY.registerObject(id.path) {
            item
        }

        return item
    }

    fun <T: Item> createItem(id: ResourceLocation, factory: (Item.Properties) -> T, requiredMod: Mods): T {
        val item = constructItem(factory, id, requiredMod)

        REGISTRY.registerObject(id.path) {
            item
        }

        return item
    }
    
    private fun <T: Item> constructItem(
        factory: (Item.Properties) -> T,
        id: ResourceLocation,
    ): T {
        val props = Item.Properties().group(CreativeTab.group)

        val item = factory(props)

        if (item.registryName != null) {
            item.registryName = id
        }

        ITEMS.add(item)

        return item
    }

    private fun <T : Item> constructItem(
        factory: (Item.Properties) -> T,
        id: ResourceLocation,
        requiredMod: Mods
    ): T {
        val props = Item.Properties()
        
        if (requiredMod.isEnabled) {
            props.group(CreativeTab.group)
        }

        val item = factory(props)

        if (item.registryName != null) {
            item.registryName = id
        }

        ITEMS.add(item)

        return item
    }

    fun <T: Item> createItem(id: ResourceLocation, factory: (Item.Properties) -> T, registry: KDeferredRegister<Item>): T {
        val item = constructItem(factory, id)
        
        registry.registerObject(id.path) {
            item
        }
        
        return item
    }

    fun <T: Item> createItem(id: ResourceLocation, factory: (Item.Properties) -> T, registry: KDeferredRegister<Item>, requiredMod: Mods): T {
        val item = constructItem(factory, id, requiredMod)

        registry.registerObject(id.path) {
            item
        }

        return item
    }
}