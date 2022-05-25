package com.the9grounds.aeadditions.block

import appeng.api.util.AEPartLocation
import com.the9grounds.aeadditions.integration.appeng.AppEng
import com.the9grounds.aeadditions.tile.AbstractNetworkedTile
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

abstract class AbstractNetworkedBlock(properties: Properties) : Block(properties) {
    override fun onBlockPlacedBy(
        worldIn: World,
        pos: BlockPos,
        state: BlockState,
        placer: LivingEntity?,
        stack: ItemStack
    ) {
        if (!worldIn.isRemote && placer is PlayerEntity) {
            val tile = worldIn.getTileEntity(pos) as? AbstractNetworkedTile ?: return

            val gridNode = tile.getGridNode(AEPartLocation.INTERNAL) ?: return
            val id = AppEng.API!!.registries().players().getID(placer)

            gridNode.playerID = id
            gridNode.updateState()
        }
    }
}