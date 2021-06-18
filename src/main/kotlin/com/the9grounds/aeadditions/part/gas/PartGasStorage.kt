package com.the9grounds.aeadditions.part.gas

import appeng.api.AEApi
import appeng.api.config.AccessRestriction
import appeng.api.config.SecurityPermissions
import appeng.api.networking.events.*
import appeng.api.parts.IPart
import appeng.api.parts.IPartCollisionHelper
import appeng.api.parts.IPartModel
import appeng.api.parts.PartItemStack
import appeng.api.storage.ICellContainer
import appeng.api.storage.ICellInventory
import appeng.api.storage.IMEInventoryHandler
import appeng.api.storage.IStorageChannel
import appeng.api.util.AECableType
import com.the9grounds.aeadditions.api.gas.IAEGasStack
import com.the9grounds.aeadditions.container.IUpgradeable
import com.the9grounds.aeadditions.container.gas.ContainerBusGasStorage
import com.the9grounds.aeadditions.gui.gas.GuiBusGasStorage
import com.the9grounds.aeadditions.gui.widget.fluid.IFluidSlotListener
import com.the9grounds.aeadditions.integration.Integration
import com.the9grounds.aeadditions.inventory.IInventoryListener
import com.the9grounds.aeadditions.inventory.InventoryPlain
import com.the9grounds.aeadditions.inventory.cell.HandlerPartStorageGas
import com.the9grounds.aeadditions.inventory.cell.IHandlerPartBase
import com.the9grounds.aeadditions.models.PartModels
import com.the9grounds.aeadditions.network.packet.other.PacketFluidSlotUpdate
import com.the9grounds.aeadditions.network.packet.part.PacketPartConfig
import com.the9grounds.aeadditions.part.PartECBase
import com.the9grounds.aeadditions.util.NetworkUtil
import com.the9grounds.aeadditions.util.PermissionUtil
import com.the9grounds.aeadditions.util.StorageChannels
import mekanism.api.gas.GasStack
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.IBlockAccess
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidRegistry
import java.util.*
import net.minecraftforge.fml.common.Optional

class PartGasStorage : PartECBase(), ICellContainer, IInventoryListener, IFluidSlotListener, IUpgradeable {

    private val gasList = mutableMapOf<GasStack, Int>()
    private val filterGases = arrayOfNulls<Fluid>(54)
    private val upgradeInventory = object : InventoryPlain("", 1, 1, this) {
        override fun isItemValidForSlot(i: Int, itemstack: ItemStack?): Boolean {
            return itemstack != null && AEApi.instance().definitions().materials().cardInverter().isSameAs(itemstack)
        }

        override fun onContentsChanged() {
            saveData()
        }
    }

    private var _priority = 0
    val handler = HandlerPartStorageGas(this)
    protected var access = AccessRestriction.READ_WRITE
    protected val channel = StorageChannels.GAS!!
    val isMekanismGasEnabled = Integration.Mods.MEKANISMGAS.isEnabled

    override fun getUpgradeInventory(): IInventory {
        return upgradeInventory
    }


    override fun getDrops(drops: MutableList<ItemStack>, wrenched: Boolean) {
        for (stack in upgradeInventory.slots){
            if (stack == null) {
                continue
            }

            drops.add(stack)
        }
    }

    override fun getItemStack(type: PartItemStack?): ItemStack {
        val stack = super.getItemStack(type)

        if (type!!.equals(PartItemStack.WRENCH)) {
            stack.tagCompound!!.removeTag("upgradeInventory")
        }

        return stack
    }

    override fun blinkCell(p0: Int) {
        // Do nothing
    }

    override fun getCableConnectionLength(p0: AECableType?): Float {
        return 3.0f
    }

    override fun getBoxes(bch: IPartCollisionHelper) {
        bch.addBox(2.0, 2.0, 15.0, 14.0, 14.0, 16.0)
        bch.addBox(4.0, 4.0, 14.0, 12.0, 12.0, 15.0)
        bch.addBox(5.0, 5.0, 13.0, 11.0, 11.0, 14.0)
    }

    override fun getCellArray(channel: IStorageChannel<*>?): MutableList<IMEInventoryHandler<*>> {
        val list = mutableListOf<IMEInventoryHandler<*>>()
        if (channel == this.channel) {
            list.add(this.handler)
        }

        updateNeighbor()

        return list
    }

    override fun getClientGuiElement(player: EntityPlayer?): Any {
        return GuiBusGasStorage(this, player)
    }

    override fun getLightLevel(): Int = 0

    override fun getPowerUsage(): Double = 0.0

    override fun getPriority(): Int {
        return this._priority
    }

    override fun getServerGuiElement(player: EntityPlayer?): Any {
        return ContainerBusGasStorage(this, player!!)
    }

    override fun onActivate(player: EntityPlayer?, enumHand: EnumHand?, pos: Vec3d?): Boolean {
        return PermissionUtil.hasPermission(player, SecurityPermissions.BUILD, this as IPart) && super.onActivate(player, enumHand, pos)
    }

    override fun onInventoryChanged() {
        this.handler.setInverted(AEApi.instance().definitions().materials().cardInverter().isSameAs(upgradeInventory.getStackInSlot(0)))

        saveData()
    }

