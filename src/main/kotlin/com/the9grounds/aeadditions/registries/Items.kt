package com.the9grounds.aeadditions.registries

import appeng.api.stacks.AEKeyType
import appeng.core.definitions.AEItems
import appeng.items.materials.StorageComponentItem
import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.integration.Mods
import com.the9grounds.aeadditions.item.storage.DiskCell
import com.the9grounds.aeadditions.item.storage.DiskCellWithoutMod
import com.the9grounds.aeadditions.item.storage.StorageCell
import com.the9grounds.aeadditions.item.storage.SuperStorageCell
import me.ramidzkh.mekae2.AMItems
import me.ramidzkh.mekae2.ae2.MekanismKeyType
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.Rarity
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object Items {
    val REGISTRY = DeferredRegister.createItems(AEAdditions.ID)
    
    val ITEMS = mutableListOf<Item>()
    val CREATIVE_TAB_ITEMS = mutableListOf<Item>()
    
    val CELL_COMPONENT_1024k by createItem(Ids.CELL_COMPONENT_1024) { properties ->  StorageComponentItem(properties, 1024)}
    val CELL_COMPONENT_4096k by createItem(Ids.CELL_COMPONENT_4096) { properties ->  StorageComponentItem(properties.rarity(Rarity.RARE), 4096)}
    val CELL_COMPONENT_16384k by createItem(Ids.CELL_COMPONENT_16384) { properties ->  StorageComponentItem(properties.rarity(Rarity.EPIC), 16384) }
    val CELL_COMPONENT_65536k by createItem(Ids.CELL_COMPONENT_65536) { properties ->  StorageComponentItem(properties.rarity(Rarity.EPIC), 65536) }
    
    val SUPER_CELL_COMPONENT_1k by createItem(Ids.SUPER_CELL_COMPONENT_1k) { properties -> StorageComponentItem(properties, 1) }
    val SUPER_CELL_COMPONENT_4k by createItem(Ids.SUPER_CELL_COMPONENT_4k) { properties -> StorageComponentItem(properties, 4) }
    val SUPER_CELL_COMPONENT_16k by createItem(Ids.SUPER_CELL_COMPONENT_16k) { properties -> StorageComponentItem(properties, 16) }
    val SUPER_CELL_COMPONENT_64k by createItem(Ids.SUPER_CELL_COMPONENT_64k) { properties -> StorageComponentItem(properties, 64) }
    val SUPER_CELL_COMPONENT_256k by createItem(Ids.SUPER_CELL_COMPONENT_256k) { properties -> StorageComponentItem(properties, 256) }
    val SUPER_CELL_COMPONENT_1024k by createItem(Ids.SUPER_CELL_COMPONENT_1024k) { properties -> StorageComponentItem(properties, 1024) }
    val SUPER_CELL_COMPONENT_4096k by createItem(Ids.SUPER_CELL_COMPONENT_4096k) { properties -> StorageComponentItem(properties, 4096) }
    val SUPER_CELL_COMPONENT_16M by createItem(Ids.SUPER_CELL_COMPONENT_16M) { properties -> StorageComponentItem(properties, 16384) }
    val SUPER_CELL_COMPONENT_65M by createItem(Ids.SUPER_CELL_COMPONENT_65M) { properties -> StorageComponentItem(properties, 65536) }
    
    val ITEM_STORAGE_CELL_1024k by createItem(Ids.ITEM_STORAGE_CELL_1024) { properties ->  StorageCell(properties.stacksTo(1), CELL_COMPONENT_1024k, AEItems.ITEM_CELL_HOUSING, 4.0, 4096, 1024, 100, AEKeyType.items())}
    val ITEM_STORAGE_CELL_4096k by createItem(Ids.ITEM_STORAGE_CELL_4096) { properties ->  StorageCell(properties.stacksTo(1).rarity(Rarity.RARE), CELL_COMPONENT_4096k, AEItems.ITEM_CELL_HOUSING, 5.0, 8192, 4096, 150, AEKeyType.items())}
    val ITEM_STORAGE_CELL_16384k by createItem(Ids.ITEM_STORAGE_CELL_16384) { properties ->  StorageCell(properties.stacksTo(1).rarity(Rarity.EPIC), CELL_COMPONENT_16384k, AEItems.ITEM_CELL_HOUSING, 6.0, 32768, 16384, 200, AEKeyType.items())}
    val ITEM_STORAGE_CELL_65536k by createItem(Ids.ITEM_STORAGE_CELL_65536) { properties ->  StorageCell(properties.stacksTo(1).rarity(Rarity.EPIC), CELL_COMPONENT_65536k, AEItems.ITEM_CELL_HOUSING, 10.0, 131072, 65536, 300, AEKeyType.items())}
    
    val FLUID_STORAGE_CELL_1024k by createItem(Ids.FLUID_STORAGE_CELL_1024) { properties ->  StorageCell(properties.stacksTo(1).rarity(Rarity.RARE), CELL_COMPONENT_1024k, AEItems.FLUID_CELL_HOUSING, 4.0, 4096, 1024, 10, AEKeyType.fluids()) }
    val FLUID_STORAGE_CELL_4096k by createItem(Ids.FLUID_STORAGE_CELL_4096) { properties ->  StorageCell(properties.stacksTo(1).rarity(Rarity.EPIC), CELL_COMPONENT_4096k, AEItems.FLUID_CELL_HOUSING, 5.0, 8192, 4096, 15, AEKeyType.fluids()) }
    val FLUID_STORAGE_CELL_16384k by createItem(Ids.FLUID_STORAGE_CELL_16384) { properties ->  StorageCell(properties.stacksTo(1).rarity(Rarity.EPIC), CELL_COMPONENT_16384k, AEItems.FLUID_CELL_HOUSING, 6.0, 32768, 16384, 20, AEKeyType.fluids()) }
    
    val SUPER_CELL_HOUSING by createItem(Ids.SUPER_CELL_HOUSING) { properties -> Item(properties.stacksTo(64)) }
    
    val SUPER_CELL_1k by createItem(Ids.SUPER_CELL_1k) { properties -> SuperStorageCell(properties.stacksTo(1), SUPER_CELL_COMPONENT_1k, SUPER_CELL_HOUSING, 3.0, 16, 1, 63) }
    val SUPER_CELL_4k by createItem(Ids.SUPER_CELL_4k) { properties -> SuperStorageCell(properties.stacksTo(1), SUPER_CELL_COMPONENT_4k, SUPER_CELL_HOUSING, 3.0, 32, 4, 63) }
    val SUPER_CELL_16k by createItem(Ids.SUPER_CELL_16k) { properties -> SuperStorageCell(properties.stacksTo(1), SUPER_CELL_COMPONENT_16k, SUPER_CELL_HOUSING, 3.0, 64, 16, 63) }
    val SUPER_CELL_64k by createItem(Ids.SUPER_CELL_64k) { properties -> SuperStorageCell(properties.stacksTo(1), SUPER_CELL_COMPONENT_64k, SUPER_CELL_HOUSING, 3.0, 256, 64, 63) }
    val SUPER_CELL_256k by createItem(Ids.SUPER_CELL_256k) { properties -> SuperStorageCell(properties.stacksTo(1), SUPER_CELL_COMPONENT_256k, SUPER_CELL_HOUSING, 3.0, 1024, 256, 100) }
    val SUPER_CELL_1024k by createItem(Ids.SUPER_CELL_1024k) { properties -> SuperStorageCell(properties.stacksTo(1), SUPER_CELL_COMPONENT_1024k, SUPER_CELL_HOUSING, 3.0, 4096, 1024, 150) }
    val SUPER_CELL_4096k by createItem(Ids.SUPER_CELL_4096k) { properties -> SuperStorageCell(properties.stacksTo(1).rarity(Rarity.UNCOMMON), SUPER_CELL_COMPONENT_4096k, SUPER_CELL_HOUSING, 3.0, 8192, 4096, 200) }
    val SUPER_CELL_16M by createItem(Ids.SUPER_CELL_16M) { properties -> SuperStorageCell(properties.stacksTo(1).rarity(Rarity.RARE), SUPER_CELL_COMPONENT_16M, SUPER_CELL_HOUSING, 3.0, 32768, 16384, 250) }
    val SUPER_CELL_65M by createItem(Ids.SUPER_CELL_65M) { properties -> SuperStorageCell(properties.stacksTo(1).rarity(Rarity.EPIC), SUPER_CELL_COMPONENT_65M, SUPER_CELL_HOUSING, 30.0, 131072, 65536, 300) }
    
    val DISK_FLUID_HOUSING by createItem(Ids.DISK_FLUID_HOUSING, { Item(it.stacksTo(64))}, false, Mods.AE2THINGS)
    val DISK_CHEMICAL_HOUSING by createItem(Ids.DISK_CHEMICAL_HOUSING, { Item(it.stacksTo(64))}, false, Mods.AE2THINGS, Mods.APPMEK)

    val DISK_1024k by createItemForMod(Ids.DISK_1024k, { loadDiskCell(it.stacksTo(1), AEKeyType.items(), CELL_COMPONENT_1024k, getAEThingsHousing(), 1024, 5.0) }, false, Mods.AE2THINGS)
    val DISK_4096k by createItemForMod(Ids.DISK_4096k, { loadDiskCell(it.stacksTo(1).rarity(Rarity.UNCOMMON), AEKeyType.items(), CELL_COMPONENT_4096k, getAEThingsHousing(), 4096, 8.0) }, false, Mods.AE2THINGS)
    val DISK_16384k by createItemForMod(Ids.DISK_16384k, { loadDiskCell(it.stacksTo(1).rarity(Rarity.RARE), AEKeyType.items(), CELL_COMPONENT_16384k, getAEThingsHousing(), 16384, 10.0) }, false, Mods.AE2THINGS)
    val DISK_65536k by createItemForMod(Ids.DISK_65536k, { loadDiskCell(it.stacksTo(1).rarity(Rarity.EPIC), AEKeyType.items(), CELL_COMPONENT_65536k, getAEThingsHousing(), 65536, 15.0) }, false, Mods.AE2THINGS)

    val DISK_FLUID_1k by createItem(Ids.DISK_FLUID_1k, { loadDiskCell(it.stacksTo(1), AEKeyType.fluids(), AEItems.CELL_COMPONENT_1K, DISK_FLUID_HOUSING, 1, .5)}, false, Mods.AE2THINGS)
    val DISK_FLUID_4k by createItem(Ids.DISK_FLUID_4k, { loadDiskCell(it.stacksTo(1), AEKeyType.fluids(), AEItems.CELL_COMPONENT_4K, DISK_FLUID_HOUSING, 4, 1.0)}, false, Mods.AE2THINGS)
    val DISK_FLUID_16k by createItem(Ids.DISK_FLUID_16k, { loadDiskCell(it.stacksTo(1), AEKeyType.fluids(), AEItems.CELL_COMPONENT_16K, DISK_FLUID_HOUSING, 16, 1.5)}, false, Mods.AE2THINGS)
    val DISK_FLUID_64k by createItem(Ids.DISK_FLUID_64k, { loadDiskCell(it.stacksTo(1), AEKeyType.fluids(), AEItems.CELL_COMPONENT_64K, DISK_FLUID_HOUSING, 64, 2.0)}, false, Mods.AE2THINGS)
    val DISK_FLUID_256k by createItem(Ids.DISK_FLUID_256k, { loadDiskCell(it.stacksTo(1), AEKeyType.fluids(), AEItems.CELL_COMPONENT_256K, DISK_FLUID_HOUSING, 256, 4.0)}, false, Mods.AE2THINGS)
    val DISK_FLUID_1024k by createItem(Ids.DISK_FLUID_1024k, { loadDiskCell(it.stacksTo(1), AEKeyType.fluids(), CELL_COMPONENT_1024k, DISK_FLUID_HOUSING, 1024, 6.0)}, false, Mods.AE2THINGS)
    val DISK_FLUID_4096k by createItem(Ids.DISK_FLUID_4096k, { loadDiskCell(it.stacksTo(1), AEKeyType.fluids(), CELL_COMPONENT_4096k, DISK_FLUID_HOUSING, 4096, 8.0)}, false, Mods.AE2THINGS)
    val DISK_FLUID_16384k by createItem(Ids.DISK_FLUID_16384k, { loadDiskCell(it.stacksTo(1), AEKeyType.fluids(), CELL_COMPONENT_16384k, DISK_FLUID_HOUSING, 16384, 10.0)}, false, Mods.AE2THINGS)
    val DISK_FLUID_65536k by createItem(Ids.DISK_FLUID_65536k, { loadDiskCell(it.stacksTo(1), AEKeyType.fluids(), CELL_COMPONENT_65536k, DISK_FLUID_HOUSING, 65536, 15.0)}, false, Mods.AE2THINGS)

    val DISK_CHEMICAL_1k by createItem(Ids.DISK_CHEMICAL_1k, { loadDiskCell(it.stacksTo(1), getMekType(), AEItems.CELL_COMPONENT_1K, DISK_CHEMICAL_HOUSING, 1, .5)}, false, Mods.AE2THINGS, Mods.APPMEK)
    val DISK_CHEMICAL_4k by createItem(Ids.DISK_CHEMICAL_4k, { loadDiskCell(it.stacksTo(1), getMekType(), AEItems.CELL_COMPONENT_4K, DISK_CHEMICAL_HOUSING, 4, 1.0)}, false, Mods.AE2THINGS, Mods.APPMEK)
    val DISK_CHEMICAL_16k by createItem(Ids.DISK_CHEMICAL_16k, { loadDiskCell(it.stacksTo(1), getMekType(), AEItems.CELL_COMPONENT_16K, DISK_CHEMICAL_HOUSING, 16, 1.5)}, false, Mods.AE2THINGS, Mods.APPMEK)
    val DISK_CHEMICAL_64k by createItem(Ids.DISK_CHEMICAL_64k, { loadDiskCell(it.stacksTo(1), getMekType(), AEItems.CELL_COMPONENT_64K, DISK_CHEMICAL_HOUSING, 64, 2.0)}, false, Mods.AE2THINGS, Mods.APPMEK)
    val DISK_CHEMICAL_256k by createItem(Ids.DISK_CHEMICAL_256k, { loadDiskCell(it.stacksTo(1), getMekType(), AEItems.CELL_COMPONENT_256K, DISK_CHEMICAL_HOUSING, 256, 4.0)}, false, Mods.AE2THINGS, Mods.APPMEK)
    val DISK_CHEMICAL_1024k by createItem(Ids.DISK_CHEMICAL_1024k, { loadDiskCell(it.stacksTo(1), getMekType(), CELL_COMPONENT_1024k, DISK_CHEMICAL_HOUSING, 1024, 6.0)}, false, Mods.AE2THINGS, Mods.APPMEK)
    val DISK_CHEMICAL_4096k by createItem(Ids.DISK_CHEMICAL_4096k, { loadDiskCell(it.stacksTo(1), getMekType(), CELL_COMPONENT_4096k, DISK_CHEMICAL_HOUSING, 4096, 8.0)}, false, Mods.AE2THINGS, Mods.APPMEK)
    val DISK_CHEMICAL_16384k by createItem(Ids.DISK_CHEMICAL_16384k, { loadDiskCell(it.stacksTo(1), getMekType(), CELL_COMPONENT_16384k, DISK_CHEMICAL_HOUSING, 16384, 10.0)}, false, Mods.AE2THINGS, Mods.APPMEK)
    val DISK_CHEMICAL_65536k by createItem(Ids.DISK_CHEMICAL_65536k, { loadDiskCell(it.stacksTo(1), getMekType(), CELL_COMPONENT_65536k, DISK_CHEMICAL_HOUSING, 65536, 15.0)}, false, Mods.AE2THINGS, Mods.APPMEK)
    
    val CHEMICAL_STORAGE_CELL_1024k by REGISTRY.register(Ids.CHEMICAL_STORAGE_CELL_1024.path) { ->
        constructItem({ properties ->
            if (Mods.APPMEK.isEnabled) {
                return@constructItem StorageCell(properties.stacksTo(1).rarity(Rarity.RARE), CELL_COMPONENT_1024k, AMItems.CHEMICAL_CELL_HOUSING.get(), 4.0, 4096, 1024, 8, getMekType())
            }
            Item(properties)
        }, Ids.CHEMICAL_STORAGE_CELL_1024, false, Mods.APPMEK)
    }
    val CHEMICAL_STORAGE_CELL_4096k by REGISTRY.register(Ids.CHEMICAL_STORAGE_CELL_4096.path) { ->
        constructItem({ properties ->
            if (Mods.APPMEK.isEnabled) {
                return@constructItem StorageCell(properties.stacksTo(1).rarity(Rarity.EPIC), CELL_COMPONENT_4096k, AMItems.CHEMICAL_CELL_HOUSING.get(), 5.0, 8192, 4096, 12, getMekType())
            }

            Item(properties)
        }, Ids.CHEMICAL_STORAGE_CELL_4096, false, Mods.APPMEK)
    }

    val CHEMICAL_STORAGE_CELL_16384k by REGISTRY.register(Ids.CHEMICAL_STORAGE_CELL_16384.path) { ->
        constructItem({ properties ->
            if (Mods.APPMEK.isEnabled) {
                return@constructItem StorageCell(properties.stacksTo(1).rarity(Rarity.EPIC), CELL_COMPONENT_16384k, AMItems.CHEMICAL_CELL_HOUSING.get(), 6.0, 32768, 16384, 15, getMekType())
            }

            Item(properties)
        }, Ids.CHEMICAL_STORAGE_CELL_16384, false, Mods.APPMEK)
    }
    
    fun init() {
        
    }

    fun <T: Item> createItem(id: ResourceLocation, factory: (Item.Properties) -> T): DeferredItem<T> {
        return REGISTRY.register(id.path) { ->
            val item = constructItem(factory, id)
            item
        }
    }

    fun <T: Item> createItem(id: ResourceLocation, factory: (Item.Properties) -> T, deprecated: Boolean = false, vararg requiredMod: Mods): DeferredItem<T> {
        return REGISTRY.register(id.path) { ->
            val item = constructItem(factory, id, deprecated, *requiredMod)
            item
        }
    }

    fun <T: Item> createItemForMod(id: ResourceLocation, factory: (Item.Properties) -> T,deprecated: Boolean = false, vararg requiredMod: Mods): DeferredItem<T> {
        return REGISTRY.register(id.path) { ->
            val item = constructItem(factory, id, deprecated, *requiredMod)
            item
        }
    }
    
    private fun <T: Item> constructItem(
        factory: (Item.Properties) -> T,
        id: ResourceLocation,
        deprecated: Boolean = false
    ): T {
        val props = Item.Properties()

        val item = factory(props)

        if (!deprecated) {
            CREATIVE_TAB_ITEMS.add(item)
        }

        ITEMS.add(item)

        return item
    }

    private fun <T : Item> constructItem(
        factory: (Item.Properties) -> T,
        id: ResourceLocation,
        deprecated: Boolean = false,
        vararg requiredMod: Mods
    ): T {
        val props = Item.Properties()
        
        var shouldShow = true
        
        requiredMod.forEach { 
            if (!it.isEnabled) {
                shouldShow = false
            }
        }

        val item = factory(props)

        if (shouldShow && !deprecated) {
            CREATIVE_TAB_ITEMS.add(item)
        }

        ITEMS.add(item)

        return item
    }

    fun <T: Item> createItem(id: ResourceLocation, factory: (Item.Properties) -> T, registry: DeferredRegister.Items): DeferredItem<T> {
        return registry.register(id.path) { ->
            constructItem(factory, id)
        }
    }

    fun <T: Item> createItem(id: ResourceLocation, factory: (Item.Properties) -> T, registry: DeferredRegister.Items, deprecated: Boolean = false, vararg requiredMod: Mods): DeferredItem<T> {
        return registry.register(id.path) { ->
            constructItem(factory, id, deprecated, *requiredMod)
        }
    }
    
    fun loadDiskCell(props: Item.Properties, keyType: AEKeyType, component: ItemLike, housing: ItemLike?, kiloBytes: Int, idleDrain: Double): DiskCellWithoutMod {
        return if (Mods.AE2THINGS.isEnabled) {
            DiskCell(props, keyType, component, housing, kiloBytes, idleDrain)
        } else {
            DiskCellWithoutMod(props)
        }
    }

    private fun getAEThingsHousing(): Item? {
        return if (Mods.AE2THINGS.isEnabled) {
            null
        } else {
            DISK_FLUID_HOUSING
        }
    }

    private fun getMekType(): AEKeyType {
        return if (Mods.APPMEK.isEnabled) {
            MekanismKeyType.TYPE
        } else {
            AEKeyType.fluids()
        }
    }
}