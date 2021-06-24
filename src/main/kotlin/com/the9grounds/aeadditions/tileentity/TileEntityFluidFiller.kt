package com.the9grounds.aeadditions.tileentity

import com.the9grounds.aeadditions.tileentity.TileBase
import appeng.api.networking.security.IActionHost
import appeng.api.networking.crafting.ICraftingProvider
import com.the9grounds.aeadditions.api.IECTileEntity
import appeng.api.storage.IMEMonitorHandlerReceiver
import appeng.api.storage.data.IAEFluidStack
import com.the9grounds.aeadditions.tileentity.IListenerTile
import com.the9grounds.aeadditions.gridblock.ECFluidGridBlock
import appeng.api.networking.IGridNode
import net.minecraft.item.ItemStack
import appeng.api.AEApi
import appeng.api.networking.events.MENetworkEventSubscribe
import appeng.api.networking.events.MENetworkCellArrayUpdate
import appeng.api.networking.storage.IStorageGrid
import net.minecraftforge.fml.common.FMLCommonHandler
import appeng.api.util.AEPartLocation
import appeng.api.util.AECableType
import net.minecraft.nbt.NBTTagCompound
import appeng.api.util.DimensionalCoord
import net.minecraft.nbt.NBTTagList
import appeng.api.networking.IGrid
import appeng.api.networking.storage.IBaseMonitor
import appeng.api.networking.security.IActionSource
import appeng.api.storage.IMEMonitor
import appeng.api.networking.events.MENetworkCraftingPatternChange
import appeng.api.networking.events.MENetworkPowerStatusChange
import appeng.api.networking.crafting.ICraftingPatternDetails
import net.minecraftforge.fluids.FluidStack
import appeng.api.networking.crafting.ICraftingProviderHelper
import appeng.api.implementations.ICraftingPatternItem
import net.minecraft.inventory.InventoryCrafting
import appeng.api.config.Actionable
import appeng.api.storage.data.IAEItemStack
import appeng.fluids.util.AEFluidStack
import com.the9grounds.aeadditions.container.IUpgradeable
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.client.gui.inventory.GuiContainer
import com.the9grounds.aeadditions.gui.fluid.GuiFluidFiller
import com.the9grounds.aeadditions.container.fluid.ContainerFluidFiller
import com.the9grounds.aeadditions.gui.widget.fluid.IFluidSlotListener
import com.the9grounds.aeadditions.inventory.CraftingUpgradeInventory
import com.the9grounds.aeadditions.inventory.IInventoryListener
import com.the9grounds.aeadditions.network.IGuiProvider
import com.the9grounds.aeadditions.network.packet.PacketFluidFillerSlotUpdate
import com.the9grounds.aeadditions.registries.BlockEnum
import com.the9grounds.aeadditions.util.*
import net.minecraft.init.Items
import net.minecraft.inventory.Container
import net.minecraft.inventory.IInventory
import net.minecraft.util.ITickable
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fml.relauncher.Side
import java.util.ArrayList
import java.util.HashMap

