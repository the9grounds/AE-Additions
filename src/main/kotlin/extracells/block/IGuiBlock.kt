package extracells.block

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

interface IGuiBlock {
    @SideOnly(Side.CLIENT)
    fun getClientGuiElement(player: EntityPlayer?, world: World?, pos: BlockPos?): Any? = null

    fun getServerGuiElement(player: EntityPlayer?, world: World?, pos: BlockPos?): Any? = null
}