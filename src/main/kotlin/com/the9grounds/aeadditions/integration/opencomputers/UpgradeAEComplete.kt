package com.the9grounds.aeadditions.integration.opencomputers

import appeng.api.storage.data.IAEItemStack
import appeng.api.util.AEPartLocation
import appeng.tile.misc.TileSecurityStation
import li.cil.oc.api.machine.Arguments
import li.cil.oc.api.machine.Context
import li.cil.oc.api.network.EnvironmentHost
import li.cil.oc.api.network.Node
import li.cil.oc.integration.appeng.AEUtil
import li.cil.oc.integration.appeng.NetworkControl
import li.cil.oc.integration.appeng.`NetworkControl$`

//NetworkControl<TileSecurityStation>,
//li.cil.oc.integration.ec.NetworkControl<TileSecurityStation>
class UpgradeAEComplete(environmentHost: EnvironmentHost) : UpgradeAE(environmentHost) {

//    fun aeCraftItem(aeItem: IAEItemStack): IAEItemStack? {
//        val patterns = AEUtil.getGridCrafting(tile().getGridNode(pos())!!.grid).getCraftingFor(aeItem, null, 0, tile().world)
//        val pattern = patterns.find { it.outputs.any { iit ->  iit::class.java == aeItem::class.java } }
//        return when {
//            pattern != null -> pattern.outputs.find { it::class.java == aeItem::class.java }
//            else -> aeItem.copy().setStackSize(0)
//        }
//    }
//
//    fun aePotentialItem(aeItem: IAEItemStack): IAEItemStack? {
//        if (aeItem.stackSize > 0 || !aeItem.isCraftable) {
//            return aeItem
//        }
//
//        return aeCraftItem(aeItem)
//    }
//
//    override fun getAvgPowerUsage(context: Context?, args: Arguments?): Array<Any?> =
//        ResultWrapper.result(AEUtil.getGridEnergy(tile().getGridNode(pos())!!.grid).avgPowerUsage)
//
//    override fun getAvgPowerInjection(context: Context?, args: Arguments?): Array<Any?> =
//        ResultWrapper.result(AEUtil.getGridEnergy(tile().getGridNode(pos())!!.grid).avgPowerInjection)
//
//    override fun getCpus(context: Context?, args: Arguments?): Array<Any> {
////        val buffer = mutableListOf<>()
//        return super.getCraftables(context, args)
//    }
//
//    override fun getCraftables(context: Context?, args: Arguments?): Array<Any> {
//        return super.getCraftables(context, args)
//    }
//
//    override fun getItemsInNetwork(context: Context?, args: Arguments?): Array<Any> {
//        return super.getItemsInNetwork(context, args)
//    }
//
//    override fun store(context: Context?, args: Arguments?): Array<Any> {
//        return super.store(context, args)
//    }
//
//    override fun getFluidsInNetwork(context: Context?, args: Arguments?): Array<Any> {
//        return super.getFluidsInNetwork(context, args)
//    }
//
//    override fun getIdlePowerUsage(context: Context?, args: Arguments?): Array<Any> {
//        return super.getIdlePowerUsage(context, args)
//    }
//
//    override fun getMaxStoredPower(context: Context?, args: Arguments?): Array<Any> {
//        return super.getMaxStoredPower(context, args)
//    }
//
//    override fun getStoredPower(context: Context?, args: Arguments?): Array<Any> {
//        return super.getStoredPower(context, args)
//    }
}