    override fun onNeighborChanged(var1: IBlockAccess?, var2: BlockPos?, var3: BlockPos?) {
        handler.onNeighborChange()
        val node = getGridNode()

        if (node != null) {
            val grid = node.grid
            if (grid != null && this.wasChanged()) {
                grid.postEvent(MENetworkCellArrayUpdate())
                grid.postEvent(MENetworkStorageEvent(gridBlock.fluidMonitor, StorageChannels.GAS!!))
                grid.postEvent(MENetworkCellArrayUpdate())
            }
            host.markForUpdate()
        }

        super.onNeighborChanged(null, null, null)
    }

    @MENetworkEventSubscribe
    fun powerChange(event: MENetworkPowerStatusChange) {
        val node = gridNode
        if (node != null) {
            val isNowActive = node.isActive
            if (isNowActive != isActive) {
                isActive = isNowActive
                onNeighborChanged()
                host.markForUpdate()
            }
            node.grid.postEvent(MENetworkStorageEvent(gridBlock.fluidMonitor, StorageChannels.FLUID))
            node.grid.postEvent(MENetworkCellArrayUpdate())
        }
    }

    override fun readFromNBT(data: NBTTagCompound) {
        super.readFromNBT(data)
        this._priority = data.getInteger("priority")
        for (i in 0..8) {
            filterGases[i] = FluidRegistry.getFluid(data.getString("FilterFluid#$i"))
        }
        if (data.hasKey("access")) {
            try {
                access = AccessRestriction.valueOf(data.getString("access"))
            } catch (e: Throwable) {
            }
        }
        upgradeInventory.readFromNBT(data.getTagList("upgradeInventory", 10))
        onInventoryChanged()
        onNeighborChanged()
        handler.setPrioritizedFluids(filterGases)
        handler.setAccessRestriction(access)
    }

    override fun getStaticModels(): IPartModel {
        return if (isActive && isPowered) {
            PartModels.STORAGE_BUS_HAS_CHANNEL
        } else if (isPowered) {
            PartModels.STORAGE_BUS_ON
        } else {
            PartModels.STORAGE_BUS_OFF
        }
    }

    override fun saveChanges(cellInventory: ICellInventory<*>?) {
        saveData()
    }

    fun sendInformation(player: EntityPlayer?) {
        NetworkUtil.sendToPlayer(PacketFluidSlotUpdate(Arrays.asList(*filterGases)), player)
        NetworkUtil.sendToPlayer(
            PacketPartConfig(this, PacketPartConfig.FLUID_STORAGE_ACCESS, access.toString()),
            player
        )
    }

    override fun setFluid(index: Int, fluid: Fluid?, player: EntityPlayer?) {
        filterGases[index] = fluid
        handler.setPrioritizedFluids(filterGases)
        sendInformation(player)
        saveData()
    }

    fun updateAccess(access: AccessRestriction?) {
        this.access = access!!
        handler.setAccessRestriction(access)
        onNeighborChanged()
    }

    @MENetworkEventSubscribe
    fun updateChannels(channel: MENetworkChannelsChanged?) {
        val node = gridNode
        if (node != null) {
            val isNowActive = node.isActive
            if (isNowActive != isActive) {
                isActive = isNowActive
                onNeighborChanged()
                host.markForUpdate()
            }
        }
        node!!.grid.postEvent(
            MENetworkStorageEvent(
                gridBlock.fluidMonitor,
                StorageChannels.FLUID
            )
        )
        node.grid.postEvent(MENetworkCellArrayUpdate())
    }

    override fun writeToNBT(data: NBTTagCompound) {
        super.writeToNBT(data)
        data.setInteger("priority", this.priority)
        for (i in filterGases.indices) {
            val fluid: Fluid? = filterGases[i]
            if (fluid != null) {
                data.setString("FilterFluid#$i", fluid.name)
            } else {
                data.setString("FilterFluid#$i", "")
            }
        }
        data.setTag("upgradeInventory", upgradeInventory.writeToNBT())
        data.setString("access", access.name)
    }

    fun updateNeighbor() {
        if (isMekanismGasEnabled) {
            updateNeighborGases()
        }
    }

    @Optional.Method(modid = "MekanismAPI|gas")
    private fun updateNeighborGases() {
        gasList.clear()

        if ((access == AccessRestriction.READ) || (access == AccessRestriction.READ_WRITE)) {
            for (stack in (handler as IHandlerPartBase<IAEGasStack>).getAvailableItems(StorageChannels.GAS!!.createList())) {
                val gasStack = stack.gas as GasStack

                gasList.set(gasStack, gasStack.amount)
            }
        }
    }

    @Optional.Method(modid = "MekanismAPI|gas")
    fun wasChanged(): Boolean {
        var fluids = mutableMapOf<GasStack, Int>()

        for (stack in (handler as IHandlerPartBase<IAEGasStack>).getAvailableItems(StorageChannels.GAS!!.createList())) {
            val gasStack = stack.gasStack as GasStack

            fluids.set(gasStack, gasStack.amount)
        }

        return !fluids.equals(gasList)
    }

}