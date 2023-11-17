package com.the9grounds.aeadditions.part.gas

import appeng.api.config.Actionable
import appeng.api.config.SecurityPermissions
import appeng.api.networking.IGridNode
import appeng.api.networking.ticking.IGridTickable
import appeng.api.networking.ticking.TickRateModulation
import appeng.api.networking.ticking.TickingRequest
import appeng.api.parts.IPart
import appeng.api.parts.IPartCollisionHelper
import appeng.api.parts.IPartModel
import appeng.api.parts.PartItemStack
import appeng.api.util.AECableType
import com.the9grounds.aeadditions.container.ContainerTerminal
import com.the9grounds.aeadditions.container.StorageType
import com.the9grounds.aeadditions.gui.GuiTerminal
import com.the9grounds.aeadditions.integration.Integration
import com.the9grounds.aeadditions.inventory.IInventoryListener
import com.the9grounds.aeadditions.inventory.InventoryPlain
import com.the9grounds.aeadditions.models.PartModels
import com.the9grounds.aeadditions.network.packet.part.PacketTerminalSelectFluidClient
import com.the9grounds.aeadditions.part.PartECBase
import com.the9grounds.aeadditions.util.*
import mekanism.api.gas.GasStack
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumHand
import net.minecraft.util.math.Vec3d
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fml.common.Optional
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.apache.commons.lang3.tuple.MutablePair
import kotlin.math.min

class PartGasTerminal : PartECBase(), IGridTickable, IInventoryListener {

    private val containers: MutableList<Any> = mutableListOf()
    val inventory = object : InventoryPlain("com.the9grounds.aeadditions.part.gas.terminal", 2, 64, this) {
        override fun isItemValidForSlot(i: Int, itemstack: ItemStack): Boolean {
            return isItemValidForInputSlot(i, itemstack)
        }

        override fun onContentsChanged() {
            saveData()
        }
    }
    var currentFluid: Fluid? = null
    set(value) {
        field = value
        sendCurrentFluid()
    }

    protected var machineSource = MachineSource(this)

    override fun getDrops(drops: MutableList<ItemStack?>, wrenched: Boolean) {
        for (stack in inventory.slots) {
            if (stack == null) {
                continue
            }
            drops.add(stack)
        }
    }

    fun fillSecondSlot(itemStack: ItemStack?): Boolean {
        if (itemStack == null) {
            return false
        }
        val secondSlot = inventory.getStackInSlot(1)
        return if (secondSlot == null || secondSlot.isEmpty) {
            inventory.setInventorySlotContents(1, itemStack)
            true
        } else {
            if (!secondSlot.isItemEqual(itemStack) || !ItemStack.areItemStackTagsEqual(itemStack, secondSlot)) {
                return false
            }
            inventory.incrStackSize(1, itemStack.count)
            true
        }
    }

    override fun getBoxes(bch: IPartCollisionHelper) {
        bch.addBox(2.0, 2.0, 14.0, 14.0, 14.0, 16.0)
        bch.addBox(4.0, 4.0, 13.0, 12.0, 12.0, 14.0)
        bch.addBox(5.0, 5.0, 12.0, 11.0, 11.0, 13.0)
    }

    override fun getPowerUsage(): Double {
        return 0.5
    }

    override fun getTickingRequest(node: IGridNode): TickingRequest {
        return TickingRequest(1, 20, false, false)
    }

    override fun onActivate(player: EntityPlayer?, hand: EnumHand?, pos: Vec3d?): Boolean {
        return if (isActive && (PermissionUtil.hasPermission(
                player,
                SecurityPermissions.INJECT,
                this as IPart
            ) || PermissionUtil.hasPermission(player, SecurityPermissions.EXTRACT, this as IPart))
        ) {
            super.onActivate(player, hand, pos)
        } else false
    }

    override fun onInventoryChanged() {
        saveData()
    }

    override fun readFromNBT(data: NBTTagCompound) {
        super.readFromNBT(data)
        inventory.readFromNBT(data.getTagList("inventory", 10))
    }

    fun removeContainer(containerTerminalFluid: ContainerTerminal?) {
        if (containerTerminalFluid != null) {
            containers.remove(containerTerminalFluid)
        }
    }

    fun addContainer(containerTerminalFluid: ContainerTerminal?) {
        if (containerTerminalFluid != null) {
            containers.add(containerTerminalFluid)
            sendCurrentFluid()
        }
    }

    @SideOnly(Side.CLIENT)
    override fun getStaticModels(): IPartModel {
        return if (isActive) {
            PartModels.TERMINAL_HAS_CHANNEL
        } else if (isPowered) {
            PartModels.TERMINAL_ON
        } else {
            PartModels.TERMINAL_OFF
        }
    }

    fun sendCurrentFluid() {
        for (containerFluidTerminal in containers) {
            sendCurrentFluid(containerFluidTerminal)
        }
    }

    fun sendCurrentFluid(container: Any?) {
        if (container is ContainerTerminal) {
            NetworkUtil.sendToPlayer(PacketTerminalSelectFluidClient(currentFluid), container.player)
        }
    }

    override fun tickingRequest(node: IGridNode, ticksSinceLastCall: Int): TickRateModulation {
        doWork()

        return TickRateModulation.FASTER
    }

