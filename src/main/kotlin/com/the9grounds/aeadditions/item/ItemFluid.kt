package com.the9grounds.aeadditions.item

import com.the9grounds.aeadditions.Constants
import com.the9grounds.aeadditions.models.IItemModelRegister
import com.the9grounds.aeadditions.models.ModelManager
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.NonNullList
import net.minecraftforge.common.capabilities.ICapabilityProvider
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack.FLUID_NBT_KEY
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

class ItemFluid : Item(), IItemModelRegister {

    override fun initCapabilities(stack: ItemStack, nbt: NBTTagCompound?): ICapabilityProvider {
        return ItemFluidHandlerItemStack(stack, Fluid.BUCKET_VOLUME);
    }

    @SideOnly(Side.CLIENT)
    override fun registerModel(item: Item, manager: ModelManager) {
        manager.registerItemModel(item) { i: ItemStack ->
            val fluidStack = getFluid(i)
            val fluidName = fluidStack?.fluid?.name ?: ""
            ModelResourceLocation(
                Constants.MOD_ID + ":fluid/" + fluidName, "inventory"
            )
        }
    }

    @SideOnly(Side.CLIENT)
    override fun getSubItems(tab: CreativeTabs, subItems: NonNullList<ItemStack>) {
        if (!isInCreativeTab(tab)) return
        for (fluid in FluidRegistry.getRegisteredFluids().values) {
            val itemStack = ItemStack(this)
            setFluid(itemStack, FluidStack(fluid, 1))
            subItems.add(itemStack)
        }
    }

    override fun getItemStackDisplayName(stack: ItemStack): String {
        val fluidStack = getFluid(stack)
        if (fluidStack == null) {
            return "null"
        }
        val fluid = fluidStack.fluid
        return if (fluid != null) {
            fluid.getLocalizedName(FluidStack(fluid, Fluid.BUCKET_VOLUME))
        } else "null"
    }

    companion object {
        @JvmStatic
		fun setFluid(itemStack: ItemStack, fluidStack: FluidStack) {
            val nbtTagCompound = NBTTagCompound()
            fluidStack.writeToNBT(nbtTagCompound)

            if (itemStack.tagCompound == null) {
                itemStack.tagCompound = NBTTagCompound()
            }

            itemStack.tagCompound!!.setTag(FLUID_NBT_KEY, nbtTagCompound)
        }

        @JvmStatic
		fun getFluid(itemStack: ItemStack): FluidStack? {
            if (!itemStack.hasTagCompound()) {
                return null
            }
            val tagCompound = itemStack.tagCompound
            return FluidStack.loadFluidStackFromNBT(tagCompound!!.getCompoundTag(FLUID_NBT_KEY))
        }
    }

    class ItemFluidHandlerItemStack(stack: ItemStack, capacity: Int) : FluidHandlerItemStack(stack, capacity) {
        override fun drain(maxDrain: Int, doDrain: Boolean): FluidStack? {
            if (maxDrain <= 0) {
                return null
            }

            val contained = fluid
            if (contained == null || contained.amount <= 0 || !canDrainFluidType(contained)) {
                return null
            }

            val drainAmount = Math.min(contained.amount, maxDrain)

            val drained = contained.copy()
            drained.amount = drainAmount

            if (doDrain) {
                contained.amount -= drainAmount
                if (contained.amount == 0) {
                    setContainerToEmpty()
                } else {
                    fluid = contained
                }
            }

            return drained
        }
    }
}