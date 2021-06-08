package extracells.block

import appeng.api.AEApi
import appeng.api.config.SecurityPermissions
import appeng.api.networking.IGridNode
import appeng.api.util.AEPartLocation
import extracells.block.properties.PropertyDrive
import extracells.models.drive.DriveSlotsState
import extracells.network.GuiHandler
import extracells.tileentity.TileEntityFluidInterface
import extracells.tileentity.TileEntityHardMeDrive
import extracells.util.PermissionUtil
import extracells.util.TileUtil
import extracells.util.WrenchUtil
import net.minecraft.block.BlockHorizontal
import net.minecraft.block.material.Material
import net.minecraft.block.properties.IProperty
import net.minecraft.block.properties.PropertyDirection
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.common.property.ExtendedBlockState
import net.minecraftforge.common.property.IExtendedBlockState
import kotlin.random.Random

class BlockHardMEDrive : BlockEC(Material.ROCK, 2.0f, 1000000f) {

    private var _facing: PropertyDirection? = null

    val facing: PropertyDirection
    get() {
        if (_facing == null) {
            _facing = BlockHorizontal.FACING
        }

        return _facing!!
    }

    override fun createNewTileEntity(worldIn: World, meta: Int): TileEntity? = TileEntityHardMeDrive()

    override fun getStateForPlacement(
        world: World,
        pos: BlockPos,
        side: EnumFacing,
        hitX: Float,
        hitY: Float,
        hitZ: Float,
        meta: Int,
        placer: EntityLivingBase,
        hand: EnumHand
    ): IBlockState {
        return defaultState.withProperty(facing, placer.horizontalFacing.opposite)
    }

    private fun dropItems(world: World, pos: BlockPos, tileEntity: TileEntityHardMeDrive) {
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
        if (world.isRemote) {
            return true
        }

        val x = pos.x
        val y = pos.y
        val z = pos.z

        val tile = world.getTileEntity(pos)

        if (tile is TileEntityHardMeDrive) {
            if (!PermissionUtil.hasPermission(player, SecurityPermissions.BUILD, tile.getGridNode(AEPartLocation.INTERNAL))) {
                return false
            }
        }

        val current: ItemStack? = player.getHeldItem(hand)
        if (player.isSneaking) {
            val rayTraceResult = RayTraceResult(Vec3d(hitX.toDouble(), hitY.toDouble(), hitZ.toDouble()), side, pos)

            val wrenchHandler = WrenchUtil.getHandler(current, player, rayTraceResult, hand)
            if (wrenchHandler != null) {
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

        if (entity == null) {
            return
        }

        super.onBlockPlacedBy(world, pos, state, entity, stack)

        val l = MathHelper.floor(entity.rotationYaw * 4f / 360f + 5.0) and 3

        world.setBlockState(pos, state.withProperty(facing, entity.horizontalFacing.opposite), 2)

        // TODO: Add Rotation

        if (world.isRemote) {
            return
        }

        val tile: TileEntity? = world.getTileEntity(pos)

        if (tile != null) {
            if (tile is TileEntityHardMeDrive) {
                val node = tile.getGridNode(AEPartLocation.INTERNAL)

                if (entity is EntityPlayer) {
                    node!!.playerID = AEApi.instance().registries().players().getID(entity)
                }

                node?.updateState()
            }
        }
    }

    override fun breakBlock(world: World, pos: BlockPos, state: IBlockState) {
        if (world.isRemote) {
            super.breakBlock(world, pos, state)

            return
        }

        val tile: TileEntity? = world.getTileEntity(pos)

        if (tile != null && tile is TileEntityHardMeDrive) {

            dropItems(world, pos, tile)

            val node: IGridNode? = tile.getGridNode(AEPartLocation.INTERNAL)

            node?.destroy()
        }

        super.breakBlock(world, pos, state)
    }

    override fun getRenderLayer(): BlockRenderLayer = BlockRenderLayer.CUTOUT

    override fun createBlockState(): BlockStateContainer {
        return ExtendedBlockState(this, arrayOf(facing), arrayOf(PropertyDrive.INSTANCE))
    }

    override fun getExtendedState(state: IBlockState, world: IBlockAccess, pos: BlockPos): IBlockState {
        val tileEntity: TileEntityHardMeDrive = TileUtil.getTile(world, pos, TileEntityHardMeDrive::class.java)
            ?: return super.getExtendedState(state, world, pos)

        val extendedState = super.getExtendedState(state, world, pos) as IExtendedBlockState

        return extendedState.withProperty(PropertyDrive.INSTANCE, DriveSlotsState.createState(tileEntity))
    }

    override fun getStateFromMeta(meta: Int): IBlockState {
        var facing = EnumFacing.VALUES[meta]

        if (facing.axis == EnumFacing.Axis.Y) {
            facing = EnumFacing.NORTH
        }

        return this.defaultState.withProperty(this.facing, facing)
    }

    override fun getMetaFromState(state: IBlockState): Int = state.getValue(facing).index

    override fun withRotation(state: IBlockState, rot: Rotation): IBlockState = state.withProperty(facing, rot.rotate(state.getValue(facing)))

    override fun withMirror(state: IBlockState, mirrorIn: Mirror): IBlockState = state.withRotation(mirrorIn.toRotation(state.getValue(
        facing)))
}