class TileEntityFluidFiller : TileBase(), IActionHost, ICraftingProvider, IECTileEntity,
    IMEMonitorHandlerReceiver<IAEFluidStack>, IListenerTile, ITickable, IGuiProvider, IInventoryListener, IFluidSlotListener, IUpgradeable {
    private val gridBlock: ECFluidGridBlock
    private var node: IGridNode? = null
    var fluids: MutableList<Fluid> = ArrayList()

    var beingBroken = false
    var speedState = 0
    var selectedFluid: Fluid? = null
    var upgradeInventory = object : CraftingUpgradeInventory(this, BlockEnum.FLUIDCRAFTER, 3) {
        override fun onContentsChanged() {
            saveData()
            super.onContentsChanged()
        }
    }

    @JvmField
	var containerItem: ItemStack? = ItemStack(Items.BUCKET)
    var returnStack: ItemStack? = null
    var ticksToFinish = 0
    private var isFirstGetGridNode = true
    private val encodedPattern = AEApi.instance().definitions().items().encodedPattern().maybeItem().orElse(null)
    @MENetworkEventSubscribe
    fun cellUpdate(event: MENetworkCellArrayUpdate?) {
        val storage = storageGrid
        if (storage != null) {
            postChange(storage.getInventory(StorageChannels.FLUID), null, null)
        }
    }

    override fun getActionableNode(): IGridNode {
        if (FMLCommonHandler.instance().effectiveSide.isClient) {
            return node!!
        }
        if (node == null) {
            node = AEApi.instance().grid().createGridNode(gridBlock)
        }
        return node!!
    }

    override fun getCableConnectionType(dir: AEPartLocation): AECableType {
        return AECableType.DENSE_SMART
    }

    override fun getUpdateTag(): NBTTagCompound {
        return writeToNBT(NBTTagCompound())
    }

    override fun getGridNode(location: AEPartLocation): IGridNode? {
        if (FMLCommonHandler.instance().side.isClient
            && (world == null || world.isRemote)
        ) {
            return null
        }
        if (isFirstGetGridNode) {
            isFirstGetGridNode = false
            actionableNode.updateState()
            val storage = storageGrid
            storage!!.getInventory(StorageChannels.FLUID).addListener(this, null)
        }
        return node
    }

    override fun getLocation(): DimensionalCoord {
        return DimensionalCoord(this)
    }

    private fun getPattern(
        emptyContainer: ItemStack?,
        filledContainer: ItemStack?
    ): ItemStack {
        val `in` = NBTTagList()
        val out = NBTTagList()
        `in`.appendTag(emptyContainer!!.writeToNBT(NBTTagCompound()))
        out.appendTag(filledContainer!!.writeToNBT(NBTTagCompound()))
        val itemTag = NBTTagCompound()
        itemTag.setTag("in", `in`)
        itemTag.setTag("out", out)
        itemTag.setBoolean("crafting", false)
        val pattern = ItemStack(encodedPattern)
        pattern.tagCompound = itemTag
        return pattern
    }

    override fun getPowerUsage(): Double {
        return 1.0
    }

    private val storageGrid: IStorageGrid?
        private get() {
            node = getGridNode(AEPartLocation.INTERNAL)
            if (node == null) {
                return null
            }
            val grid = node!!.grid ?: return null
            return grid.getCache(IStorageGrid::class.java)
        }

    override fun isBusy(): Boolean {
        return returnStack != null && !returnStack!!.isEmpty
    }

    override fun isValid(verificationToken: Any?): Boolean {
        return true
    }

    override fun onListUpdate() {}
    override fun postChange(
        monitor: IBaseMonitor<IAEFluidStack>,
        change: Iterable<IAEFluidStack>?,
        actionSource: IActionSource?
    ) {
        val oldFluids: MutableList<Fluid> = ArrayList(fluids)
        var mustUpdate = false
        fluids.clear()
        for (fluid in (monitor as IMEMonitor<IAEFluidStack>)
            .storageList) {
            if (!oldFluids.contains(fluid.fluid)) {
                mustUpdate = true
            } else {
                oldFluids.remove(fluid.fluid)
            }
            fluids.add(fluid.fluid)
        }
        if (!(oldFluids.isEmpty() && !mustUpdate)) {
            if (getGridNode(AEPartLocation.INTERNAL) != null
                && getGridNode(AEPartLocation.INTERNAL)!!.grid != null
            ) {
                getGridNode(AEPartLocation.INTERNAL)!!.grid.postEvent(
                    MENetworkCraftingPatternChange(
                        this,
                        getGridNode(AEPartLocation.INTERNAL)
                    )
                )
            }
        }
    }

    fun postUpdateEvent() {
        if (getGridNode(AEPartLocation.INTERNAL) != null
            && getGridNode(AEPartLocation.INTERNAL)!!.grid != null
        ) {
            getGridNode(AEPartLocation.INTERNAL)!!.grid.postEvent(
                MENetworkCraftingPatternChange(
                    this,
                    getGridNode(AEPartLocation.INTERNAL)
                )
            )
        }
    }

    @MENetworkEventSubscribe
    fun powerUpdate(event: MENetworkPowerStatusChange?) {
        val storage = storageGrid
        if (storage != null) {
            postChange(storage.getInventory(StorageChannels.FLUID), null, null)
        }
    }

    var patternFluids = HashMap<ICraftingPatternDetails, FluidStack?>()
    override fun provideCrafting(craftingTracker: ICraftingProviderHelper) {
        patternFluids.clear()
        if (selectedFluid == null) {
            return
        }
        val fluidStack = FluidStack(selectedFluid, 1)

        val fluid = fluidStack.fluid ?: return
        val maxCapacity = FluidHelper.getCapacity(containerItem)
        if (maxCapacity == 0) {
            return
        }
        val filled = FluidHelper.fillStack(
            containerItem!!.copy(), FluidStack(
                fluid,
                maxCapacity
            )
        )
        if (filled.right == null) {
            return
        }
        val pattern = getPattern(containerItem, filled.right)
        val patter = pattern
            .item as ICraftingPatternItem
        val details = patter.getPatternForItem(pattern, world) ?: return
        patternFluids[details] = FluidStack(fluid, filled.left)
        craftingTracker.addCraftingOption(this, details)
    }

    // This is that if a player breaks the block, we finish crafting so it's not stuck in limbo
    fun finishCrafting() {
        beingBroken = true
        if (returnStack != null && !returnStack!!.isEmpty) {
            injectCraftedItems()
        }
    }

    override fun pushPattern(patternDetails: ICraftingPatternDetails, table: InventoryCrafting): Boolean {
        if ((returnStack != null && !returnStack!!.isEmpty) || beingBroken) {
            return false
        }
        val filled = patternDetails.condensedOutputs[0].definition
        if (!patternFluids.containsKey(patternDetails)) return false
        val fluid = patternFluids[patternDetails]
        val storage = storageGrid
        if (storage == null || fluid == null) {
            return false
        }
        val fluidStack = AEUtils.createFluidStack(
            FluidStack(
                fluid.fluid,
                FluidHelper.getCapacity(patternDetails.condensedInputs[0].definition)
            )
        )
        val extracted = storage.getInventory(StorageChannels.FLUID)
            .extractItems(fluidStack.copy(), Actionable.SIMULATE, MachineSource(this))
        if (extracted == null || extracted.stackSize != fluidStack.stackSize) {
            return false
        }
        storage.getInventory(StorageChannels.FLUID).extractItems(fluidStack, Actionable.MODULATE, MachineSource(this))
        returnStack = filled
        ticksToFinish = 40 - (speedState * 12)
        return true
    }

    override fun readFromNBT(tagCompound: NBTTagCompound) {
        super.readFromNBT(tagCompound)
        if (tagCompound.hasKey("container")) {
            containerItem = ItemStack(
                tagCompound
                    .getCompoundTag("container")
            )
        } else if (tagCompound.hasKey("isContainerEmpty")
            && tagCompound.getBoolean("isContainerEmpty")
        ) {
            containerItem = null
        }

        if (tagCompound.hasKey("selectedFluid")) {
            selectedFluid = FluidRegistry.getFluid(tagCompound.getString("selectedFluid"))
        }

        if (tagCompound.hasKey("return")) {
            returnStack = ItemStack(
                tagCompound
                    .getCompoundTag("return")
            )
        } else if (tagCompound.hasKey("isReturnEmpty")
            && tagCompound.getBoolean("isReturnEmpty")
        ) {
            returnStack = null
        }
        upgradeInventory.readFromNBT(
            tagCompound.getTagList(
                "upgradeInventory",
                10
            )
        )
        onInventoryChanged()
        if (tagCompound.hasKey("time")) {
            ticksToFinish = tagCompound.getInteger("time")
        }
        if (hasWorld()) {
            val node = getGridNode(AEPartLocation.INTERNAL)
            if (tagCompound.hasKey("nodes") && node != null) {
                node.loadFromNBT("node0", tagCompound.getCompoundTag("nodes"))
                node.updateState()
            }
        }
    }

    override fun registerListener() {
        val storage = storageGrid ?: return
        val fluidInventory = storage.getInventory(StorageChannels.FLUID)
        postChange(fluidInventory, null, null)
        fluidInventory.addListener(this, null)
    }

    override fun removeListener() {
        val storage = storageGrid ?: return
        val fluidInventory = storage.getInventory(StorageChannels.FLUID)
        fluidInventory.removeListener(this)
    }

    override fun securityBreak() {
        //TODO: Find out what func_147480_a is
        /*if (this.getWorldObj() != null)
			getWorldObj().func_147480_a(this.xCoord, this.yCoord, this.zCoord, true);*/
    }

    override fun update() {
        if (!hasWorld()) {
            return
        }
        if (ticksToFinish > 0) {
            ticksToFinish = ticksToFinish - 1
        }
        if (ticksToFinish <= 0 && returnStack != null && !returnStack!!.isEmpty) {
            injectCraftedItems()
        }
    }

    private fun injectCraftedItems() {
        val storage = storageGrid ?: return
        val toInject = StorageChannels.ITEM.createStack(returnStack!!)
        if (storage.getInventory(StorageChannels.ITEM).canAccept(toInject!!.copy())) {
            val nodAdded = storage.getInventory(StorageChannels.ITEM).injectItems(
                toInject.copy(), Actionable.SIMULATE,
                MachineSource(this)
            )
            if (nodAdded == null) {
                storage.getInventory(StorageChannels.ITEM).injectItems(
                    toInject,
                    Actionable.MODULATE, MachineSource(this)
                )
                returnStack = null
            }
        }
    }

    override fun updateGrid(oldGrid: IGrid, newGrid: IGrid) {
        if (oldGrid != null) {
            val storage = oldGrid.getCache<IStorageGrid>(IStorageGrid::class.java)
            if (storage != null) {
                storage.getInventory(StorageChannels.FLUID).removeListener(this)
            }
        }
        if (newGrid != null) {
            val storage = newGrid.getCache<IStorageGrid>(IStorageGrid::class.java)
            if (storage != null) {
                storage.getInventory(StorageChannels.FLUID).addListener(this, null)
            }
        }
    }

    override fun writeToNBT(tagCompound: NBTTagCompound): NBTTagCompound {
        super.writeToNBT(tagCompound)
        if (containerItem != null) {
            tagCompound.setTag("container", containerItem!!.writeToNBT(NBTTagCompound()))
        } else {
            tagCompound.setBoolean("isContainerEmpty", true)
        }

        if (selectedFluid != null) {
            tagCompound.setString("selectedFluid", selectedFluid!!.name)
        } else {
            tagCompound.removeTag("selectedFluid")
        }

        tagCompound.setTag("upgradeInventory", upgradeInventory.writeToNBT())

        if (returnStack != null && !returnStack!!.isEmpty) {
            tagCompound.setTag("return", returnStack!!.writeToNBT(NBTTagCompound()))
        } else {
            tagCompound.setBoolean("isReturnEmpty", true)
        }
        tagCompound.setInteger("time", ticksToFinish)
        if (!hasWorld()) {
            return tagCompound
        }
        val node = getGridNode(AEPartLocation.INTERNAL)
        if (node != null) {
            val nodeTag = NBTTagCompound()
            node.saveToNBT("node0", nodeTag)
            tagCompound.setTag("nodes", nodeTag)
        }
        return tagCompound
    }

    @SideOnly(Side.CLIENT)
    override fun getClientGuiElement(player: EntityPlayer, vararg args: Any): GuiContainer {
        return GuiFluidFiller(player, this)
    }

    override fun getServerGuiElement(player: EntityPlayer, vararg args: Any): Container {
        return ContainerFluidFiller(player.inventory, this)
    }

    init {
        gridBlock = ECFluidGridBlock(this)
    }

    override fun onInventoryChanged() {
        speedState = 0
        for (i in 0 until upgradeInventory.sizeInventory) {
            val currentStack = upgradeInventory.getStackInSlot(i)
            if (currentStack != null) {
                if (AEApi.instance().definitions().materials().cardSpeed().isSameAs(currentStack)) {
                    speedState++
                }
            }
        }

        saveData()
    }

    override fun setFluid(index: Int, fluid: Fluid?, player: EntityPlayer?) {
        this.selectedFluid = fluid

        if (hasWorld()) {
            updateBlock()
        }

        postUpdateEvent()

        NetworkUtil.sendToPlayer(PacketFluidFillerSlotUpdate(this.selectedFluid), player)
        saveData()
    }

    override fun getUpgradeInventory(): IInventory {
        return upgradeInventory
    }

    fun syncClientGui(player: EntityPlayer) {
        NetworkUtil.sendToPlayer(PacketFluidFillerSlotUpdate(this.selectedFluid), player)
    }


}