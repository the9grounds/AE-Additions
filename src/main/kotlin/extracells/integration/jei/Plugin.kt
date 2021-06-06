package extracells.integration.jei

import extracells.integration.Integration
import extracells.registries.BlockEnum
import extracells.registries.ItemEnum
import extracells.util.CreativeTabEC
import mezz.jei.api.IModPlugin
import mezz.jei.api.IModRegistry
import mezz.jei.api.ISubtypeRegistry
import mezz.jei.api.JEIPlugin
import mezz.jei.api.ingredients.IModIngredientRegistration
import mezz.jei.api.recipe.VanillaRecipeCategoryUid
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList

@JEIPlugin
class Plugin : IModPlugin {

    override fun register(registry: IModRegistry) {

        if (!Integration.Mods.JEI.isEnabled) {
            return
        }

        Jei.registry = registry

        val terminalRecipes = mutableListOf<UniversalTerminalRecipeWrapper>()

        terminalRecipes.add(UniversalTerminalRecipeWrapper(true))
        terminalRecipes.add(UniversalTerminalRecipeWrapper(false))

        registry.addRecipes(terminalRecipes, VanillaRecipeCategoryUid.CRAFTING)

        for (item in Jei.fluidBlackList) {
            registry.jeiHelpers.ingredientBlacklist.addIngredientToBlacklist(item)
        }

        hideItem(ItemStack(ItemEnum.FLUIDITEM.item), registry)
        hideItem(ItemStack(ItemEnum.GASITEM.item), registry)
        hideItem(ItemStack(ItemEnum.CRAFTINGPATTERN.item), registry)

        for (item in ItemEnum.values()) {
            if (item.mod != null && item.mod.isEnabled) {
                val i = item.item

                val list = NonNullList.create<ItemStack>()

                i.getSubItems(CreativeTabEC.INSTANCE, list)

                val iterator = list.iterator()
                while (iterator.hasNext()) {
                    hideItem(iterator.next(), registry)
                }
            }
        }

        for (block in BlockEnum.values()) {
            if (block.mod != null && !block.mod.isEnabled) {
                val b = block.block

                val list = NonNullList.create<ItemStack>()

                b.getSubBlocks(CreativeTabEC.INSTANCE, list)

                val iterator = list.iterator()
                while (iterator.hasNext()) {
                    hideItem(iterator.next(), registry)
                }
            }
        }
    }

    private fun hideItem(item: ItemStack, registry: IModRegistry) {
        registry.jeiHelpers.ingredientBlacklist.addIngredientToBlacklist(item)
    }
}