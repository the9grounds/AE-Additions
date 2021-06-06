package extracells.item

import extracells.api.ECApi
import extracells.api.IWirelessGasTermHandler
import extracells.models.ModelManager
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

object ItemWirelessTerminalGas : WirelessTermBase(), IWirelessGasTermHandler {

    init {
        ECApi.instance().registerWirelessTermHandler(this)
    }

    override fun getUnlocalizedName(stack: ItemStack): String {
        return super.getUnlocalizedName(stack).replace("item.extracells", "extracells.item")
    }

    override fun isItemNormalWirelessTermToo(`is`: ItemStack?): Boolean = false

    override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        return ActionResult(EnumActionResult.SUCCESS, ECApi.instance().openWirelessGasTerminal(player, hand, world))
    }

    @SideOnly(Side.CLIENT)
    override fun registerModel(item: Item?, manager: ModelManager?) {
        manager?.registerItemModel(item, 0, "terminals/fluid_wireless")
    }
}