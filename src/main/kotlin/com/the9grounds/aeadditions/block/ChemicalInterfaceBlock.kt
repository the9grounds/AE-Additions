package com.the9grounds.aeadditions.block

import com.the9grounds.aeadditions.container.ContainerOpener
import com.the9grounds.aeadditions.container.Locator
import com.the9grounds.aeadditions.container.chemical.ChemicalInterfaceContainer
import com.the9grounds.aeadditions.integration.Mods
import com.the9grounds.aeadditions.tile.ChemicalInterfaceTileEntity
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ActionResultType
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockRayTraceResult
import net.minecraft.world.IBlockReader
import net.minecraft.world.IWorld
import net.minecraft.world.World
import net.minecraftforge.common.ToolType

class ChemicalInterfaceBlock(properties: Properties) : AbstractNetworkedBlock(properties) {

    override fun createTileEntity(state: BlockState?, world: IBlockReader?): TileEntity? {
        if (Mods.MEKANISM.isEnabled) {
            return ChemicalInterfaceTileEntity()
        }
        
        return null
    }

    override fun hasTileEntity(state: BlockState?): Boolean = Mods.MEKANISM.isEnabled

    override fun onBlockActivated(
        state: BlockState,
        worldIn: World,
        pos: BlockPos,
        player: PlayerEntity,
        handIn: Hand,
        hit: BlockRayTraceResult
    ): ActionResultType {
        
        if (player.isSneaking) {
            return ActionResultType.PASS
        }
        
        val tileEntity = worldIn.getTileEntity(pos)
        
        if (tileEntity === null || tileEntity !is ChemicalInterfaceTileEntity) {
            return ActionResultType.PASS
        }
        
        if (!worldIn.isRemote) {
            ContainerOpener.openContainer(ChemicalInterfaceContainer.CHEMICAL_INTERFACE, player, Locator.forTileEntitySide(tileEntity, hit.face))
        }
        
        return ActionResultType.func_233537_a_(worldIn.isRemote)
    }

    override fun onPlayerDestroy(worldIn: IWorld, pos: BlockPos, state: BlockState) {
        super.onPlayerDestroy(worldIn, pos, state)
    }
    
    init {
        properties.hardnessAndResistance(2f, 11f).harvestTool(ToolType.PICKAXE).harvestLevel(0)
    }
}