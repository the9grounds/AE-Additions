package com.the9grounds.aeadditions.block

import com.the9grounds.aeadditions.blockentity.BaseMEWirelessTransceiverBlockEntity
import com.the9grounds.aeadditions.blockentity.MEWirelessTransceiverBlockEntity
import dev.architectury.injectables.annotations.ExpectPlatform
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.phys.BlockHitResult

class MEWirelessTransceiverBlock(properties: Properties) : Block(properties), EntityBlock {
    override fun newBlockEntity(pos: BlockPos, blockState: BlockState): BlockEntity? {
        return MEWirelessTransceiverBlock.getBlockEntity(pos, blockState)
    }

    override fun onPlace(
        blockState: BlockState,
        level: Level,
        pos: BlockPos,
        p_60569_: BlockState,
        p_60570_: Boolean
    ) {
        super.onPlace(blockState, level, pos, p_60569_, p_60570_)
        
        val blockEntity = level.getBlockEntity(pos, getBlockEntityType())
        
        if (blockEntity.isPresent) {
            blockEntity.get().blockPlaced()
        }
    }

    override fun use(
        blockState: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hit: BlockHitResult
    ): InteractionResult {
        
        val blockEntity = level.getBlockEntity(pos, getBlockEntityType())
        
        if (blockEntity.isPresent && !player.isShiftKeyDown) {
            if (!level.isClientSide) {
                player.openMenu(blockEntity.get())
            }
            return InteractionResult.sidedSuccess(level.isClientSide)
        }
        
        return super.use(blockState, level, pos, player, hand, hit)
    }

    override fun getDrops(state: BlockState, p_287596_: LootParams.Builder): MutableList<ItemStack> {
        return mutableListOf(ItemStack(state.block.asItem(), 1))
    }

    companion object {
        @JvmStatic
        @ExpectPlatform
        fun getBlockEntity(pos: BlockPos, blockState: BlockState): BaseMEWirelessTransceiverBlockEntity {
            throw Error()
        }

        @JvmStatic
        @ExpectPlatform
        fun getBlockEntityType(): BlockEntityType<BaseMEWirelessTransceiverBlockEntity> {
            throw Error()
        }
    }
}