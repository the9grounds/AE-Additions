package com.the9grounds.aeadditions.integration.opencomputers

import appeng.api.AEApi
import appeng.api.config.Actionable
import appeng.api.implementations.tiles.IWirelessAccessPoint
import appeng.api.networking.IGrid
import appeng.api.networking.IGridHost
import appeng.api.networking.security.IActionHost
import appeng.api.networking.storage.IStorageGrid
import appeng.api.storage.IMEMonitor
import appeng.api.storage.data.IAEFluidStack
import appeng.api.storage.data.IAEItemStack
import appeng.api.util.AEPartLocation
import com.the9grounds.aeadditions.registries.ItemEnum
import com.the9grounds.aeadditions.util.FluidHelper
import com.the9grounds.aeadditions.util.MachineSource
import com.the9grounds.aeadditions.util.StorageChannels
import li.cil.oc.api.internal.Agent
import li.cil.oc.api.internal.Database
import li.cil.oc.api.internal.Drone
import li.cil.oc.api.internal.Robot
import li.cil.oc.api.machine.Arguments
import li.cil.oc.api.machine.Callback
import li.cil.oc.api.machine.Context
import li.cil.oc.api.network.*
import li.cil.oc.api.prefab.AbstractManagedEnvironment
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import kotlin.math.exp
import kotlin.math.min

abstract class NetworkControl<T> : AbstractManagedEnvironment() where T : TileEntity, T : IActionHost, T: IGridHost {
    abstract fun tile(): T

    abstract fun host(): EnvironmentHost

    val robot: Robot?
    get() = (host()) as? Robot

    val drone: Drone?
    get() = (host()) as? Drone

    val agent: Agent?
    get() = (host()) as? Agent

    var isActive = false

    fun getComponent(): ItemStack? {
        if (robot != null) {
            return robot!!.getStackInSlot(robot!!.componentSlot(node().address()))
        } else if (drone != null) {
            val iterator = drone!!.internalComponents().iterator()

            while(iterator.hasNext()) {
                val item = iterator.next()
                if (item != null && item.item == ItemEnum.OCUPGRADE.item) {
                    return item
                }
            }
        }

        return null
    }

    fun getSecurity(): IGridHost? {
        if (host().world().isRemote) {
            return null
        }

        val component = getComponent() ?: return null
        val security = AEApi.instance().registries().locatable().getLocatableBy(getAEKey(component)) as IGridHost

        if (checkRange(component, security)) {
            return security
        }

        return null
    }

    fun checkRange(itemStack: ItemStack?, security: IGridHost?): Boolean {
        if (itemStack == null || security == null) {
            return false
        }

        val gridNode = security.getGridNode(AEPartLocation.INTERNAL) ?: return false

        val grid = gridNode.grid ?: return false

        return when(itemStack.itemDamage) {
            0 -> {
                val wirelessAccessPoint = AEApi.instance().definitions().blocks().wirelessAccessPoint().maybeEntity().get()

                grid.getMachines(wirelessAccessPoint as Class<out IGridHost>).iterator().hasNext()
            }
            1 -> {
                val gridBlock = gridNode.gridBlock ?: return false

                val location = gridBlock.location ?: return false

                val accessPoints = AEApi.instance().definitions().blocks().wirelessAccessPoint().maybeEntity().get()

                for (node in grid.getMachines(accessPoints as Class<out IGridHost>)) {
                    val accessPoint = node as IWirelessAccessPoint

                    val distance = accessPoint.location.subtract(agent!!.xPosition().toInt(), agent!!.yPosition().toInt(), agent!!.zPosition().toInt())
                    val squaredDistance = distance.x * distance.x + distance.y * distance.y + distance.z * distance.z

                    val range = accessPoint.range

                    if (squaredDistance <= range * range) {
                        return true
                    }
                }

                return false
            }
            else -> {
                val gridBlock = gridNode.gridBlock ?: return false

                val location = gridBlock.location ?: return false

                val accessPoints = AEApi.instance().definitions().blocks().wirelessAccessPoint().maybeEntity().get()

                for (node in grid.getMachines(accessPoints as Class<out IGridHost>)) {
                    val accessPoint = node as IWirelessAccessPoint

                    val distance = accessPoint.location.subtract(agent!!.xPosition().toInt(), agent!!.yPosition().toInt(), agent!!.zPosition().toInt())
                    val squaredDistance = distance.x * distance.x + distance.y * distance.y + distance.z * distance.z

                    val range = accessPoint.range / 2

                    if (squaredDistance <= range * range) {
                        return true
                    }
                }

                return false
            }
        }
    }

