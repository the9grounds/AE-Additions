package com.the9grounds.aeadditions.data.provider

import appeng.core.Api
import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.item.storage.ChemicalStorageCell
import com.the9grounds.aeadditions.item.storage.FluidStorageCell
import com.the9grounds.aeadditions.item.storage.PhysicalStorageCell
import com.the9grounds.aeadditions.registries.Blocks
import com.the9grounds.aeadditions.registries.Items
import com.the9grounds.aeadditions.registries.Parts
import net.minecraft.data.*
import net.minecraft.tags.ItemTags
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.Tags
import net.minecraftforge.common.crafting.ConditionalRecipe
import net.minecraftforge.common.crafting.conditions.IConditionBuilder
import java.util.function.Consumer

class StorageCellsRecipeProvider(generatorIn: DataGenerator) : RecipeProvider(generatorIn), IConditionBuilder {
    
    val certusCrystalTag = ItemTags.createOptional(ResourceLocation(appeng.core.AppEng.MOD_ID, "crystals/certus"))
    val panelTag = ItemTags.createOptional(ResourceLocation(appeng.core.AppEng.MOD_ID, "illuminated_panel"))
    
    override fun registerRecipes(consumer: Consumer<IFinishedRecipe>) {
        val itemComponents = mapOf(
            64 to Api.instance().definitions().materials().cell64kPart().item(),
            256 to Items.ITEM_STORAGE_COMPONENT_256k,
            1024 to Items.ITEM_STORAGE_COMPONENT_1024k,
            4096 to Items.ITEM_STORAGE_COMPONENT_4096k,
            16384 to Items.ITEM_STORAGE_COMPONENT_16384k
        )
        val itemKbs = mapOf<Int, PhysicalStorageCell>(
            256 to Items.ITEM_STORAGE_CELL_256k, 
            1024 to Items.ITEM_STORAGE_CELL_1024k,
            4096 to Items.ITEM_STORAGE_CELL_4096k, 
            16384 to Items.ITEM_STORAGE_CELL_16384k
        )
        val fluidItems = mapOf(
            64 to Api.instance().definitions().materials().fluidCell64kPart().item(),
            256 to Items.FLUID_STORAGE_COMPONENT_256k,
            1024 to Items.FLUID_STORAGE_COMPONENT_1024k,
            4096 to Items.FLUID_STORAGE_COMPONENT_4096k
        )
        val fluidKbs = mapOf<Int, FluidStorageCell>(
            256 to Items.FLUID_STORAGE_CELL_256k,
            1024 to Items.FLUID_STORAGE_CELL_1024k,
            4096 to Items.FLUID_STORAGE_CELL_4096k
        )
        val chemicalKbs = mapOf<Int, ChemicalStorageCell>(
            1 to Items.CHEMICAL_STORAGE_CELL_1k,
            4 to Items.CHEMICAL_STORAGE_CELL_4k,
            16 to Items.CHEMICAL_STORAGE_CELL_16k,
            64 to Items.CHEMICAL_STORAGE_CELL_64k,
            256 to Items.CHEMICAL_STORAGE_CELL_256k,
            1024 to Items.CHEMICAL_STORAGE_CELL_1024k,
            4096 to Items.CHEMICAL_STORAGE_CELL_4096k
        )
        val mapOfRequired = mapOf(
            4 to 1,
            16 to 4,
            64 to 16,
            256 to 64,
            1024 to 256,
            4096 to 1024,
            16384 to 4096
        )
        
        for (kb in itemKbs) {
            ShapelessRecipeBuilder.shapelessRecipe(kb.value)
                .addIngredient(Api.instance().definitions().materials().emptyStorageCell())
                .addIngredient(kb.value.component)
                .addCriterion("has_component", hasItem(kb.value.component))
                .build(consumer, ResourceLocation(AEAdditions.ID, "cells/item/casing-${kb.key}k"))

            ShapedRecipeBuilder.shapedRecipe(kb.value)
                .patternLine("aba")
                .patternLine("bcb")
                .patternLine("ddd")
                .key('a', Api.instance().definitions().blocks().quartzGlass())
                .key('b', Api.instance().definitions().materials().fluixDust())
                .key('c', kb.value.component)
                .key('d', Tags.Items.GEMS_DIAMOND)
                .addCriterion("has_component", hasItem(kb.value.component))
                .build(consumer, ResourceLocation(AEAdditions.ID, "cells/item/${kb.key}k"))

            ShapedRecipeBuilder.shapedRecipe(kb.value.component)
                .patternLine("aba")
                .patternLine("cdc")
                .patternLine("aca")
                .key('a', Tags.Items.GEMS_LAPIS)
                .key('b', Api.instance().definitions().materials().engProcessor())
                .key('c', itemComponents[mapOfRequired[kb.key]]!!)
                .key('d', Api.instance().definitions().materials().logicProcessor())
                .addCriterion("has_item", hasItem(itemComponents[mapOfRequired[kb.key]]!!))
                .build(consumer, ResourceLocation(AEAdditions.ID, "components/item/${kb.key}k"))
        }
        for (kb in fluidKbs) {
            ShapelessRecipeBuilder.shapelessRecipe(kb.value)
                .addIngredient(Api.instance().definitions().materials().emptyStorageCell())
                .addIngredient(kb.value.component)
                .addCriterion("has_component", hasItem(kb.value.component))
                .build(consumer, ResourceLocation(AEAdditions.ID, "cells/fluid/casing-${kb.key}k"))

            ShapedRecipeBuilder.shapedRecipe(kb.value)
                .patternLine("aba")
                .patternLine("bcb")
                .patternLine("ddd")
                .key('a', Api.instance().definitions().blocks().quartzGlass())
                .key('b', Api.instance().definitions().materials().fluixDust())
                .key('c', kb.value.component)
                .key('d', Tags.Items.INGOTS_IRON)
                .addCriterion("has_component", hasItem(kb.value.component))
                .build(consumer, ResourceLocation(AEAdditions.ID, "cells/fluid/${kb.key}k"))
            
            ShapedRecipeBuilder.shapedRecipe(kb.value.component)
                .patternLine("aba")
                .patternLine("cdc")
                .patternLine("aca")
                .key('a', Tags.Items.GEMS_LAPIS)
                .key('b', Api.instance().definitions().materials().engProcessor())
                .key('c', fluidItems[mapOfRequired[kb.key]]!!)
                .key('d', Api.instance().definitions().materials().logicProcessor())
                .addCriterion("has_item", hasItem(fluidItems[mapOfRequired[kb.key]]!!))
                .build(consumer, ResourceLocation(AEAdditions.ID, "components/fluid/${kb.key}k"))
        }
        for (kb in chemicalKbs) {
            ConditionalRecipe.builder().addCondition(
                modLoaded("mekanism")
            ).addRecipe() {
                ShapelessRecipeBuilder.shapelessRecipe(kb.value)
                    .addIngredient(Api.instance().definitions().materials().emptyStorageCell())
                    .addIngredient(kb.value.component)
                    .addCriterion("has_component", hasItem(kb.value.component))
                    .build(it)
            }.build(consumer, ResourceLocation(AEAdditions.ID, "cells/chemical/casing-${kb.key}k"))
            ConditionalRecipe.builder().addCondition(
                modLoaded("mekanism")
            ).addRecipe() {
                ShapedRecipeBuilder.shapedRecipe(kb.value)
                    .patternLine("aba")
                    .patternLine("bcb")
                    .patternLine("ddd")
                    .key('a', Api.instance().definitions().blocks().quartzGlass())
                    .key('b', Api.instance().definitions().materials().fluixDust())
                    .key('c', kb.value.component)
                    .key('d', Tags.Items.INGOTS_GOLD)
                    .addCriterion("has_component", hasItem(kb.value.component))
                    .build(it)
            }.build(consumer, ResourceLocation(AEAdditions.ID, "cells/chemical/${kb.key}k"))
        }

        ConditionalRecipe.builder().addCondition(
            modLoaded("mekanism")
        ).addRecipe() {
            ShapedRecipeBuilder.shapedRecipe(Items.CHEMICAL_STORAGE_COMPONENT_1k)
                .patternLine("aba")
                .patternLine("bcb")
                .patternLine("aba")
                .key('a', Tags.Items.DYES_YELLOW)
                .key('b', certusCrystalTag)
                .key('c', Api.instance().definitions().materials().logicProcessor())
                .addCriterion("has_item", hasItem(Api.instance().definitions().materials().logicProcessor()))
                .build(it)
        }.build(consumer, ResourceLocation(AEAdditions.ID, "components/chemical/1k"))
        
        for (item in listOf<Int>(4, 16, 64)) {
            ConditionalRecipe.builder().addCondition(
                modLoaded("mekanism")
            ).addRecipe() {
                ShapedRecipeBuilder.shapedRecipe(chemicalKbs[item]!!.component)
                    .patternLine("aba")
                    .patternLine("cdc")
                    .patternLine("aca")
                    .key('a', Tags.Items.DYES_YELLOW)
                    .key('b', Api.instance().definitions().materials().calcProcessor())
                    .key('c', chemicalKbs[mapOfRequired[item]]!!.component)
                    .key('d', Api.instance().definitions().blocks().quartzGlass())
                    .addCriterion("has_item", hasItem(chemicalKbs[mapOfRequired[item]]!!.component))
                    .build(it)
            }.build(consumer, ResourceLocation(AEAdditions.ID, "components/chemical/${item}k"))
        }
        
        for (item in listOf(256,1024,4096)) {
            ConditionalRecipe.builder().addCondition(
                modLoaded("mekanism")
            ).addRecipe() {
                ShapedRecipeBuilder.shapedRecipe(chemicalKbs[item]!!.component)
                    .patternLine("aba")
                    .patternLine("cdc")
                    .patternLine("aca")
                    .key('a', Tags.Items.DYES_YELLOW)
                    .key('b', Api.instance().definitions().materials().engProcessor())
                    .key('c', chemicalKbs[mapOfRequired[item]]!!.component)
                    .key('d', Api.instance().definitions().materials().logicProcessor())
                    .addCriterion("has_item", hasItem(chemicalKbs[mapOfRequired[item]]!!.component))
                    .build(it)
            }.build(consumer, ResourceLocation(AEAdditions.ID, "components/chemical/${item}k"))
        }

        addConditionalRecipeForMekanism("chemical_interface", consumer) {
            ShapedRecipeBuilder.shapedRecipe(Blocks.CHEMICAL_INTERFACE.item)
                .patternLine("aba")
                .patternLine("c d")
                .patternLine("aba")
                .key('a', Tags.Items.INGOTS_IRON)
                .key('d', Api.instance().definitions().materials().formationCore())
                .key('c', Api.instance().definitions().materials().annihilationCore())
                .key('b', Tags.Items.DYES_YELLOW)
                .addCriterion("has_item", hasItem(Api.instance().definitions().materials().formationCore()))
                .build(it)
        }

        addConditionalRecipeForMekanism("parts/chemical/import-bus", consumer) {
            ShapedRecipeBuilder.shapedRecipe(Parts.CHEMICAL_IMPORT_BUS)
                .patternLine("   ")
                .patternLine("aba")
                .patternLine("cdc")
                .key('a', Tags.Items.INGOTS_IRON)
                .key('b', Api.instance().definitions().materials().annihilationCore())
                .key('c', Tags.Items.DYES_YELLOW)
                .key('d', net.minecraft.block.Blocks.STICKY_PISTON)
                .addCriterion("has_item", hasItem(Tags.Items.DYES_YELLOW))
                .build(it)
        }

        addConditionalRecipeForMekanism("parts/chemical/import-bus-from-item", consumer) {
            ShapelessRecipeBuilder.shapelessRecipe(Parts.CHEMICAL_IMPORT_BUS)
                .addIngredient(Tags.Items.DYES_YELLOW)
                .addIngredient(Api.instance().definitions().parts().importBus())
                .addCriterion("has_item", hasItem(Api.instance().definitions().parts().importBus()))
                .build(it)
        }
        
        addConditionalRecipeForMekanism("parts/chemical/export-bus", consumer) {
            ShapedRecipeBuilder.shapedRecipe(Parts.CHEMICAL_EXPORT_BUS)
                .patternLine("   ")
                .patternLine("aba")
                .patternLine("cdc")
                .key('a', Tags.Items.INGOTS_IRON)
                .key('b', Api.instance().definitions().materials().formationCore())
                .key('c', Tags.Items.DYES_YELLOW)
                .key('d', net.minecraft.block.Blocks.PISTON)
                .addCriterion("has_item", hasItem(Tags.Items.DYES_YELLOW))
                .build(it)
        }

        addConditionalRecipeForMekanism("parts/chemical/export-bus-from-item", consumer) {
            ShapelessRecipeBuilder.shapelessRecipe(Parts.CHEMICAL_EXPORT_BUS)
                .addIngredient(Tags.Items.DYES_YELLOW)
                .addIngredient(Api.instance().definitions().parts().exportBus())
                .addCriterion("has_item", hasItem(Api.instance().definitions().parts().importBus()))
                .build(it)
        }
        
        addConditionalRecipeForMekanism("parts/chemical/storage-monitor", consumer) {
            ShapelessRecipeBuilder.shapelessRecipe(Parts.CHEMICAL_STORAGE_MONITOR)
                .addIngredient(net.minecraft.item.Items.YELLOW_DYE, 2)
                .addIngredient(Api.instance().definitions().parts().levelEmitter())
                .addIngredient(panelTag)
                .addCriterion("has_item", hasItem(Api.instance().definitions().parts().levelEmitter()))
                .build(it)
        }
        addConditionalRecipeForMekanism("parts/chemical/conversion-monitor", consumer) {
            ShapelessRecipeBuilder.shapelessRecipe(Parts.CHEMICAL_CONVERSION_MONITOR)
                .addIngredient(Parts.CHEMICAL_STORAGE_MONITOR)
                .addIngredient(Api.instance().definitions().materials().formationCore())
                .addIngredient(Api.instance().definitions().materials().annihilationCore())
                .addCriterion("has_item", hasItem(Parts.CHEMICAL_STORAGE_MONITOR))
                .build(it)
        }
        addConditionalRecipeForMekanism("parts/chemical/terminal", consumer) {
            ShapelessRecipeBuilder.shapelessRecipe(Parts.CHEMICAL_TERMINAL)
                .addIngredient(panelTag)
                .addIngredient(net.minecraft.item.Items.YELLOW_DYE, 2)
                .addIngredient(Api.instance().definitions().materials().logicProcessor())
                .addIngredient(Api.instance().definitions().materials().formationCore())
                .addIngredient(Api.instance().definitions().materials().annihilationCore())
                .addCriterion("has_item", hasItem(panelTag))
                .build(it)
        }
        
        ShapelessRecipeBuilder.shapelessRecipe(Parts.FLUID_STORAGE_MONITOR)
            .addIngredient(net.minecraft.item.Items.LAPIS_LAZULI, 2)
            .addIngredient(Api.instance().definitions().parts().fluidLevelEmitter())
            .addIngredient(panelTag)
            .addCriterion("has_item", hasItem(panelTag))
            .build(consumer, ResourceLocation(AEAdditions.ID, "parts/fluid/storage-monitor"))
        ShapelessRecipeBuilder.shapelessRecipe(Parts.FLUID_CONVERSION_MONITOR)
            .addIngredient(Parts.FLUID_STORAGE_MONITOR)
            .addIngredient(Api.instance().definitions().materials().formationCore())
            .addIngredient(Api.instance().definitions().materials().annihilationCore())
            .addCriterion("has_item", hasItem(Parts.FLUID_STORAGE_MONITOR))
            .build(consumer, ResourceLocation(AEAdditions.ID, "parts/fluid/conversion-monitor"))
    }
    
    private fun addConditionalRecipeForMekanism(resourcePath: String, consumer: Consumer<IFinishedRecipe>, factory: (Consumer<IFinishedRecipe>) -> Unit) {
        ConditionalRecipe.builder().addCondition(
            modLoaded("mekanism")
        ).addRecipe() {
            factory(it)
        }.build(consumer, ResourceLocation(AEAdditions.ID, resourcePath))
    }
}