package com.the9grounds.aeadditions.integration.opencomputers

import appeng.api.features.IWirelessTermHandler
import appeng.api.util.IConfigManager
import appeng.core.sync.GuiBridge
import com.the9grounds.aeadditions.registries.ItemEnum
import li.cil.oc.common.item.data.DroneData
import li.cil.oc.common.item.data.MicrocontrollerData
import li.cil.oc.common.item.data.RobotData
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fml.common.network.IGuiHandler

object WirelessHandlerUpgradeAE : IWirelessTermHandler {
    override fun getEncryptionKey(itemStack: ItemStack?): String {
        if (itemStack == null) {
            return ""
        }

        if (OCUtils.isRobot(itemStack)) {
            return getEncryptionKeyForRobot(itemStack)
        }
        if (OCUtils.isDrone(itemStack)) {
            return getEncryptionKeyForDrone(itemStack)
        }

        if (!itemStack.hasTagCompound()) {
            itemStack.tagCompound = NBTTagCompound()
        }

        return itemStack.tagCompound!!.getString("key")
    }

    private fun getEncryptionKeyForRobot(itemStack: ItemStack): String {
        val robotData = RobotData(itemStack)

        val component = OCUtils.getComponent(robotData, ItemEnum.OCUPGRADE.item) ?: return ""

        return getEncryptionKey(component)
    }

    private fun getEncryptionKeyForDrone(itemStack: ItemStack): String {
        val droneData = DroneData(itemStack)

        val component = OCUtils.getComponent(droneData, ItemEnum.OCUPGRADE.item) ?: return ""

        return getEncryptionKey(component)
    }

    override fun setEncryptionKey(itemStack: ItemStack?, encryptionKey: String?, name: String?) {
        if (itemStack == null) {
            return
        }
        if (OCUtils.isRobot(itemStack)) {
            setEncryptionKeyForRobot(itemStack, encryptionKey, name)
            return
        }
        if (OCUtils.isDrone(itemStack)) {
            setEncryptionKeyForDrone(itemStack, encryptionKey, name)

            return
        }

        if (!itemStack.hasTagCompound()) {
            itemStack.tagCompound = NBTTagCompound()
        }

        itemStack.tagCompound!!.setString("key", encryptionKey)
    }

    private fun setEncryptionKeyForRobot(itemStack: ItemStack, encryptionKey: String?, name: String?) {
        val robot = RobotData(itemStack)
        val component = OCUtils.getComponent(robot, ItemEnum.OCUPGRADE.item)

        if (component != null) {
            setEncryptionKey(itemStack, encryptionKey, name)
        }

        robot.save(itemStack)
    }

    private fun setEncryptionKeyForDrone(itemStack: ItemStack, encryptionKey: String?, name: String?) {
        val drone = DroneData(itemStack)
        val component = OCUtils.getComponent(drone, ItemEnum.OCUPGRADE.item)

        if (component != null) {
            setEncryptionKey(itemStack, encryptionKey, name)
        }

        drone.save(itemStack)
    }

    override fun canHandle(itemStack: ItemStack?): Boolean {
        if (itemStack == null) {
            return false
        }

        val item = itemStack.item

        if (item == ItemEnum.OCUPGRADE.item) {
            return true
        }

        val robotCheck = OCUtils.isRobot(itemStack) && OCUtils.getComponent(RobotData(itemStack), ItemEnum.OCUPGRADE.item) != null
        val droneCheck = OCUtils.isDrone(itemStack) && OCUtils.getComponent(DroneData(itemStack), ItemEnum.OCUPGRADE.item) != null

        return robotCheck || droneCheck
    }

    override fun usePower(entityPlayer: EntityPlayer?, v: Double, itemStack: ItemStack?): Boolean = false

    override fun hasPower(entityPlayer: EntityPlayer?, v: Double, itemStack: ItemStack?): Boolean = true

    override fun getConfigManager(itemStack: ItemStack?): IConfigManager? = null

    fun getGuiHandler(itemStack: ItemStack):IGuiHandler = GuiBridge.GUI_WIRELESS_TERM
}