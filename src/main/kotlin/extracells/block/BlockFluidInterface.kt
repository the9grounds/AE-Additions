package extracells.block

import appeng.api.AEApi
import appeng.api.config.SecurityPermissions
import appeng.api.networking.IGridNode
import appeng.api.util.AEPartLocation
import extracells.api.IECTileEntity
import extracells.models.ModelManager
import extracells.network.GuiHandler
import extracells.tileentity.IListenerTile
import extracells.tileentity.TileEntityFluidInterface
import extracells.util.PermissionUtil
import extracells.util.WrenchUtil
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import kotlin.random.Random

class BlockFluidInterface: BlockEC(Material.IRON, 2.0f, 10.0f) {
    override fun createNewTileEntity(worldIn: World, meta: Int): TileEntity? = TileEntityFluidInterface()

    override fun registerModel(item: Item?, manager: ModelManager?) {
        manager?.registerItemModel(item, 0, "fluid_interface")
    }

    private fun dropPatterns(world: World, pos: BlockPos, tileEntity: TileEntityFluidInterface) {
        val rand = java.util.Random()
        val x = pos.x
        val y = pos.y
        val z = pos.z

        val inventory = tileEntity.inventory

        var i = 0

        while (i < inventory.sizeInventory) {
            val itemStack: ItemStack? = inventory.getStackInSlot(i)

            if (itemStack != null && itemStack.count > 0) {
                val rx = Random.nextFloat() * 0.8f + .1f
                val ry = Random.nextFloat() * 0.8f + .1f
                val rz = Random.nextFloat() * 0.8f + .1f

                val entityItem = EntityItem(world, (x + rx).toDouble(), (y + ry).toDouble(), (z + rz).toDouble(), itemStack.copy())

                if (itemStack.hasTagCompound()) {
                    entityItem.item.tagCompound = itemStack.tagCompound!!.copy()
                }

                val factor = 0.05f

                entityItem.motionX = rand.nextGaussian() * factor
                entityItem.motionY = rand.nextGaussian() * factor + 0.2f
                entityItem.motionZ = rand.nextGaussian() * factor

                world.spawnEntity(entityItem)

                itemStack.count = 0
            }

            i++
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
        // TODO: Deduplicate code
        if (world.isRemote) {
            return true
        }

        val x = pos.x
        val y = pos.y
        val z = pos.z

        val tile = world.getTileEntity(pos)

        if (tile is TileEntityFluidInterface) {
            if (!PermissionUtil.hasPermission(player, SecurityPermissions.BUILD, tile.getGridNode(AEPartLocation.INTERNAL))) {
                return false
            }
        }

        val current: ItemStack? = player.getHeldItem(hand)
        if (player.isSneaking) {
            val rayTraceResult = RayTraceResult(Vec3d(hitX.toDouble(), hitY.toDouble(), hitZ.toDouble()), side, pos)

            val wrenchHandler = WrenchUtil.getHandler(current, player, rayTraceResult, hand)
            if (wrenchHandler != null) {
                val block = ItemStack(this, 1, 0)

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
        entity: EntityLivingBase?,
        stack: ItemStack
    ) {
        if (world.isRemote) {
            return
        }

        val tile: TileEntity? = world.getTileEntity(pos)

        if (tile != null) {
            if (tile is TileEntityFluidInterface) {
                val node = tile.getGridNode(AEPartLocation.INTERNAL)

                if (entity != null && entity is EntityPlayer) {
                    node!!.playerID = AEApi.instance().registries().players().getID(entity)
                }

                node?.updateState()
            }

            if (tile is IListenerTile) {
                tile.registerListener()
            }
        }
    }

    override fun breakBlock(world: World, pos: BlockPos, state: IBlockState) {
        if (world.isRemote) {
            super.breakBlock(world, pos, state)

            return
        }

        val tile: TileEntity? = world.getTileEntity(pos)

        if (tile != null && tile is TileEntityFluidInterface) {

            dropPatterns(world, pos, tile)

            val node: IGridNode? = tile.getGridNode(AEPartLocation.INTERNAL)

            node?.destroy()
        }

        super.breakBlock(world, pos, state)
    }
}