package com.the9grounds.aeadditions.integration.mekanism

import appeng.api.config.Actionable
import appeng.api.networking.security.IActionSource
import appeng.api.storage.IMEInventory
import appeng.api.storage.IStorageChannel
import appeng.api.storage.data.IItemList
import com.the9grounds.aeadditions.api.IExternalGasStorageHandler
import com.the9grounds.aeadditions.api.gas.IAEGasStack
import com.the9grounds.aeadditions.util.GasUtil
import com.the9grounds.aeadditions.util.StorageChannels
import mekanism.api.gas.GasStack
import mekanism.api.gas.GasTank
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing

object HandlerMekanismGasTank : IExternalGasStorageHandler {

    val clazz = Class.forName("mekanism.common.tile.TileEntityGasTank")

    override fun canHandle(tile: TileEntity?, d: EnumFacing?, mySrc: IActionSource?): Boolean {
        return tile != null && tile::class.java == clazz::class.java
    }

    override fun getInventory(tile: TileEntity?, d: EnumFacing?, src: IActionSource?): IMEInventory<IAEGasStack>? {
        if (tile == null) {
            return null
        }

        val tank = getGasTank(tile) ?: return null

        return Inventory(tank)
    }

    fun getGasTank(tile: TileEntity): GasTank? {
        try {
            val tank = clazz.getField("gasTank")

            if (tank != null) {
                return tank.get(tile) as GasTank
            }
        } catch (e: Throwable) {

        }

        return null
    }

    class Inventory(val tank: GasTank) : IMEInventory<IAEGasStack> {
        override fun injectItems(stackType: IAEGasStack?, actionable: Actionable?, actionSource: IActionSource?): IAEGasStack? {
            val gasStack = stackType?.gasStack as? GasStack ?: return null

            if (tank.canReceive(gasStack.gas)) {
                val accepted = tank.receive(gasStack, actionable == Actionable.MODULATE)

                if (accepted.toLong() == stackType.stackSize) {
                    return null
                }

                val returnStack = stackType.copy()

                returnStack.stackSize = stackType.stackSize - accepted.toLong()

                return returnStack
            }

            return stackType
        }

        override fun extractItems(stackType: IAEGasStack?, actionable: Actionable?, actionSource: IActionSource?): IAEGasStack? {
            val gasStack = stackType?.gasStack as? GasStack ?: return null

            if (tank.canDraw(gasStack.gas)) {
                val drawn = tank.draw(gasStack.amount, actionable == Actionable.MODULATE)

                return StorageChannels.GAS!!.createStack(drawn)
            }

            return null
        }

        override fun getAvailableItems(itemList: IItemList<IAEGasStack>?): IItemList<IAEGasStack>? {
            val gas = tank.gas

            if (gas != null) {
                itemList?.add(GasUtil.createAEGasStack(gas))
            }

            return itemList
        }

        override fun getChannel(): IStorageChannel<IAEGasStack> = StorageChannels.GAS!!

    }
}