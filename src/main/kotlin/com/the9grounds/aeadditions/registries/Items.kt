package com.the9grounds.aeadditions.registries

import appeng.api.stacks.AEKeyType
import appeng.core.definitions.AEItems
import appeng.items.materials.StorageComponentItem
import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.core.CreativeTab
import com.the9grounds.aeadditions.integration.Mods
import com.the9grounds.aeadditions.item.storage.DiskCell
import com.the9grounds.aeadditions.item.storage.DiskCellWithoutMod
import com.the9grounds.aeadditions.item.storage.StorageCell
import me.ramidzkh.mekae2.AMItems
import me.ramidzkh.mekae2.ae2.MekanismKeyType
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.Rarity
import net.minecraft.world.level.ItemLike
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import thedarkcolour.kotlinforforge.forge.registerObject

object Items {
    val REGISTRY = KDeferredRegister.create(ForgeRegistries.ITEMS, AEAdditions.ID)
    
    val ITEMS = mutableListOf<Item>()
    
    val CELL_COMPONENT_1024k = createItem(Ids.CELL_COMPONENT_1024) { properties ->  StorageComponentItem(properties, 1024)}
    val CELL_COMPONENT_4096k = createItem(Ids.CELL_COMPONENT_4096) { properties ->  StorageComponentItem(properties.rarity(Rarity.RARE), 4096)}
    val CELL_COMPONENT_16384k = createItem(Ids.CELL_COMPONENT_16384) { properties ->  StorageComponentItem(properties.rarity(Rarity.EPIC), 16384) }
    val CELL_COMPONENT_65536k = createItem(Ids.CELL_COMPONENT_65536) { properties ->  StorageComponentItem(properties.rarity(Rarity.EPIC), 65536) }
    
    val ITEM_STORAGE_CELL_1024k = createItem(Ids.ITEM_STORAGE_CELL_1024) { properties ->  StorageCell(properties.stacksTo(1), CELL_COMPONENT_1024k, AEItems.ITEM_CELL_HOUSING, 4.0, 4096, 1024, 100, AEKeyType.items())}
    val ITEM_STORAGE_CELL_4096k = createItem(Ids.ITEM_STORAGE_CELL_4096) { properties ->  StorageCell(properties.stacksTo(1).rarity(Rarity.RARE), CELL_COMPONENT_4096k, AEItems.ITEM_CELL_HOUSING, 5.0, 8192, 8192, 150, AEKeyType.items())}
    val ITEM_STORAGE_CELL_16384k = createItem(Ids.ITEM_STORAGE_CELL_16384) { properties ->  StorageCell(properties.stacksTo(1).rarity(Rarity.EPIC), CELL_COMPONENT_16384k, AEItems.ITEM_CELL_HOUSING, 6.0, 32768, 16384, 200, AEKeyType.items())}
    val ITEM_STORAGE_CELL_65536k = createItem(Ids.ITEM_STORAGE_CELL_65536) { properties ->  StorageCell(properties.stacksTo(1).rarity(Rarity.EPIC), CELL_COMPONENT_65536k, AEItems.ITEM_CELL_HOUSING, 10.0, 131072, 65536, 300, AEKeyType.items())}
    
    val FLUID_STORAGE_CELL_1024k = createItem(Ids.FLUID_STORAGE_CELL_1024) { properties ->  StorageCell(properties.stacksTo(1).rarity(Rarity.RARE), CELL_COMPONENT_1024k, AEItems.FLUID_CELL_HOUSING, 4.0, 4096, 1024, 10, AEKeyType.fluids()) }
    val FLUID_STORAGE_CELL_4096k = createItem(Ids.FLUID_STORAGE_CELL_4096) { properties ->  StorageCell(properties.stacksTo(1).rarity(Rarity.EPIC), CELL_COMPONENT_4096k, AEItems.FLUID_CELL_HOUSING, 5.0, 8192, 4096, 15, AEKeyType.fluids()) }
    val FLUID_STORAGE_CELL_16384k = createItem(Ids.FLUID_STORAGE_CELL_16384) { properties ->  StorageCell(properties.stacksTo(1).rarity(Rarity.EPIC), CELL_COMPONENT_16384k, AEItems.FLUID_CELL_HOUSING, 6.0, 32768, 16384, 20, AEKeyType.fluids()) }
    
