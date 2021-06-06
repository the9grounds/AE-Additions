package extracells.item

import extracells.integration.opencomputers.UpgradeItemAEBase
import extracells.models.ModelManager
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

object ItemOCUpgrade: UpgradeItemAEBase() {

    init {
        setHasSubtypes(true)
    }

    override fun getUnlocalizedName(): String = super.getUnlocalizedName().replace("item.extracells", "extracells.item")

    override fun getUnlocalizedName(stack: ItemStack): String = unlocalizedName

    override fun getItemStackDisplayName(stack: ItemStack): String {
        val tier = 3 - stack.itemDamage

        return super.getItemStackDisplayName(stack) + " (Tier " + tier + ")"
    }

    @SideOnly(Side.CLIENT)
    override fun getSubItems(tab: CreativeTabs, items: NonNullList<ItemStack>) {
        if (!this.isInCreativeTab(tab)) {
            return
        }

        items.add(ItemStack(this, 1, 2))
        items.add(ItemStack(this, 1, 1))
        items.add(ItemStack(this, 1, 0))
    }

    @SideOnly(Side.CLIENT)
    override fun registerModel(item: Item?, manager: ModelManager?) {
        var i = 0
        while (i < 3) {
            manager?.registerItemModel(item, i)
            i += 1
        }
    }
}