package com.the9grounds.aeadditions.integration.jei;

import com.the9grounds.aeadditions.registries.ItemEnum;
import com.the9grounds.aeadditions.util.UniversalTerminal;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UniversalTerminalRecipeWrapper implements ICraftingRecipeWrapper {
    private final boolean isUniversal;

    public UniversalTerminalRecipeWrapper(boolean isUniversal){
        this.isUniversal = isUniversal;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {

        List<List<ItemStack>> in = new ArrayList<>();

        if (isUniversal) {
            List<ItemStack> univeralsTerminal = new ArrayList<ItemStack>();
            univeralsTerminal.add(ItemEnum.UNIVERSALTERMINAL.getSizedStack(1));
            in.add(univeralsTerminal);
        }else {
            in.add(UniversalTerminal.getWirelessTerminals());
        }

        in.add(UniversalTerminal.getTerminals());

        ingredients.setInputLists(ItemStack.class, in);
        ingredients.setOutput(ItemStack.class, ItemEnum.UNIVERSALTERMINAL.getSizedStack(1));
    }
}
