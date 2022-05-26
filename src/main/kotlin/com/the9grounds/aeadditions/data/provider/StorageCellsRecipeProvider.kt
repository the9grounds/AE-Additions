package com.the9grounds.aeadditions.data.provider

import appeng.core.AppEng
import appeng.core.definitions.AEBlocks
import appeng.core.definitions.AEItems
import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.item.storage.StorageCell
import com.the9grounds.aeadditions.registries.Items
import me.ramidzkh.mekae2.AMItems
import net.minecraft.data.DataGenerator
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraftforge.common.Tags
import net.minecraftforge.common.crafting.ConditionalRecipe
import net.minecraftforge.common.crafting.conditions.IConditionBuilder
import java.util.function.Consumer

class StorageCellsRecipeProvider(generatorIn: DataGenerator) : RecipeProvider(generatorIn), IConditionBuilder {
    
    val certusCrystalTag = ItemTags.create(ResourceLocation(AppEng.MOD_ID, "crystals/certus"))
    val panelTag = ItemTags.create(ResourceLocation(AppEng.MOD_ID, "illuminated_panel"))
    val osmiumTag = ItemTags.create(ResourceLocation("forge", "ingots/osmium"))
    
    override fun buildCraftingRecipes(consumer: Consumer<FinishedRecipe>) {
        val components = mapOf(
            256 to AEItems.CELL_COMPONENT_256K,
            1024 to Items.CELL_COMPONENT_1024k,
            4096 to Items.CELL_COMPONENT_4096k,
            16384 to Items.CELL_COMPONENT_16384k,
            65536 to Items.CELL_COMPONENT_65536k
        )
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
        
        for (component in listOf(1024, 4096, 16384, 65536)) {
            ShapedRecipeBuilder.shaped(components[component])
                .pattern("aba")
                .pattern("cdc")
                .pattern("aca")
                .define('a', Tags.Items.GEMS_DIAMOND)
                .define('b', AEItems.ENGINEERING_PROCESSOR)
                .define('c', components[mapOfRequired[component]])
                .define('d', AEBlocks.QUARTZ_GLASS)
                .unlockedBy("has_item", has(components[mapOfRequired[component]]))
                .save(consumer, ResourceLocation(AEAdditions.ID, "components/${component}k"))
        }
        
        for (kb in itemKbs) {
            ShapelessRecipeBuilder.shapeless(kb.value)
                .requires(AEItems.ITEM_CELL_HOUSING)
                .requires(kb.value.component)
                .unlockedBy("has_item", has(kb.value.component))
                .save(consumer, ResourceLocation(AEAdditions.ID, "cells/item/casing-${kb.key}k"))

            ShapedRecipeBuilder.shaped(kb.value)
                .pattern("aba")
                .pattern("bcb")
                .pattern("ddd")
                .define('a', AEBlocks.QUARTZ_GLASS)
                .define('b', AEItems.FLUIX_DUST)
                .define('c', kb.value.component)
                .define('d', Tags.Items.GEMS_DIAMOND)
                .unlockedBy("has_item", has(kb.value.component))
                .save(consumer, ResourceLocation(AEAdditions.ID, "cells/item/${kb.key}k"))
        }
        for (kb in fluidKbs) {
            ShapelessRecipeBuilder.shapeless(kb.value)
                .requires(AEItems.FLUID_CELL_HOUSING)
                .requires(kb.value.component)
                .unlockedBy("has_item", has(kb.value.component))
                .save(consumer, ResourceLocation(AEAdditions.ID, "cells/fluid/casing-${kb.key}k"))

            ShapedRecipeBuilder.shaped(kb.value)
                .pattern("aba")
                .pattern("bcb")
                .pattern("ddd")
                .define('a', AEBlocks.QUARTZ_GLASS)
                .define('b', AEItems.FLUIX_DUST)
                .define('c', kb.value.component)
                .define('d', Tags.Items.INGOTS_COPPER)
                .unlockedBy("has_item", has(kb.value.component))
                .save(consumer, ResourceLocation(AEAdditions.ID, "cells/fluid/${kb.key}k"))
        }
        for (kb in chemicalKbs) {
            addConditionalRecipeForMekanism("cells/chemical/casing-${kb.key}k", consumer) {
                ShapelessRecipeBuilder.shapeless(kb.value)
                    .requires(AMItems.CHEMICAL_CELL_HOUSING.get())
                    .requires(kb.value.component)
                    .unlockedBy("has_item", has(kb.value.component))
                    .save(it)
            }
            addConditionalRecipeForMekanism("cells/chemical/${kb.key}k", consumer) {
                ShapedRecipeBuilder.shaped(kb.value)
                    .pattern("aba")
                    .pattern("bcb")
                    .pattern("ddd")
                    .define('a', AEBlocks.QUARTZ_GLASS)
                    .define('b', AEItems.FLUIX_DUST)
                    .define('c', kb.value.component)
                    .define('d', osmiumTag)
                    .unlockedBy("has_item", has(kb.value.component))
                    .save(it)
            }
        }
    }
    
    private fun addConditionalRecipeForMekanism(resourcePath: String, consumer: Consumer<FinishedRecipe>, factory: (Consumer<FinishedRecipe>) -> Unit) {
        ConditionalRecipe.builder().addCondition(
            modLoaded("appmek")
        ).addRecipe() {
            factory(it)
        }.build(consumer, ResourceLocation(AEAdditions.ID, resourcePath))
    }
}