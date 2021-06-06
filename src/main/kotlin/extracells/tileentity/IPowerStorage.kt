package extracells.tileentity

import appeng.api.config.AccessRestriction
import appeng.api.config.Actionable
import appeng.api.config.PowerMultiplier
import appeng.api.networking.energy.IAEPowerStorage
import net.minecraft.nbt.NBTTagCompound

interface IPowerStorage : IAEPowerStorage {

    companion object {
        val powerInformation = PowerInformation()
    }

    override fun getAECurrentPower(): Double {
        return powerInformation.currentPower
    }

    override fun getPowerFlow(): AccessRestriction {
        return AccessRestriction.READ_WRITE
    }

    override fun getAEMaxPower(): Double {
        return powerInformation.maxPower
    }

    fun setMaxPower(power: Double) {
        powerInformation.maxPower = power
    }

    override fun injectAEPower(amt: Double, mode: Actionable): Double {
        val maxStore = powerInformation.maxPower - powerInformation.currentPower

        val notStored = if (maxStore - amt >= 0) 0.00 else amt - maxStore

        if (mode == Actionable.MODULATE) {
            powerInformation.currentPower += amt - notStored
        }

        return notStored
    }

    override fun isAEPublicPowerStorage(): Boolean {
        return true
    }

    override fun extractAEPower(amount: Double, mode: Actionable, usePowerMultiplier: PowerMultiplier): Double {
        val toExtract = Math.min(amount, powerInformation.currentPower)
        if (mode == Actionable.MODULATE) {
            powerInformation.currentPower -= toExtract
        }

        return toExtract
    }

    fun readPowerFromNBT(tag: NBTTagCompound) {
        if (tag.hasKey("currentPowerBattery")) {
            powerInformation.currentPower = tag.getDouble("currentPowerBattery")
        }
    }

    fun writePowerToNBT(tag: NBTTagCompound) = tag.setDouble("currentPowerBattery", powerInformation.currentPower)

    class PowerInformation(var currentPower: Double = 0.00, var maxPower: Double = 500.00) {
    }
}