    fun getGrid(): IGrid? {
        if (host().world().isRemote) {
            return null
        }

        return getSecurity()?.getGridNode(AEPartLocation.INTERNAL)?.grid
    }

    fun getAEKey(itemStack: ItemStack): Long {
        try {
            return WirelessHandlerUpgradeAE.getEncryptionKey(itemStack).toLong()
        } catch (e: Throwable) {
            // Do nothing
        }

        return 0L
    }

    fun getFluidInventory(): IMEMonitor<IAEFluidStack>? {
        return getGrid()?.getCache<IStorageGrid>(IStorageGrid::class.java)?.getInventory(StorageChannels.FLUID)
    }

    fun getItemInventory(): IMEMonitor<IAEItemStack>? {
        return getGrid()?.getCache<IStorageGrid>(IStorageGrid::class.java)?.getInventory(StorageChannels.ITEM)
    }

    @Callback(doc = "function([number:amount]):number -- Transfer selected items to your ae system.")
    fun sendItems(context: Context, args: Arguments): List<Any> {
        val selected = agent!!.selectedSlot()
        val invRobot = agent!!.mainInventory()
        if (invRobot.sizeInventory <= 0) {
            return listOf(0)
        }

        val itemStack: ItemStack? = invRobot.getStackInSlot(selected)
        val inventory = getItemInventory()

        if (itemStack == null || itemStack.isEmpty || inventory == null) {
            return listOf(0)
        }

        val amount = min(args.optInteger(0, 64), itemStack.count)
        val itemStack2 = itemStack.copy()

        itemStack2.count = amount

        val notInjected = inventory.injectItems(StorageChannels.ITEM.createStack(itemStack2), Actionable.MODULATE, MachineSource(tile()))

        if (notInjected == null) {
            itemStack.count = itemStack.count - amount

            if (itemStack.count <= 0) {
                invRobot.setInventorySlotContents(selected, ItemStack.EMPTY)
            } else {
                invRobot.setInventorySlotContents(selected, itemStack)
            }

            return listOf(amount)
        } else {
            itemStack.count = itemStack.count - amount + notInjected.stackSize.toInt()

            if (itemStack.count <= 0) {
                invRobot.setInventorySlotContents(selected, ItemStack.EMPTY)
            } else {
                invRobot.setInventorySlotContents(selected, itemStack)
            }

            return listOf(itemStack2.count - notInjected.stackSize)
        }
    }

    @Callback(doc = "function(database:address, entry:number[, number:amount]):number -- Get items from your ae system.")
    fun requestItems(context: Context, args: Arguments): List<Any> {
        val address = args.checkString(0)
        val entry = args.checkInteger(1)
        val amount = args.optInteger(2, 64)

        val selected = agent!!.selectedSlot()
        val invRobot = agent!!.mainInventory()

        if (invRobot.sizeInventory <= 0) {
            return listOf(0)
        }

        val inventory = getItemInventory() ?: return listOf(0)

        val node: Node = node().network().node(address) ?: throw IllegalArgumentException("no such component")

        if (node !is Component) {
            throw IllegalArgumentException("no such component")
        }

        val environment = node.host()

        if (environment !is Database) {
            throw IllegalArgumentException("not a database")
        }

        val robotSelected: ItemStack? = invRobot.getStackInSlot(selected)

        val inSlot = robotSelected?.count ?: 0

        val maxSize = robotSelected?.maxStackSize ?: 64

        val itemStack: ItemStack = environment.getStackInSlot(entry - 1) ?: return listOf(0)

        itemStack.count = min(amount, maxSize - inSlot)

        val itemStack2 = itemStack.copy()

        itemStack2.count = 1

        val selected2 = if (robotSelected != null) {
            val selected3 = robotSelected.copy()
            selected3.count = 1
            selected3
        } else {
            null
        }
        // TODO: Null check for selected2
        if (robotSelected != null && !ItemStack.areItemStacksEqual(selected2, itemStack2) && !selected2!!.isEmpty) {
            return listOf(0)
        }

        val extracted = inventory.extractItems(StorageChannels.ITEM.createStack(itemStack), Actionable.MODULATE, MachineSource(tile())) ?: return listOf(0)

        val stackSize = extracted.stackSize.toInt()

        itemStack.count = inSlot + stackSize

        invRobot.setInventorySlotContents(selected, itemStack)

        return listOf(stackSize)
    }

