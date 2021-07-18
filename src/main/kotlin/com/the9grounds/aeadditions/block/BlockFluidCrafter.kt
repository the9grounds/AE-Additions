package com.the9grounds.aeadditions.block

import com.the9grounds.aeadditions.network.GuiHandler.launchGui
import net.minecraft.world.World
import net.minecraft.util.math.BlockPos
import net.minecraft.block.state.IBlockState
import com.the9grounds.aeadditions.util.TileUtil
import com.the9grounds.aeadditions.tileentity.TileEntityFluidCrafter
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.EnumHand
import net.minecraft.util.EnumFacing
import appeng.api.config.SecurityPermissions
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.Vec3d
import com.the9grounds.aeadditions.util.PermissionUtil
import com.the9grounds.aeadditions.util.WrenchUtil
import net.minecraft.block.material.Material
import net.minecraft.entity.EntityLivingBase
import net.minecraft.tileentity.TileEntity
import java.util.*

class BlockFluidCrafter : BlockAE(Material.IRON, 2.0f, 10.0f) {
    override fun breakBlock(world: World, pos: BlockPos, state: IBlockState) {
        dropItems(world, pos)
        if (!world.isRemote) TileUtil.destroy(world, pos)
        super.breakBlock(world, pos, state)
    }

    override fun createNewTileEntity(world: World, meta: Int): TileEntity? {
        return TileEntityFluidCrafter()
    }

    private fun dropItems(world: World, pos: BlockPos) {
        val rand = Random()
        val tileEntity = world.getTileEntity(pos) as? TileEntityFluidCrafter ?: return
        val inventory: IInventory = tileEntity.inventory
        for (i in 0 until inventory.sizeInventory) {
            val item = inventory.getStackInSlot(i)
            dropItem(item, rand, world, pos)
        }

        for (i in 0 until tileEntity.upgradeInventory.sizeInventory) {
            val item = tileEntity.upgradeInventory.getStackInSlot(i)
            dropItem(item, rand, world, pos)
        }
    }

    private fun dropItem(
        item: ItemStack?,
        rand: Random,
        world: World,
        pos: BlockPos
    ) {
        if (item != null && item.count > 0) {
            val rx = rand.nextFloat() * 0.8f + 0.1f
            val ry = rand.nextFloat() * 0.8f + 0.1f
            val rz = rand.nextFloat() * 0.8f + 0.1f
            val entityItem = EntityItem(
                world, (pos.x + rx).toDouble(), (pos.y + ry).toDouble(), (pos.z
                        + rz).toDouble(), item.copy()
            )
            if (item.hasTagCompound()) {
                entityItem.item.tagCompound = item.tagCompound!!.copy()
            }
            val factor = 0.05f
            entityItem.motionX = rand.nextGaussian() * factor
            entityItem.motionY = rand.nextGaussian() * factor + 0.2f
            entityItem.motionZ = rand.nextGaussian() * factor
            world.spawnEntity(entityItem)
            item.count = 0
        }
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
        launchGui(0, player, world, pos.x, pos.y, pos.z)
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