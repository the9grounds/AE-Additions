package com.the9grounds.aeadditions.block

import appeng.api.config.SecurityPermissions
import com.the9grounds.aeadditions.network.GuiHandler
import com.the9grounds.aeadditions.tileentity.TileEntityGasInterface
import com.the9grounds.aeadditions.util.PermissionUtil
import com.the9grounds.aeadditions.util.TileUtil
import com.the9grounds.aeadditions.util.WrenchUtil
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

class BlockGasInterface : BlockAE(Material.IRON, 2.0f, 10.0f) {
    override fun createNewTileEntity(worldIn: World, meta: Int): TileEntity = TileEntityGasInterface()

    override fun breakBlock(world: World, pos: BlockPos, state: IBlockState) {
        if (!world.isRemote) TileUtil.destroy(world, pos)
        super.breakBlock(world, pos, state)
    }

    override fun onBlockActivated(
        world: World,
        pos: BlockPos,
        state: IBlockState,
        player: EntityPlayer,
        hand: EnumHand,
        side: EnumFacing,
        hitX: Float,
        hitY: Float,
        hitZ: Float
    ): Boolean {
        if (world.isRemote) {
            return true
        }
        val current = player.getHeldItem(hand)
        if (player.isSneaking) {
            val tile = world.getTileEntity(pos)
            if (!PermissionUtil.hasPermission(player, SecurityPermissions.BUILD, tile)) {
                return false
            }
            val rayTraceResult = RayTraceResult(Vec3d(hitX.toDouble(), hitY.toDouble(), hitZ.toDouble()), side, pos)
            val wrenchHandler = WrenchUtil.getHandler(current, player, rayTraceResult, hand)
            if (wrenchHandler != null) {
                spawnAsEntity(world, pos, ItemStack(this))
                world.setBlockToAir(pos)
                wrenchHandler.wrenchUsed(current, player, rayTraceResult, hand)
                return true
            }
        }
        GuiHandler.launchGui(0, player, world, pos.x, pos.y, pos.z)
        return true
    }

    override fun onBlockPlacedBy(
        world: World,
        pos: BlockPos,
        state: IBlockState,
        placer: EntityLivingBase,
        stack: ItemStack
    ) {
        if (world.isRemote) {
            return
        }
        TileUtil.setOwner(world, pos, placer)
    }
}