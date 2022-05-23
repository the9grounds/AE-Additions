package com.the9grounds.aeadditions.block

import appeng.api.util.AEPartLocation
import com.the9grounds.aeadditions.integration.appeng.AppEng
import com.the9grounds.aeadditions.tile.ChemicalInterfaceTileEntity
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockReader
import net.minecraft.world.World
import net.minecraftforge.common.ToolType

class ChemicalInterfaceBlock(properties: Properties) : Block(properties) {

    override fun createTileEntity(state: BlockState?, world: IBlockReader?): TileEntity? = ChemicalInterfaceTileEntity()

    override fun hasTileEntity(state: BlockState?): Boolean = true

    override fun onBlockPlacedBy(
        worldIn: World,
        pos: BlockPos,
        state: BlockState,
        placer: LivingEntity?,
        stack: ItemStack
    ) {
        if (!worldIn.isRemote && placer is PlayerEntity) {
            val tile = worldIn.getTileEntity(pos) as? ChemicalInterfaceTileEntity ?: return
            
            val gridNode = tile.getGridNode(AEPartLocation.INTERNAL) ?: return
            val id = AppEng.API!!.registries().players().getID(placer)
            
            gridNode.playerID = id
            gridNode.updateState()
        }
    }
    
    init {
        properties.hardnessAndResistance(2f, 11f).harvestTool(ToolType.PICKAXE).harvestLevel(0)
    }
}