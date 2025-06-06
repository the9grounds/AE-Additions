package com.the9grounds.aeadditions.data.provider

import appeng.core.definitions.AEBlocks
import appeng.core.definitions.AEItems
import appeng.items.materials.StorageComponentItem
import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.integration.Mods
import com.the9grounds.aeadditions.item.storage.DiskCellWithoutMod
import com.the9grounds.aeadditions.item.storage.StorageCell
import com.the9grounds.aeadditions.registries.Blocks
import com.the9grounds.aeadditions.registries.Items
import io.github.projectet.ae2things.item.AETItems
import me.ramidzkh.mekae2.AMItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.*
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.conditions.IConditionBuilder
import java.util.concurrent.CompletableFuture

class AEAdditionsRecipeProvider(output: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) : RecipeProvider(output, registries), IConditionBuilder {
    val osmiumTag = ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "ingots/osmium"))
    val infusedAlloy = ItemTags.create(ResourceLocation.fromNamespaceAndPath("mekanism", "alloys/infused"))
    val reinforcedAlloy = ItemTags.create(ResourceLocation.fromNamespaceAndPath("mekanism", "alloys/reinforced"))
    val atomicAlloy = ItemTags.create(ResourceLocation.fromNamespaceAndPath("mekanism", "alloys/atomic"))
    val osmiumDust = ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "dusts/osmium"))

    val components = mapOf(
        1 to AEItems.CELL_COMPONENT_1K,
        4 to AEItems.CELL_COMPONENT_4K,
        16 to AEItems.CELL_COMPONENT_16K,
        64 to AEItems.CELL_COMPONENT_64K,
        256 to AEItems.CELL_COMPONENT_256K,
        1024 to Items.CELL_COMPONENT_1024k,
        4096 to Items.CELL_COMPONENT_4096k,
        16384 to Items.CELL_COMPONENT_16384k,
        65536 to Items.CELL_COMPONENT_65536k
    )
    
    val superMap = mapOf(
        "1k" to 1,
        "4k" to 4,
        "16k" to 16,
        "64k" to 64,
        "256k" to 256,
        "1024k" to 1024,
        "4096k" to 4096,
        "16M" to 16384,
        "65M" to 65536,
    )

    val superMapReversed = mapOf(
        1 to "1k",
        4 to "4k",
        16 to "16k",
        64 to "64k",
        256 to "256k",
        1024 to "1024k",
        4096 to "4096k",
        16384 to "16m",
        65536 to "65m",
    )
    
    override fun buildRecipes(consumer: RecipeOutput) {
        val itemKbs = mapOf<Int, StorageCell>(
            1024 to Items.ITEM_STORAGE_CELL_1024k,
            4096 to Items.ITEM_STORAGE_CELL_4096k, 
            16384 to Items.ITEM_STORAGE_CELL_16384k,
            65536 to Items.ITEM_STORAGE_CELL_65536k
        )
        val fluidKbs = mapOf<Int, StorageCell>(
            1024 to Items.FLUID_STORAGE_CELL_1024k,
            4096 to Items.FLUID_STORAGE_CELL_4096k,
            16384 to Items.FLUID_STORAGE_CELL_16384k
        )
        val chemicalKbs = mapOf<Int, StorageCell>(
            1024 to Items.CHEMICAL_STORAGE_CELL_1024k as StorageCell,
            4096 to Items.CHEMICAL_STORAGE_CELL_4096k as StorageCell,
            16384 to Items.CHEMICAL_STORAGE_CELL_16384k as StorageCell
        )
        val mapOfRequired = mapOf(
            4 to 1,
            16 to 4,
            64 to 16,
            256 to 64,
            1024 to 256,
            4096 to 1024,
            16384 to 4096,
            65536 to 16384
        )
        val craftingStorageBlocks = mapOf(
            1024 to Blocks.BLOCK_CRAFTING_STORAGE_1024k,
            4096 to Blocks.BLOCK_CRAFTING_STORAGE_4096k,
            16384 to Blocks.BLOCK_CRAFTING_STORAGE_16384k,
            65536 to Blocks.BLOCK_CRAFTING_STORAGE_65536k,
        )
        
        val superCellComponents = mapOf(
            1 to Items.SUPER_CELL_COMPONENT_1k,
            4 to Items.SUPER_CELL_COMPONENT_4k,
            16 to Items.SUPER_CELL_COMPONENT_16k,
            64 to Items.SUPER_CELL_COMPONENT_64k,
            256 to Items.SUPER_CELL_COMPONENT_256k,
            1024 to Items.SUPER_CELL_COMPONENT_1024k,
            4096 to Items.SUPER_CELL_COMPONENT_4096k,
            16384 to Items.SUPER_CELL_COMPONENT_16M,
            65536 to Items.SUPER_CELL_COMPONENT_65M,
        )

        val superStorageCells = mapOf(
            1 to Items.SUPER_CELL_1k,
            4 to Items.SUPER_CELL_4k,
            16 to Items.SUPER_CELL_16k,
            64 to Items.SUPER_CELL_64k,
            256 to Items.SUPER_CELL_256k,
            1024 to Items.SUPER_CELL_1024k,
            4096 to Items.SUPER_CELL_4096k,
            16384 to Items.SUPER_CELL_16M,
            65536 to Items.SUPER_CELL_65M,
        )

        val diskCells = mapOf(
//                256 to Items.DISK_256k,
                1024 to Items.DISK_1024k,
                4096 to Items.DISK_4096k,
                16384 to Items.DISK_16384k,
                65536 to Items.DISK_65536k
        )
        val fluidCells = mapOf(
                1 to Items.DISK_FLUID_1k,
                4 to Items.DISK_FLUID_4k,
                16 to Items.DISK_FLUID_16k,
                64 to Items.DISK_FLUID_64k,
                256 to Items.DISK_FLUID_256k,
                1024 to Items.DISK_FLUID_1024k,
                4096 to Items.DISK_FLUID_4096k,
                16384 to Items.DISK_FLUID_16384k,
                65536 to Items.DISK_FLUID_65536k
        )
        val chemicalCells = mapOf(
                1 to Items.DISK_CHEMICAL_1k,
                4 to Items.DISK_CHEMICAL_4k,
                16 to Items.DISK_CHEMICAL_16k,
                64 to Items.DISK_CHEMICAL_64k,
                256 to Items.DISK_CHEMICAL_256k,
                1024 to Items.DISK_CHEMICAL_1024k,
                4096 to Items.DISK_CHEMICAL_4096k,
                16384 to Items.DISK_CHEMICAL_16384k,
                65536 to Items.DISK_CHEMICAL_65536k
        )

        var previousComponent: StorageComponentItem? = null
        
        for (superCellComponent in superCellComponents) {
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, superCellComponent.value)
                .pattern("aba")
                .pattern("aaa")
                .pattern("aba")
                .define('a', components[superCellComponent.key])
                .define('b', Tags.Items.GEMS_DIAMOND)
                .unlockedBy("has_item", has(components[superCellComponent.key]))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(AEAdditions.ID, "components/super/${superMapReversed[superCellComponent.key]}"))
            if (previousComponent !== null) {
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, superCellComponent.value)
                    .pattern("aaa")
                    .pattern("bbb")
                    .pattern("aaa")
                    .define('a', Tags.Items.GEMS_DIAMOND)
                    .define('b', previousComponent)
                    .unlockedBy("has_previous_component", has(previousComponent))
                    .save(consumer, ResourceLocation.fromNamespaceAndPath(AEAdditions.ID, "components/super/${superMapReversed[superCellComponent.key]}_upgrade"))

            }

            previousComponent = superCellComponent.value
        }
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,Items.SUPER_CELL_HOUSING)
            .pattern("aba")
            .pattern("b b")
            .pattern("ccc")
            .define('a', AEBlocks.QUARTZ_GLASS)
            .define('b', Tags.Items.DUSTS_REDSTONE)
            .define('c', Tags.Items.GEMS_DIAMOND)
            .unlockedBy("has_item", has(Tags.Items.GEMS_DIAMOND))
            .save(consumer, ResourceLocation.fromNamespaceAndPath(AEAdditions.ID, "super_cell_housing"))
        
        for (superStorageCell in superStorageCells) {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,superStorageCell.value)
                .requires(Items.SUPER_CELL_HOUSING)
                .requires(superCellComponents[superStorageCell.key])
                .unlockedBy("has_item", has(superCellComponents[superStorageCell.key]))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(AEAdditions.ID, "cells/super/${superMapReversed[superStorageCell.key]}-housing"))

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC,superStorageCell.value)
                .pattern("aba")
                .pattern("bdb")
                .pattern("ccc")
                .define('a', AEBlocks.QUARTZ_GLASS)
                .define('b', Tags.Items.DUSTS_REDSTONE)
                .define('c', Tags.Items.GEMS_DIAMOND)
                .define('d', superCellComponents[superStorageCell.key])
                .unlockedBy("has_item", has(superCellComponents[superStorageCell.key]))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(AEAdditions.ID, "cells/super/${superMapReversed[superStorageCell.key]}"))
        }

        val ae2thingsConditionsConsumer = consumer.withConditions(and(modLoaded(Mods.AE2THINGS.modId)))
        
        for (fluidCell in fluidCells) {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, fluidCell.value)
                .requires(Items.DISK_FLUID_HOUSING)
                .requires(components[fluidCell.key])
                .unlockedBy("has_item", has(components[fluidCell.key]))
                .save(ae2thingsConditionsConsumer, ResourceLocation.fromNamespaceAndPath(AEAdditions.ID, "cells/fluid/disk-${fluidCell.key}-casing"))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, fluidCell.value)
                .pattern("aba")
                .pattern("bcb")
                .pattern("ded")
                .define('a', AEBlocks.QUARTZ_GLASS)
                .define('b', Tags.Items.DUSTS_REDSTONE)
                .define('c', components[fluidCell.key])
                .define('d', Tags.Items.INGOTS_NETHERITE)
                .define('e', Tags.Items.GEMS_LAPIS)
                .unlockedBy("has_item", has(components[fluidCell.key]))
                .save(ae2thingsConditionsConsumer, ResourceLocation.fromNamespaceAndPath(AEAdditions.ID, "cells/fluid/disk-${fluidCell.key}"))
        }

        for (chemicalCell in chemicalCells) {
            when(chemicalCell.key) {
                1, 4, 16 -> registerChemicalDiskRecipe(chemicalCell, infusedAlloy, components, consumer)
                64, 256, 1024 -> registerChemicalDiskRecipe(chemicalCell, reinforcedAlloy, components, consumer)
                4096, 16384, 65536 -> registerChemicalDiskRecipe(chemicalCell, atomicAlloy, components, consumer)
            }
        }

        val ae2thingsAndAppMekConditions = consumer.withConditions(
            and(
                modLoaded(Mods.AE2THINGS.modId),
                modLoaded(Mods.APPMEK.modId)
            )
        )

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.DISK_CHEMICAL_HOUSING)
            .pattern("aba")
            .pattern("b b")
            .pattern("ded")
            .define('a', AEBlocks.QUARTZ_GLASS)
            .define('b', Tags.Items.DUSTS_REDSTONE)
            .define('d', Tags.Items.INGOTS_NETHERITE)
            .define('e', osmiumDust)
            .unlockedBy("has_item", has(Tags.Items.INGOTS_NETHERITE))
            .save(ae2thingsAndAppMekConditions, ResourceLocation.fromNamespaceAndPath(AEAdditions.ID, "cells/chemical/disk-housing"))

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.DISK_FLUID_HOUSING)
            .pattern("aba")
            .pattern("b b")
            .pattern("ded")
            .define('a', AEBlocks.QUARTZ_GLASS)
            .define('b', Tags.Items.DUSTS_REDSTONE)
            .define('d', Tags.Items.INGOTS_NETHERITE)
            .define('e', Tags.Items.GEMS_LAPIS)
            .unlockedBy("has_item", has(Tags.Items.INGOTS_NETHERITE))
            .save(ae2thingsConditionsConsumer, ResourceLocation.fromNamespaceAndPath(AEAdditions.ID, "cells/fluid/disk-housing"))

        for (diskCell in diskCells) {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, diskCell.value)
                .requires(AETItems.DISK_HOUSING.get())
                .requires(components[diskCell.key])
                .unlockedBy("has_item", has(components[diskCell.key]))
                .save(ae2thingsConditionsConsumer, ResourceLocation.fromNamespaceAndPath(AEAdditions.ID, "cells/item/disk-${diskCell.key}-casing"))

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, diskCell.value)
                .pattern("aba")
                .pattern("bcb")
                .pattern("ded")
                .define('a', AEBlocks.QUARTZ_GLASS)
                .define('b', Tags.Items.DUSTS_REDSTONE)
                .define('c', components[diskCell.key])
                .define('d', Tags.Items.INGOTS_NETHERITE)
                .define('e', Tags.Items.GEMS_AMETHYST)
                .unlockedBy("has_item", has(components[diskCell.key]))
                .save(ae2thingsConditionsConsumer, ResourceLocation.fromNamespaceAndPath(AEAdditions.ID, "cells/item/disk-${diskCell.key}"))
        }
        
        for (block in craftingStorageBlocks) {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,block.value.item)
                .requires(AEBlocks.CRAFTING_UNIT)
                .requires(components[block.key])
                .unlockedBy("has_item", has(components[block.key]))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(AEAdditions.ID, "crafting/${block.key}k_storage"))
        }
        
        for (component in listOf(1024, 4096, 16384, 65536)) {
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC,components[component])
                .pattern("aba")
                .pattern("cdc")
                .pattern("aca")
                .define('a', Tags.Items.GEMS_DIAMOND)
                .define('b', AEItems.ENGINEERING_PROCESSOR)
                .define('c', components[mapOfRequired[component]])
                .define('d', AEBlocks.QUARTZ_GLASS)
                .unlockedBy("has_item", has(components[mapOfRequired[component]]))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(AEAdditions.ID, "components/${component}k"))
        }
        
        for (kb in itemKbs) {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,kb.value)
                .requires(AEItems.ITEM_CELL_HOUSING)
                .requires(kb.value.component)
                .unlockedBy("has_item", has(kb.value.component))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(AEAdditions.ID, "cells/item/casing-${kb.key}k"))

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC,kb.value)
                .pattern("aba")
                .pattern("bcb")
                .pattern("ddd")
                .define('a', AEBlocks.QUARTZ_GLASS)
                .define('b', AEItems.FLUIX_DUST)
                .define('c', kb.value.component)
                .define('d', Tags.Items.GEMS_DIAMOND)
                .unlockedBy("has_item", has(kb.value.component))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(AEAdditions.ID, "cells/item/${kb.key}k"))
        }
        for (kb in fluidKbs) {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,kb.value)
                .requires(AEItems.FLUID_CELL_HOUSING)
                .requires(kb.value.component)
                .unlockedBy("has_item", has(kb.value.component))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(AEAdditions.ID, "cells/fluid/casing-${kb.key}k"))

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC,kb.value)
                .pattern("aba")
                .pattern("bcb")
                .pattern("ddd")
                .define('a', AEBlocks.QUARTZ_GLASS)
                .define('b', AEItems.FLUIX_DUST)
                .define('c', kb.value.component)
                .define('d', Tags.Items.INGOTS_COPPER)
                .unlockedBy("has_item", has(kb.value.component))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(AEAdditions.ID, "cells/fluid/${kb.key}k"))
        }
        val appMekConsumer = consumer.withConditions(
            and(
                modLoaded(Mods.APPMEK.modId)
            )
        )

        for (kb in chemicalKbs) {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,kb.value)
                .requires(AMItems.CHEMICAL_CELL_HOUSING.get())
                .requires(components[kb.key])
                .unlockedBy("has_item", has(components[kb.key]))
                .save(appMekConsumer, ResourceLocation.fromNamespaceAndPath(AEAdditions.ID, "cells/chemical/casing-${kb.key}k"))
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC,kb.value)
                .pattern("aba")
                .pattern("bcb")
                .pattern("ddd")
                .define('a', AEBlocks.QUARTZ_GLASS)
                .define('b', AEItems.FLUIX_DUST)
                .define('c', components[kb.key])
                .define('d', osmiumTag)
                .unlockedBy("has_item", has(components[kb.key]))
                .save(appMekConsumer, ResourceLocation.fromNamespaceAndPath(AEAdditions.ID, "cells/chemical/${kb.key}k"))
        }
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,Blocks.BLOCK_ME_WIRELESS_TRANSCEIVER.block)
            .pattern("aba")
            .pattern("cdc")
            .pattern("aba")
            .define('a', AEItems.FLUIX_CRYSTAL)
            .define('b', AEItems.ENGINEERING_PROCESSOR)
            .define('c', AEItems.WIRELESS_RECEIVER)
            .define('d', Tags.Items.GEMS_DIAMOND)
            .unlockedBy("has_item", has(AEItems.WIRELESS_RECEIVER))
            .unlockedBy("has_diamond", has(Tags.Items.GEMS_DIAMOND))
            .save(consumer, ResourceLocation.fromNamespaceAndPath(AEAdditions.ID, "blocks/wireless_transceiver"))
    }

    private fun registerChemicalDiskRecipe(
        chemicalCell: Map.Entry<Int, DiskCellWithoutMod>,
        alloy: TagKey<Item>,
        components: Map<Int, ItemLike>,
        consumer: RecipeOutput
    ) {
        val newConsumer = consumer.withConditions(
            and(
                modLoaded(Mods.AE2THINGS.modId),
                modLoaded(Mods.APPMEK.modId)
            )
        )
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,chemicalCell.value)
            .requires(Items.DISK_CHEMICAL_HOUSING)
            .requires(components[chemicalCell.key])
            .requires(alloy)
            .unlockedBy("has_item", has(components[chemicalCell.key]))
            .save(newConsumer,ResourceLocation.fromNamespaceAndPath(AEAdditions.ID, "cells/chemical/disk-${chemicalCell.key}-casing"))
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,chemicalCell.value)
            .pattern("aba")
            .pattern("bcb")
            .pattern("ded")
            .define('a', AEBlocks.QUARTZ_GLASS)
            .define('b', Tags.Items.DUSTS_REDSTONE)
            .define('c', components[chemicalCell.key])
            .define('d', Tags.Items.INGOTS_NETHERITE)
            .define('e', alloy)
            .unlockedBy("has_item", has(components[chemicalCell.key]))
            .save(newConsumer, ResourceLocation.fromNamespaceAndPath(AEAdditions.ID, "cells/chemical/disk-${chemicalCell.key}"))
    }
}
