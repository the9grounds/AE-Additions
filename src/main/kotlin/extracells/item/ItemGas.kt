package extracells.item

import extracells.Constants
import extracells.integration.Integration
import extracells.models.IItemModelRegister
import extracells.models.ModelManager
import mekanism.api.gas.GasRegistry
import net.minecraft.client.renderer.ItemMeshDefinition
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagString
import net.minecraft.util.NonNullList
import net.minecraftforge.fml.common.Optional
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

object ItemGas: Item(), IItemModelRegister {

    val isMekanismGasEnabled = Integration.Mods.MEKANISMGAS.isEnabled

    fun setGasName(itemStack: ItemStack?, fluidName: String) {
        itemStack?.setTagInfo("gas", NBTTagString(fluidName))
    }

    fun getGasName(itemStack: ItemStack?): String {
        if (itemStack == null || itemStack.hasTagCompound()) {
            return ""
        }

        return itemStack.tagCompound!!.getString("gas")
    }

    @SideOnly(Side.CLIENT)
    override fun registerModel(item: Item?, manager: ModelManager?) {
        manager?.registerItemModel(item, object : ItemMeshDefinition {
            override fun getModelLocation(stack: ItemStack): ModelResourceLocation {
                if (isMekanismGasEnabled) {
                    return ModelResourceLocation(Constants.MOD_ID + ":gas/" + getGasName(stack), "inventory")
                }

                return ModelResourceLocation(Constants.MOD_ID + ":fluid/water", "inventory")
            }
        })
    }

    @SideOnly(Side.CLIENT)
    override fun getSubItems(tab: CreativeTabs, items: NonNullList<ItemStack>) {
        if (!this.isInCreativeTab(tab)) {
            return
        }

        if (isMekanismGasEnabled) {
            getSubItemsGas(items)
        }
    }

    @SideOnly(Side.CLIENT)
    @Optional.Method(modid = "MekanismAPI|gas")
    private fun getSubItemsGas(subItems: NonNullList<ItemStack>) {
        for (gas in GasRegistry.getRegisteredGasses()) {
            val itemStack = ItemStack(this)
            ItemGas.setGasName(itemStack, gas.name)
            subItems.add(itemStack)
        }
    }

    override fun getItemStackDisplayName(stack: ItemStack): String {
        if (isMekanismGasEnabled) {
            return getItemStackDisplayNameGas(stack)
        }

        return "null"
    }

    @Optional.Method(modid = "MekanismAPI|gas")
    private fun getItemStackDisplayNameGas(stack: ItemStack): String {
        val gasName = ItemGas.getGasName(stack)
        if (gasName.isEmpty()) {
            return "null"
        }

        val gas = GasRegistry.getGas(gasName)

        if (gas != null) {
            return gas.localizedName
        }

        return "null"
    }
}