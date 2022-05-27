package com.the9grounds.aeadditions.core.mixin;

import appeng.blockentity.crafting.CraftingBlockEntity;
import appeng.blockentity.crafting.CraftingStorageBlockEntity;
import com.the9grounds.aeadditions.registries.Blocks;
import com.the9grounds.aeadditions.util.AE2;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CraftingStorageBlockEntity.class)
public abstract class CraftingStorageBlockEntityMixin extends CraftingBlockEntity {
    public CraftingStorageBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
        super(blockEntityType, pos, blockState);
    }
    
    @Overwrite
    public int getStorageBytes() {
        
        var extraBytes = AE2.getStorageBytes((CraftingStorageBlockEntity) (Object)this);
        
        if (null != extraBytes) {
            return extraBytes;
        }
        
        return super.getStorageBytes();
    };

    @Inject(method = "getItemFromBlockEntity()Lnet/minecraft/world/item/Item", at = @At("HEAD"), cancellable = true)
    private void extraCraftingCpus$getItemFromBlockEntity(CallbackInfoReturnable<Item> callbackInfoReturnable) {
        var storage = getStorageBytes() / 1024;
        
        switch(storage) {
            case 1024 -> callbackInfoReturnable.setReturnValue(Blocks.BLOCK_CRAFTING_STORAGE_1024k.getItem());
            case 4096 -> callbackInfoReturnable.setReturnValue(Blocks.BLOCK_CRAFTING_STORAGE_4096k.getItem());
            case 16384 -> callbackInfoReturnable.setReturnValue(Blocks.BLOCK_CRAFTING_STORAGE_16384k.getItem());
            case 65536 -> callbackInfoReturnable.setReturnValue(Blocks.BLOCK_CRAFTING_STORAGE_65536k.getItem());
        }
    }
    
//    @Inject(method = "getStorageBytes", at = @At("HEAD"))
//    private void extraCraftingCpus$getStorageBytes(CallbackInfoReturnable<Integer> callbackInfoReturnable) {
//        AE2.getStorageBytes((CraftingStorageBlockEntity)(Object) this, callbackInfoReturnable);
//    }
}
