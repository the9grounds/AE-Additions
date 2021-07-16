package com.the9grounds.aeadditions.block

import appeng.api.config.SecurityPermissions
import appeng.api.util.AEPartLocation
import com.the9grounds.aeadditions.api.IECTileEntity
import com.the9grounds.aeadditions.network.GuiHandler
import com.the9grounds.aeadditions.tileentity.IListenerTile
import com.the9grounds.aeadditions.tileentity.TileEntityFluidFiller
import com.the9grounds.aeadditions.tileentity.TileEntityFluidInterface
import com.the9grounds.aeadditions.util.PermissionUtil
import com.the9grounds.aeadditions.util.TileUtil
import com.the9grounds.aeadditions.util.WrenchUtil
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import java.util.*

class BlockFluidFiller : BlockAE(Material.IRON, 2.0f, 10.0f) {
    override fun createNewTileEntity(worldIn: World, meta: Int): TileEntity = TileEntityFluidFiller()

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
        val x = pos.x
        val y = pos.y
        val z = pos.z

        val current: ItemStack? = player.getHeldItem(hand)

        if (world.isRemote) {
            return true
        }

        val tile = world.getTileEntity(pos)

        if (tile is IECTileEntity) {
            if (!PermissionUtil.hasPermission(player, SecurityPermissions.BUILD, tile.getGridNode(AEPartLocation.INTERNAL))) {
                return false
            }
        }

        if (player.isSneaking) {
            val rayTraceResult = RayTraceResult(Vec3d(hitX.toDouble(), hitY.toDouble(), hitZ.toDouble()), side, pos)

            val wrenchHandler = WrenchUtil.getHandler(current, player, rayTraceResult, hand)
            if (wrenchHandler != null) {
                val block = ItemStack(this, 1, 0)

                // TODO: Make sure this is correct, class doesn't match above
                if (tile != null && tile is TileEntityFluidInterface) {
                    block.tagCompound = tile.writeFilter(NBTTagCompound())
                }

                dropBlockAsItem(world, pos, state, 1)

                world.setBlockToAir(pos)

                wrenchHandler.wrenchUsed(current, player, rayTraceResult, hand)
                return true
            }
        }

        GuiHandler.launchGui(0, player, world, x, y, z)

        return true
    }

    override fun onBlockPlacedBy(
        world: World,
        pos: BlockPos,
        state: IBlockState,
        entity: EntityLivingBase,
        stack: ItemStack
    ) {
        super.onBlockPlacedBy(world, pos, state, entity, stack)

        if (world.isRemote) {
            return
        }

        TileUtil.setOwner(world, pos, entity)

        val tile: TileEntity? = world.getTileEntity(pos)

        if (tile != null && tile is IListenerTile) {
            tile.registerListener()
        }
    }

    override fun breakBlock(world: World, pos: BlockPos, state: IBlockState) {
        if (world.isRemote) {
            super.breakBlock(world, pos, state)

            return
        }

        dropItems(world, pos)

        TileUtil.destroy(world, pos)

        val tile: TileEntity? = world.getTileEntity(pos)

        if (tile != null && tile is IListenerTile) {
            tile.removeListener()
        }

        if (tile is TileEntityFluidFiller) {
            tile.finishCrafting()
        }

        super.breakBlock(world, pos, state)
    }

    fun dropItems(world: World, pos: BlockPos) {
        val rand = Random()
        val tileEntity = world.getTileEntity(pos) as? TileEntityFluidFiller ?: return

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
}