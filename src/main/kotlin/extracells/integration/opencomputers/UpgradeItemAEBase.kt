package extracells.integration.opencomputers

import extracells.integration.Integration
import extracells.item.ItemECBase
import li.cil.oc.api.CreativeTab
import li.cil.oc.api.driver.EnvironmentProvider
import li.cil.oc.api.driver.item.HostAware
import li.cil.oc.api.driver.item.Slot
import li.cil.oc.api.internal.Drone
import li.cil.oc.api.internal.Robot
import li.cil.oc.api.network.EnvironmentHost
import li.cil.oc.api.network.ManagedEnvironment
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.EnumRarity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fml.common.Optional

@Optional.InterfaceList(value = [
    Optional.Interface(iface = "li.cil.oc.api.driver.item.HostAware", modid = "opencomputers", striprefs = true),
    Optional.Interface(iface = "li.cil.oc.api.driver.EnvironmentProvider", modid = "opencomputers", striprefs = true)
])
abstract class UpgradeItemAEBase : ItemECBase(), HostAware, EnvironmentProvider {
    @Optional.Method(modid = "opencomputers")
    override fun worksWith(itemStack: ItemStack?, host: Class<out EnvironmentHost>?): Boolean {
        return worksWith(itemStack) && host != null && (Robot::class.java.isAssignableFrom(host) || Drone::class.java.isAssignableFrom(host))
    }

    @Optional.Method(modid = "opencomputers")
    override fun worksWith(itemStack: ItemStack?): Boolean = itemStack != null && itemStack.item == this

    @Optional.Method(modid = "opencomputers")
    override fun createEnvironment(itemStack: ItemStack?, host: EnvironmentHost?): ManagedEnvironment? {
        if (itemStack != null && itemStack.item == this && worksWith(itemStack, host!!::class.java)) {
            try {
                return CompleteHelper.getCompleteUpgradeAE(host)
            } catch (e: Throwable) {
                return UpgradeAE(host)
            }
        }

        return null
    }

    override fun getRarity(stack: ItemStack): EnumRarity {
        return when(stack.itemDamage) {
            0 -> EnumRarity.RARE
            1 -> EnumRarity.UNCOMMON
            else -> super.getRarity(stack)
        }
    }

    @Optional.Method(modid = "opencomputers")
    fun getOCCreativeTab(): CreativeTabs = CreativeTab.instance

    override fun getCreativeTabs(): Array<CreativeTabs> {
        if (Integration.Mods.OPENCOMPUTERS.isEnabled) {
            return arrayOf(creativeTab!!, CreativeTab.instance)
        }

        return super.getCreativeTabs()
    }

    @Optional.Method(modid = "opencomputers")
    override fun slot(itemStack: ItemStack?): String = Slot.Upgrade

    @Optional.Method(modid = "opencomputers")
    override fun tier(itemStack: ItemStack): Int {
        return when(itemStack.itemDamage) {
            0 -> 2
            1 -> 1
            else -> 0
        }
    }

    @Optional.Method(modid = "opencomputers")
    override fun dataTag(itemStack: ItemStack): NBTTagCompound {
        if (!itemStack.hasTagCompound()) {
            itemStack.tagCompound = NBTTagCompound()
        }

        val nbt = itemStack.tagCompound!!

        if (!nbt.hasKey("oc:data")) {
            nbt.setTag("oc:data", NBTTagCompound())
        }

        return nbt.getCompoundTag("oc:data")
    }

    @Optional.Method(modid = "opencomputers")
    override fun getEnvironment(itemStack: ItemStack?): Class<*>? {
        if (itemStack != null && itemStack.item == this) {
            try {
                return CompleteHelper.getCompleteUpgradeAEClass()
            } catch (e: Throwable) {
                return UpgradeAE::class.java
            }
        }

        return null
    }
}