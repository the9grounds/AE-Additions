package com.the9grounds.aeadditions.registries

import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.core.CreativeTab
import com.the9grounds.aeadditions.items.ChemicalDummyItem
import com.the9grounds.aeadditions.items.storage.ChemicalStorageCell
import com.the9grounds.aeadditions.items.storage.FluidStorageCell
import com.the9grounds.aeadditions.items.storage.PhysicalStorageCell
import com.the9grounds.aeadditions.items.storage.StorageComponentItem
import net.minecraft.item.Item
import net.minecraft.item.Rarity
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.KDeferredRegister

object Items {
    val REGISTRY = KDeferredRegister(ForgeRegistries.ITEMS, AEAdditions.ID)

    val ITEM_STORAGE_COMPONENT_256k = createItem(Ids.ITEM_STORAGE_COMPONENT_256) { properties ->  StorageComponentItem(properties, 256)}
    val ITEM_STORAGE_COMPONENT_1024k = createItem(Ids.ITEM_STORAGE_COMPONENT_1024) { properties ->  StorageComponentItem(properties, 1024)}
    val ITEM_STORAGE_COMPONENT_4096k = createItem(Ids.ITEM_STORAGE_COMPONENT_4096) { properties ->  StorageComponentItem(properties.rarity(Rarity.RARE), 4096)}
    val ITEM_STORAGE_COMPONENT_16384k = createItem(Ids.ITEM_STORAGE_COMPONENT_16384) { properties ->  StorageComponentItem(properties.rarity(Rarity.EPIC), 16384)}

    val FLUID_STORAGE_COMPONENT_256k = createItem(Ids.FLUID_STORAGE_COMPONENT_256) { properties -> StorageComponentItem(properties, 256) }
    val FLUID_STORAGE_COMPONENT_1024k = createItem(Ids.FLUID_STORAGE_COMPONENT_1024) { properties -> StorageComponentItem(properties.rarity(Rarity.RARE), 1024) }
    val FLUID_STORAGE_COMPONENT_4096k = createItem(Ids.FLUID_STORAGE_COMPONENT_4096) { properties -> StorageComponentItem(properties.rarity(Rarity.EPIC), 4096) }
    
    val CHEMICAL_STORAGE_COMPONENT_64k = createItem(Ids.CHEMICAL_STORAGE_COMPONENT_64) { properties -> StorageComponentItem(properties, 64) }

    val ITEM_STORAGE_CELL_256k = createItem(Ids.ITEM_STORAGE_CELL_256) { properties ->  PhysicalStorageCell(properties.maxStackSize(1), ITEM_STORAGE_COMPONENT_256k, 256, 3.0, 1024)}
    val ITEM_STORAGE_CELL_1024k = createItem(Ids.ITEM_STORAGE_CELL_1024) { properties ->  PhysicalStorageCell(properties.maxStackSize(1), ITEM_STORAGE_COMPONENT_1024k, 256, 4.0, 4096)}
    val ITEM_STORAGE_CELL_4096k = createItem(Ids.ITEM_STORAGE_CELL_4096) { properties ->  PhysicalStorageCell(properties.maxStackSize(1).rarity(Rarity.RARE), ITEM_STORAGE_COMPONENT_4096k, 256, 5.0, 8192)}
    val ITEM_STORAGE_CELL_16384k = createItem(Ids.ITEM_STORAGE_CELL_16384) { properties ->  PhysicalStorageCell(properties.maxStackSize(1).rarity(Rarity.EPIC), ITEM_STORAGE_COMPONENT_16384k, 256, 6.0, 32768)}

    val FLUID_STORAGE_CELL_256k = createItem(Ids.FLUID_STORAGE_CELL_256) { properties ->  FluidStorageCell(properties.maxStackSize(1), FLUID_STORAGE_COMPONENT_256k, 256, 3.0, 1024)}
    val FLUID_STORAGE_CELL_1024k = createItem(Ids.FLUID_STORAGE_CELL_1024) { properties ->  FluidStorageCell(properties.maxStackSize(1).rarity(Rarity.RARE), FLUID_STORAGE_COMPONENT_1024k, 1024, 4.0, 4096)}
    val FLUID_STORAGE_CELL_4096k = createItem(Ids.FLUID_STORAGE_CELL_4096) { properties ->  FluidStorageCell(properties.maxStackSize(1).rarity(Rarity.EPIC), FLUID_STORAGE_COMPONENT_4096k, 4096, 5.0, 8192)}
    
    val CHEMICAL_STORAGE_CELL_64k = createItem(Ids.CHEMICAL_STORAGE_CELL_64) { properties ->  ChemicalStorageCell(properties.maxStackSize(1), CHEMICAL_STORAGE_COMPONENT_64k, 64, .5, 512)}
    
    val DUMMY_CHEMICAL_ITEM = createItem(Ids.DUMMY_CHEMICAL_ITEM) { properties -> ChemicalDummyItem(properties) }

    private fun <T: Item> createItem(id: ResourceLocation, factory: (Item.Properties) -> T): Item {
        val props = Item.Properties().group(CreativeTab.group)

        val item = factory(props)

        if (item.registryName != null) {
            item.registryName = id
        }

        REGISTRY.registerObject(id.path) {
            item
        }

        return item
    }
}