    val DISK_256k = createItem(Ids.DISK_256k, { loadDiskCell(it.stacksTo(1), AEItems.CELL_COMPONENT_256K, 256, 3.0) }, Mods.AE2THINGS)
    val DISK_1024k = createItem(Ids.DISK_1024k, { loadDiskCell(it.stacksTo(1), CELL_COMPONENT_1024k, 1024, 5.0) }, Mods.AE2THINGS)
    val DISK_4096k = createItem(Ids.DISK_4096k, { loadDiskCell(it.stacksTo(1).rarity(Rarity.UNCOMMON), CELL_COMPONENT_4096k, 4096, 8.0) }, Mods.AE2THINGS)
    val DISK_16384k = createItem(Ids.DISK_16384k, { loadDiskCell(it.stacksTo(1).rarity(Rarity.RARE), CELL_COMPONENT_16384k, 16384, 10.0) }, Mods.AE2THINGS)
    val DISK_65536k = createItem(Ids.DISK_65536k, { loadDiskCell(it.stacksTo(1).rarity(Rarity.EPIC), CELL_COMPONENT_65536k, 65536, 15.0) }, Mods.AE2THINGS)
    
    
    val CHEMICAL_STORAGE_CELL_1024k by REGISTRY.registerObject(Ids.CHEMICAL_STORAGE_CELL_1024.path) {
        constructItem({ properties ->  
            if (Mods.APPMEK.isEnabled) {
                return@constructItem StorageCell(properties.stacksTo(1).rarity(Rarity.RARE), CELL_COMPONENT_1024k, AMItems.CHEMICAL_CELL_HOUSING.get(), 4.0, 4096, 1024, 8, MekanismKeyType.TYPE)
            }
            Item(properties)
        }, Ids.CHEMICAL_STORAGE_CELL_1024, Mods.APPMEK)
    } 
    val CHEMICAL_STORAGE_CELL_4096k by REGISTRY.registerObject(Ids.CHEMICAL_STORAGE_CELL_4096.path) { 
        constructItem({ properties ->  
            if (Mods.APPMEK.isEnabled) {
                return@constructItem StorageCell(properties.stacksTo(1).rarity(Rarity.EPIC), CELL_COMPONENT_4096k, AMItems.CHEMICAL_CELL_HOUSING.get(), 5.0, 8192, 4096, 12, MekanismKeyType.TYPE)
            }
            
            Item(properties)
        }, Ids.CHEMICAL_STORAGE_CELL_4096, Mods.APPMEK) 
    }
    
    val CHEMICAL_STORAGE_CELL_16384k by REGISTRY.registerObject(Ids.CHEMICAL_STORAGE_CELL_16384.path) { 
        constructItem({ properties -> 
            if (Mods.APPMEK.isEnabled) {
                return@constructItem StorageCell(properties.stacksTo(1).rarity(Rarity.EPIC), CELL_COMPONENT_16384k, AMItems.CHEMICAL_CELL_HOUSING.get(), 6.0, 32768, 16384, 15, MekanismKeyType.TYPE)
            }
            
            Item(properties)
        }, Ids.CHEMICAL_STORAGE_CELL_16384, Mods.APPMEK)
    }
    
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
        val props = Item.Properties().tab(CreativeTab.group)

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
            props.tab(CreativeTab.group)
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
    
    fun loadDiskCell(props: Item.Properties, component: ItemLike, kiloBytes: Int, idleDrain: Double): DiskCellWithoutMod {
        return if (Mods.AE2THINGS.isEnabled) {
            DiskCell(props, component, kiloBytes, idleDrain)
        } else {
            DiskCellWithoutMod(props)
        }
    }
}