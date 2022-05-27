package com.the9grounds.aeadditions.core.mixin;

import appeng.block.crafting.AbstractCraftingUnitBlock;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.ArrayList;
import java.util.Arrays;

// https://github.com/SpongePowered/Mixin/issues/387#issuecomment-888408556
@Mixin(AbstractCraftingUnitBlock.CraftingUnitType.class)
@Unique
public abstract class CraftingUnitTypeMixin {
    @Final
    @Mutable
    @Shadow(remap = false)
    private static AbstractCraftingUnitBlock.CraftingUnitType[] $VALUES;
    
    private static final AbstractCraftingUnitBlock.CraftingUnitType STORAGE_1024k = craftingUnitType$addType("STORAGE_1024k");
    private static final AbstractCraftingUnitBlock.CraftingUnitType STORAGE_4096k = craftingUnitType$addType("STORAGE_4096k");
    private static final AbstractCraftingUnitBlock.CraftingUnitType STORAGE_16384k = craftingUnitType$addType("STORAGE_16384k");
    private static final AbstractCraftingUnitBlock.CraftingUnitType STORAGE_65536k = craftingUnitType$addType("STORAGE_65536k");
    
    @Invoker(value = "<init>")
    public static AbstractCraftingUnitBlock.CraftingUnitType craftingUnitType$invokeInit(String internalName, int internalId) {
        throw new AssertionError();
    }
    
    private static AbstractCraftingUnitBlock.CraftingUnitType craftingUnitType$addType(String internalName) {
        ArrayList<AbstractCraftingUnitBlock.CraftingUnitType> variants = new ArrayList<AbstractCraftingUnitBlock.CraftingUnitType>(Arrays.asList(CraftingUnitTypeMixin.$VALUES));
        AbstractCraftingUnitBlock.CraftingUnitType type = craftingUnitType$invokeInit(internalName, variants.get(variants.size() - 1).ordinal());
        variants.add(type);
        CraftingUnitTypeMixin.$VALUES = variants.toArray(new AbstractCraftingUnitBlock.CraftingUnitType[0]);
        return type;
    }
}