    override fun writeToNBT(data: NBTTagCompound) {
        super.writeToNBT(data)
        data.setTag("inventory", inventory.writeToNBT())
    }

    override fun getCableConnectionLength(aeCableType: AECableType?): Float {
        return 1.0f
    }

    fun decreaseFirstSlot() {
        val slot = inventory.getStackInSlot(0)
        slot.count = slot.count - 1
        if (slot.count <= 0) {
            inventory.setInventorySlotContents(0, ItemStack.EMPTY)
        }
    }

    override fun getItemStack(type: PartItemStack): ItemStack? {
        val stack = super.getItemStack(type)
        if (type == PartItemStack.WRENCH) {
            stack.tagCompound!!.removeTag("inventory")
        }
        return stack
    }

    val isMekanismLoaded = Integration.Mods.MEKANISMGAS.isEnabled

    var doNextFill = false

    fun isItemValidForInputSlot(i: Int, itemStack: ItemStack?): Boolean = GasUtil.isGasContainer(itemStack)

    fun doWork() {
        if (isMekanismLoaded) {
            doWorkGas()
        }
    }

    @Optional.Method(modid = "MekanismAPI|gas")
    fun doWorkGas() {
        val secondSlot: ItemStack? = inventory.getStackInSlot(1)

        if (secondSlot != null && !secondSlot.isEmpty && secondSlot.count >= secondSlot.maxStackSize) {
            return
        }

        var container: ItemStack? = inventory.getStackInSlot(0)

        if (container == null || container.isEmpty) {
            doNextFill = false
        }

        if (!GasUtil.isGasContainer(container) || container == null) {
            return
        }

        container = container.copy()
        container.count = 1

        val gridBlock = gridBlock ?: return
        val monitor = gridBlock.gasMonitor ?: return

        val gasStack = GasUtil.getGasFromContainer(container)

        if (GasUtil.isEmpty(container) || (gasStack.amount < GasUtil.getCapacity(container) && GasUtil.getFluidStack(gasStack)?.fluid == currentFluid && doNextFill)) {
            if (currentFluid == null) {
                return
            }

            val capacity = GasUtil.getCapacity(container)

            val result = monitor.extractItems(StorageChannels.GAS!!.createStack(GasStack(GasUtil.getGas(currentFluid), capacity)), Actionable.SIMULATE, this.machineSource)

            var proposedAmount = 0
            if (result == null) {
                proposedAmount = 0
            } else if (gasStack == null) {
                proposedAmount = min(capacity, result.stackSize.toInt())
            } else {
                proposedAmount = min(capacity - gasStack.amount, result.stackSize.toInt())
            }

            val filledContainer: MutablePair<Int, ItemStack> = GasUtil.fillStack(container, GasUtil.getGasStack(FluidStack(currentFluid, proposedAmount)))

            val filledContainerItemStack = filledContainer.right

            val gasStack2 = GasUtil.getGasFromContainer(filledContainerItemStack)
            if (gasStack2 == null) {
                doNextFill = false
            } else if (container.count == 1 && gasStack2.amount < GasUtil.getCapacity(filledContainerItemStack)) {
                inventory.setInventorySlotContents(0, filledContainerItemStack)
                monitor.extractItems(StorageChannels.GAS.createStack(GasStack(GasUtil.getGas(currentFluid), filledContainer.left)), Actionable.MODULATE, machineSource)
                doNextFill = true
            } else if (fillSecondSlot(filledContainerItemStack)) {
                monitor.extractItems(StorageChannels.GAS.createStack(GasStack(GasUtil.getGas(currentFluid), filledContainer.left)), Actionable.MODULATE, machineSource)
                decreaseFirstSlot()
                doNextFill = false
            }
        } else {
            val containerGas = GasUtil.getGasFromContainer(container)

            val drainedContainer: MutablePair<Int, ItemStack> = GasUtil.drainStack(container.copy(), containerGas)
            val gasStack = containerGas.copy()

            gasStack.amount = drainedContainer.left

            val notInjected = monitor.injectItems(StorageChannels.GAS!!.createStack(gasStack), Actionable.SIMULATE, machineSource)

            if (notInjected != null) {
                return
            }

            val emptyContainer = drainedContainer.right

            if (emptyContainer != null && !emptyContainer.isEmpty && GasUtil.getGasFromContainer(emptyContainer) != null && emptyContainer.count == 1) {
                monitor.injectItems(StorageChannels.GAS.createStack(gasStack), Actionable.MODULATE, this.machineSource)
                inventory.setInventorySlotContents(0, emptyContainer)
            } else if (emptyContainer == null || emptyContainer.isEmpty || fillSecondSlot(emptyContainer)) {
                monitor.injectItems(StorageChannels.GAS!!.createStack(containerGas), Actionable.MODULATE, machineSource)
                decreaseFirstSlot()
            }
        }
    }

    override fun getServerGuiElement(player: EntityPlayer?): Any? {
        if (isMekanismLoaded) {
            return ContainerTerminal(this, player, StorageType.GAS)
        }

        return null
    }

    override fun getClientGuiElement(player: EntityPlayer?): Any? {
        if (isMekanismLoaded) {
            return GuiTerminal(this, player, StorageType.GAS)
        }

        return null
    }
}