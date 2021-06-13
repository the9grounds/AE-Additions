package com.the9grounds.aeadditions.integration.opencomputers

import appeng.api.parts.IPart
import appeng.api.parts.IPartHost
import appeng.api.util.AEPartLocation
import li.cil.oc.api.API
import li.cil.oc.common.item.data.DroneData
import li.cil.oc.common.item.data.MicrocontrollerData
import li.cil.oc.common.item.data.RobotData
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object OCUtils {

    @JvmStatic fun <P, C> getPart(world: World?, pos: BlockPos?, location: AEPartLocation?, clazz: Class<C>): P? where P: IPart, C: P {
        if (world == null || pos == null) {
            return null
        }

        val tile = world.getTileEntity(pos)

        if (tile == null || tile !is IPartHost) {
            return null
        }

        if (location == null || location == AEPartLocation.INTERNAL) {
            for (side in AEPartLocation.SIDE_LOCATIONS) {
                val part = tile.getPart(side)

                if (part != null && clazz::class == part::class) {
                    return part as P
                }
            }

            return null
        }

        val part = tile.getPart(location)

        if (part == null || clazz::class != part::class) {
            return null
        }

        return part as P
    }

    @JvmStatic fun isRobot(itemStack: ItemStack): Boolean {
        val item = API.items.get(itemStack) ?: return false

        return item.name() == "robot"
    }

    @JvmStatic fun isDrone(itemStack: ItemStack): Boolean {
        val item = API.items.get(itemStack) ?: return false

        return item.name() == "drone"
    }

    @JvmStatic fun getComponent(robot: RobotData, item: Item, meta: Int): ItemStack? {
        for (component in robot.components()) {
            if (component != null && component.item == item) {
                return component
            }
        }

        return null
    }

    @JvmStatic fun getComponent(robot: RobotData, item: Item): ItemStack? = getComponent(robot, item, 0)

    @JvmStatic fun getComponent(drone: DroneData, item: Item, meta: Int): ItemStack? {
        for (component in drone.components()) {
            if (component != null && component.item == item) {
                return component
            }
        }

        return null
    }

    @JvmStatic fun getComponent(drone: DroneData, item: Item): ItemStack? = getComponent(drone, item, 0)
}