    @Callback(doc = "function([number:amount]):number -- Transfer selected fluid to your ae system.")
    fun sendFluids(context: Context, args: Arguments): List<Any> {
        val selected = agent!!.selectedTank()
        val tanks = agent!!.tank()

        if (tanks.tankCount() <= 0) {
            return listOf(0)
        }

        val tank = tanks.getFluidTank(selected)

        val inventory = getFluidInventory()

        val fluid = tank.fluid

        if (tank == null || inventory == null || fluid == null) {
            return listOf(0)
        }

        val amount = min(args.optInteger(0, tank.capacity), tank.fluidAmount)

        val fluid2 = fluid.copy()

        fluid2.amount = amount

        val notInjected = inventory.injectItems(StorageChannels.FLUID.createStack(fluid2), Actionable.MODULATE, MachineSource(tile()))

        if (notInjected == null) {
            tank.drain(amount, true)

            return listOf(amount)
        }

        tank.drain(amount - notInjected.stackSize.toInt(), true)

        return listOf(amount - notInjected.stackSize)
    }

    @Callback(doc = "function(database:address, entry:number[, number:amount]):number -- Get fluid from your ae system.")
    fun requestFluids(context: Context, args: Arguments): List<Any> {
        val address = args.checkString(0)
        val entry = args.checkInteger(1)
        val amount = args.optInteger(2, 64)

        val selected = agent!!.selectedSlot()
        val tanks = agent!!.tank()

        if (tanks.tankCount() <= 0) {
            return listOf(0)
        }

        val tank = tanks.getFluidTank(selected)

        val inventory = getFluidInventory()

        if (tank == null || inventory == null) {
            return listOf(0)
        }

        val node = node().network().node(address) ?: throw IllegalArgumentException("no such component")

        if (node !is Component) {
            throw IllegalArgumentException("no such component")
        }

        val environment = node.host()

        if (environment !is Database) {
            throw IllegalArgumentException("not a database")
        }

        val fluid = FluidHelper.getFluidFromContainer(environment.getStackInSlot(entry - 1))

        fluid.amount = amount

        val fluid2 = fluid.copy()

        fluid2.amount = tank.fill(fluid, false)

        if (fluid2.amount == 0) {
            return listOf(0)
        }

        val extracted = inventory.extractItems(StorageChannels.FLUID.createStack(fluid2), Actionable.MODULATE, MachineSource(tile()))

        // TODO: Make sure this works
        if (extracted.fluidStack.amount == 0) {
            return listOf(0)
        }

        return listOf(tank.fill(extracted.fluidStack, true))
    }

    @Callback(doc = "function():boolean -- Return true if the card is linked to your ae network.")
    fun isLinked(context: Context, args: Arguments): List<Any> {
        return listOf(getGrid() != null)
    }

    override fun update() {
        super.update()

        if ((host().world().totalWorldTime % 10).toInt() == 0 && isActive) {
            val node = node() as Connector
            // Check if enough energy
            if (node.tryChangeBuffer(-energy)) {
                isActive = false
            }
        }
    }

    val energy: Double
    get() {
        val c = getComponent() ?: return .0

        return when(c.itemDamage) {
            0 -> .6
            1 -> .3
            else -> .05
        }
    }

    override fun onMessage(message: Message?) {
        super.onMessage(message)

        if (message!!.name() == "computer.stopped") {
            isActive = false
        }else if (message.name() == "computer.started") {
            isActive = true
        }
    }
}