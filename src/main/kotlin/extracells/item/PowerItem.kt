package extracells.item

import appeng.api.config.AccessRestriction
import appeng.api.config.Actionable
import appeng.api.config.PowerUnits
import appeng.api.implementations.items.IAEItemPowerStorage
import cofh.redstoneflux.api.IEnergyContainerItem
import li.cil.oc.integration.util.Power
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fml.common.Optional
import kotlin.math.min

@Optional.Interface(iface = "cofh.redstoneflux.api.IEnergyContainerItem", modid = "redstoneflux", striprefs = true)
abstract class PowerItem : ItemECBase(), IAEItemPowerStorage, IEnergyContainerItem  {
    abstract val MAX_POWER: Double

    override fun injectAEPower(itemStack: ItemStack?, amt: Double, actionable: Actionable?): Double {
        if (itemStack == null) return 0.0
        val tagCompound = ensureTagCompound(itemStack)
        val currentPower = tagCompound.getDouble("power")
        val toInject = min(amt, this.MAX_POWER - currentPower)

        if (actionable == Actionable.MODULATE) {
            tagCompound.setDouble("power", currentPower + toInject)
        }

        return toInject
    }

    override fun extractAEPower(itemStack: ItemStack?, amt: Double, actionable: Actionable?): Double {
        if (itemStack == null) return 0.0
        val tagCompound = ensureTagCompound(itemStack)
        val currentPower = tagCompound.getDouble("power")
        val toExtract = min(amt, currentPower)

        if (actionable == Actionable.MODULATE) {
            tagCompound.setDouble("power", currentPower - toExtract)
        }

        return toExtract
    }

    override fun getAEMaxPower(p0: ItemStack?): Double {
        return this.MAX_POWER
    }

    override fun getAECurrentPower(itemStack: ItemStack?): Double {

        if (itemStack == null) return 0.0

        val tagCompound = ensureTagCompound(itemStack)
        return tagCompound.getDouble("power")
    }

    @Optional.Method(modid = "redstoneflux")
    override fun receiveEnergy(container: ItemStack?, maxReceive: Int, simulate: Boolean): Int {
        if (container == null) return 0
        if (simulate) {
            val current = PowerUnits.AE.convertTo(PowerUnits.RF, getAECurrentPower(container))
            val max = PowerUnits.AE.convertTo(PowerUnits.RF, getAEMaxPower(container))

            if (max - current >= maxReceive) {
                return maxReceive
            } else {
                return (max - current).toInt()
            }
        }

        val currentAEPower = getAECurrentPower(container)

        if (currentAEPower < getAEMaxPower(container)) {
            return PowerUnits.AE.convertTo(PowerUnits.RF, injectAEPower(container, PowerUnits.RF.convertTo(PowerUnits.AE, maxReceive.toDouble()), Actionable.MODULATE)).toInt()
        }

        return 0
    }

    @Optional.Method(modid = "redstoneflux")
    override fun extractEnergy(container: ItemStack?, maxExtract: Int, simulate: Boolean): Int {
        if (container == null) return 0

        if (simulate) {
            return if (getEnergyStored(container) >= maxExtract) maxExtract else getEnergyStored(container)
        }

        return PowerUnits.AE.convertTo(PowerUnits.RF, extractAEPower(container, PowerUnits.RF.convertTo(PowerUnits.AE, maxExtract.toDouble()), Actionable.MODULATE)).toInt()
    }

    @Optional.Method(modid = "redstoneflux")
    override fun getEnergyStored(container: ItemStack?): Int {
        return PowerUnits.AE.convertTo(PowerUnits.RF, getAECurrentPower(container)).toInt()
    }

    @Optional.Method(modid = "redstoneflux")
    override fun getMaxEnergyStored(container: ItemStack?): Int {
        return PowerUnits.AE.convertTo(PowerUnits.RF, getAEMaxPower(container)).toInt()
    }

    fun ensureTagCompound(itemStack: ItemStack): NBTTagCompound {
        if (!itemStack.hasTagCompound()) {
            itemStack.tagCompound = NBTTagCompound()
        }

        return itemStack.tagCompound!!
    }
}