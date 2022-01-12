package com.the9grounds.aeadditions.tileentity

import appeng.api.networking.security.IActionHost
import appeng.api.networking.crafting.ICraftingProvider
import appeng.api.networking.crafting.ICraftingWatcherHost
import com.the9grounds.aeadditions.api.IECTileEntity
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.entity.player.EntityPlayer
import appeng.api.implementations.ICraftingPatternItem
import appeng.api.networking.crafting.ICraftingPatternDetails
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextComponentString
import com.the9grounds.aeadditions.gridblock.ECFluidGridBlock
import appeng.api.networking.IGridNode
import appeng.api.storage.data.IAEItemStack
import appeng.api.networking.crafting.ICraftingWatcher
import net.minecraftforge.fml.common.FMLCommonHandler
import appeng.api.AEApi
import appeng.api.util.AEPartLocation
import appeng.api.util.AECableType
import appeng.api.util.DimensionalCoord
import appeng.api.networking.crafting.ICraftingGrid
import appeng.api.networking.crafting.ICraftingProviderHelper
import com.the9grounds.aeadditions.util.ItemStackUtils
import com.the9grounds.aeadditions.crafting.CraftingPattern
import net.minecraft.inventory.InventoryCrafting
import appeng.api.networking.IGrid
import appeng.api.networking.storage.IStorageGrid
import com.the9grounds.aeadditions.util.StorageChannels
import net.minecraftforge.fluids.FluidStack
import appeng.api.config.Actionable
import appeng.api.networking.events.MENetworkCraftingPatternChange
import appeng.core.AELog
import com.the9grounds.aeadditions.api.inventory.IToggleableSlotsInventory
import com.the9grounds.aeadditions.container.IUpgradeable
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraft.client.gui.inventory.GuiContainer
import com.the9grounds.aeadditions.gui.fluid.GuiFluidCrafter
import com.the9grounds.aeadditions.container.fluid.ContainerFluidCrafter
import com.the9grounds.aeadditions.inventory.CraftingUpgradeInventory
import com.the9grounds.aeadditions.inventory.IInventoryListener
import com.the9grounds.aeadditions.network.IGuiProvider
import com.the9grounds.aeadditions.network.packet.PacketCrafterCapacity
import com.the9grounds.aeadditions.network.packet.PacketCrafterDroppedItem
import com.the9grounds.aeadditions.registries.BlockEnum
import com.the9grounds.aeadditions.util.MachineSource
import com.the9grounds.aeadditions.util.NetworkUtil
import net.minecraft.entity.item.EntityItem
import net.minecraft.inventory.Container
import net.minecraft.util.ITickable
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fml.relauncher.Side
import java.util.*

class TileEntityFluidCrafter : TileBase(), IActionHost, ICraftingProvider, ICraftingWatcherHost, IECTileEntity,
    ITickable, IGuiProvider, IInventoryListener, IUpgradeable {

    var speedState: Int = 0
    var capacity: Int = 0

    val inventoryOrder = listOf<List<Int>>(
        listOf(4),
        listOf(4,1,3,5,7),
        listOf(4,1,3,5,7,0,2,6,8)
    )

    val inventoryOrderSeparated = listOf<List<Int>>(
        listOf(4),
        listOf(1,3,5,7),
        listOf(0,2,6,8)
    )

    val filterOrder = listOf(4,1,3,5,7,0,2,6,8)
    
    val craftingList = mutableListOf<ICraftingPatternDetails>()

    val upgradeInventory = object : CraftingUpgradeInventory(this, BlockEnum.FLUIDCRAFTER) {
        override fun onContentsChanged() {
            saveData()
            super.onContentsChanged()
        }
    }

    private val gridBlock: ECFluidGridBlock
    private var node: IGridNode? = null
    private var requestedItems: MutableList<IAEItemStack> = ArrayList()
    private val removeList: MutableList<IAEItemStack> = ArrayList()
    private val oldStack = arrayOfNulls<ItemStack>(9)
    private var isBusy = false
    private var watcher: ICraftingWatcher? = null
    private var isFirstGetGridNode = true
    @JvmField val inventory: FluidCrafterInventory
    private var finishCraftingTime = 0L
    private var returnStack: ItemStack? = null
    private var optionalReturnStack = arrayOfNulls<ItemStack>(0)
    private var update = false
    private val instance: TileEntityFluidCrafter
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
        return AECableType.SMART
    }

    val gridNode: IGridNode?
        get() = getGridNode(AEPartLocation.INTERNAL)

    override fun getGridNode(dir: AEPartLocation): IGridNode? {
        if (FMLCommonHandler.instance().side.isClient && (getWorld() == null || getWorld().isRemote)) {
            return null
        }
        if (isFirstGetGridNode) {
            isFirstGetGridNode = false
            actionableNode!!.updateState()
        }
        return node
    }

    fun getInventory(): IInventory {
        return inventory
    }

    override fun getLocation(): DimensionalCoord {
        return DimensionalCoord(this)
    }

    override fun getPowerUsage(): Double {
        return 0.0
    }

    override fun isBusy(): Boolean {
        return isBusy
    }

    override fun onRequestChange(craftingGrid: ICraftingGrid, what: IAEItemStack) {
        if (craftingGrid.isRequesting(what)) {
            if (!requestedItems.contains(what)) {
                requestedItems.add(what)
            }
        } else if (requestedItems.contains(what)) {
            requestedItems.remove(what)
        }
    }

    override fun provideCrafting(craftingTracker: ICraftingProviderHelper) {
        
        craftingList.forEach {
            if (it.condensedInputs.size == 0) {
                craftingTracker.setEmitable(it.condensedOutputs[0])
            } else {
                craftingTracker.addCraftingOption(this, it)
            }
        }
        updateWatcher()
    }

    override fun pushPattern(
        patternDetails: ICraftingPatternDetails,
        table: InventoryCrafting
    ): Boolean {
        if (isBusy) {
            return false
        }
        if (patternDetails is CraftingPattern) {
            val patter = patternDetails
            val fluids = HashMap<Fluid, Long?>()
            for (stack in patter.condensedFluidInputs) {
                if (fluids.containsKey(stack.fluid)) {
                    val amount = fluids[stack.fluid]!! + stack.stackSize
                    fluids.remove(stack.fluid)
                    fluids[stack.fluid] = amount
                } else {
                    fluids[stack.fluid] = stack.stackSize
                }
            }
            val grid = node!!.grid ?: return false
            val storage = grid.getCache<IStorageGrid>(IStorageGrid::class.java) ?: return false
            for (fluid in fluids.keys) {
                val amount = fluids[fluid]
                val extractFluid = storage.getInventory(StorageChannels.FLUID).extractItems(
                    StorageChannels.FLUID.createStack(FluidStack(fluid, (amount!! + 0).toInt())),
                    Actionable.SIMULATE,
                    MachineSource(this)
                )
                if (extractFluid == null || extractFluid.stackSize != amount) {
                    return false
                }
            }
            for (fluid in fluids.keys) {
                val amount = fluids[fluid]
                val extractFluid = storage.getInventory(StorageChannels.FLUID).extractItems(
                    StorageChannels.FLUID.createStack(FluidStack(fluid, (amount!! + 0).toInt())),
                    Actionable.MODULATE,
                    MachineSource(this)
                )
            }
            finishCraftingTime = System.currentTimeMillis() + 1000 - (speedState * 175)
            returnStack = patter.getOutput(table, getWorld())
            optionalReturnStack = arrayOfNulls(9)
            for (i in 0..8) {
                val s = table.getStackInSlot(i)
                if (s != null && s.item != null) {
                    optionalReturnStack[i] = s.item.getContainerItem(s.copy())
                }
            }
            isBusy = true
        }
        return true
    }

    override fun onLoad() {
        super.onLoad()
        onInventoryChanged()
    }

    override fun readFromNBT(tagCompound: NBTTagCompound) {
        super.readFromNBT(tagCompound)
        inventory.readFromNBT(tagCompound)
        if (hasWorld()) {
            val node = gridNode
            if (tagCompound.hasKey("nodes") && node != null) {
                node.loadFromNBT("node0", tagCompound.getCompoundTag("nodes"))
                node.updateState()
            }
        }

        upgradeInventory.readFromNBT(
            tagCompound.getTagList(
                "upgradeInventory",
                10
            )
        )
        onInventoryChanged()
    }

    override fun securityBreak() {}
    override fun update() {
        if (getWorld() == null || getWorld().provider == null) {
            return
        }
        if (update) {
            update = false
            if (gridNode != null && gridNode!!.grid != null) {
                gridNode!!.grid.postEvent(MENetworkCraftingPatternChange(instance, gridNode))
            }
        }
        if (isBusy && finishCraftingTime <= System.currentTimeMillis() && getWorld() != null && !getWorld().isRemote) {
            if (node == null || returnStack == null) {
                return
            }
            val grid = node!!.grid ?: return
            val storage = grid.getCache<IStorageGrid>(IStorageGrid::class.java) ?: return
            storage.getInventory(StorageChannels.ITEM)
                .injectItems(StorageChannels.ITEM.createStack(returnStack!!), Actionable.MODULATE, MachineSource(this))
            for (s in optionalReturnStack) {
                if (s == null || s.isEmpty) {
                    continue
                }
                storage.getInventory(StorageChannels.ITEM)
                    .injectItems(StorageChannels.ITEM.createStack(s), Actionable.MODULATE, MachineSource(this))
            }
            optionalReturnStack = arrayOfNulls(0)
            isBusy = false
            returnStack = null
            markDirty()
        }
        if (!isBusy && getWorld() != null && !getWorld().isRemote) {
            for (stack in removeList) {
                requestedItems.remove(stack)
            }
            removeList.clear()
            if (!requestedItems.isEmpty()) {
                for (s in requestedItems) {
                    val grid = node!!.grid ?: break
                    val crafting = grid.getCache<ICraftingGrid>(ICraftingGrid::class.java) ?: break
                    if (!crafting.isRequesting(s)) {
                        removeList.add(s)
                        continue
                    }
                    for (details in craftingList) {
                        if (details.condensedOutputs[0] == s) {
                            val patter = details as CraftingPattern
                            val fluids = HashMap<Fluid, Long?>()
                            for (stack in patter.condensedFluidInputs) {
                                if (fluids.containsKey(stack.fluid)) {
                                    val amount = fluids[stack.fluid]!! + stack.stackSize
                                    fluids.remove(stack.fluid)
                                    fluids[stack.fluid] = amount
                                } else {
                                    fluids[stack.fluid] = stack.stackSize
                                }
                            }
                            val storage = grid.getCache<IStorageGrid>(IStorageGrid::class.java) ?: break
                            var doBreak = false
                            for (fluid in fluids.keys) {
                                val amount = fluids[fluid]
                                val extractFluid = storage.getInventory(StorageChannels.FLUID).extractItems(
                                    StorageChannels.FLUID.createStack(
                                        FluidStack(
                                            fluid,
                                            (amount!! + 0).toInt()
                                        )
                                    ), Actionable.SIMULATE, MachineSource(this)
                                )
                                if (extractFluid == null || extractFluid.stackSize != amount) {
                                    doBreak = true
                                    break
                                }
                            }
                            if (doBreak) {
                                break
                            }
                            for (fluid in fluids.keys) {
                                val amount = fluids[fluid]
                                val extractFluid = storage.getInventory(StorageChannels.FLUID).extractItems(
                                    StorageChannels.FLUID.createStack(
                                        FluidStack(
                                            fluid,
                                            (amount!! + 0).toInt()
                                        )
                                    ), Actionable.MODULATE, MachineSource(this)
                                )
                            }
                            finishCraftingTime = System.currentTimeMillis() + 1000 - (speedState * 175)
                            returnStack = patter.condensedOutputs[0].createItemStack()
                            isBusy = true
                            markDirty()
                            return
                        }
                    }
                }
            }
        }
    }

    private fun updateWatcher() {
        requestedItems = ArrayList()
        var grid: IGrid? = null
        val node = gridNode
        var crafting: ICraftingGrid? = null
        if (node != null) {
            grid = node.grid
            if (grid != null) {
                crafting = grid.getCache(ICraftingGrid::class.java)
            }
        }
        for (patter in craftingList) {
            watcher!!.reset()
            if (patter.condensedInputs.size == 0) {
                watcher!!.add(patter.condensedOutputs[0])
                if (crafting != null) {
                    if (crafting.isRequesting(patter.condensedOutputs[0])) {
                        requestedItems
                            .add(patter.condensedOutputs[0])
                    }
                }
            }
        }
    }

    override fun updateWatcher(newWatcher: ICraftingWatcher) {
        watcher = newWatcher
        updateWatcher()
    }

    override fun writeToNBT(tagCompound: NBTTagCompound): NBTTagCompound {
        super.writeToNBT(tagCompound)
        inventory.writeToNBT(tagCompound)
        tagCompound.setTag("upgradeInventory", upgradeInventory.writeToNBT())
        if (!hasWorld()) {
            return tagCompound
        }
        val node = gridNode
        if (node != null) {
            val nodeTag = NBTTagCompound()
            node.saveToNBT("node0", nodeTag)
            tagCompound.setTag("nodes", nodeTag)
        }
        return tagCompound
    }

    @SideOnly(Side.CLIENT)
    override fun getClientGuiElement(player: EntityPlayer, vararg args: Any): GuiContainer {
        return GuiFluidCrafter(player.inventory, this)
    }

    override fun getServerGuiElement(player: EntityPlayer, vararg args: Any): Container {
        return ContainerFluidCrafter(player.inventory, this)
    }

    override fun getUpgradeInventory(): IInventory {
        return upgradeInventory
    }

    override fun onInventoryChanged() {
        if (!hasWorld()) {
            return
        }
        
        val oldCapacity = capacity
        speedState = 0
        capacity = 0
        for (i in 0 until upgradeInventory.sizeInventory) {
            val currentStack = upgradeInventory.getStackInSlot(i)
            if (currentStack != null) {
                if (AEApi.instance().definitions().materials().cardSpeed().isSameAs(currentStack)) {
                    speedState++
                }
                if (AEApi.instance().definitions().materials().cardCapacity().isSameAs(currentStack)) {
                    capacity++
                }
            }
        }

        if (capacity < oldCapacity) {
            val indexes = mutableListOf<Int>()
            // Drop other Patterns
            if (oldCapacity == 2 && capacity == 1) {
                indexes.addAll(inventoryOrderSeparated[2])
            }

            if (oldCapacity == 2 && capacity == 0) {
                indexes.addAll(inventoryOrderSeparated[1])
            }

            if (oldCapacity == 1 && capacity == 0) {
                indexes.addAll(inventoryOrderSeparated[1])
            }

            indexes.forEach{
                dropItem(it)
            }
        }

        inventory.enabledSlots.forEach { k,v ->
            if (k != 4) {
                inventory.enabledSlots[k] = false
            }
        }

        when(capacity) {
            0 -> {
                inventoryOrder[capacity].forEach{
                    if (!inventory.enabledSlots.containsKey(it)) {
                        inventory.enabledSlots[it] = true
                    }

                    inventory.enabledSlots[it] = true
                }
            }
            1 -> {
                inventoryOrder[capacity].forEach{
                    if (!inventory.enabledSlots.containsKey(it)) {
                        inventory.enabledSlots[it] = true
                    }

                    inventory.enabledSlots[it] = true
                }
            }
            2 -> {
                inventoryOrder[capacity].forEach{
                    if (!inventory.enabledSlots.containsKey(it)) {
                        inventory.enabledSlots[it] = true
                    }

                    inventory.enabledSlots[it] = true
                }
            }
        }

        if (getGridNode(AEPartLocation.INTERNAL) == null) {
            return
        }


        val coord = location
        if (coord == null || coord.world == null || coord.world.isRemote) {
            return
        }

        updateCraftingList()

        NetworkUtil.sendNetworkPacket(PacketCrafterCapacity(this, capacity), coord.pos, coord.world)

        saveData()
    }
    
    fun updateCraftingList() {

        if (getGridNode(AEPartLocation.INTERNAL) == null || !hasWorld()) {
            return
        }

        this.craftingList.clear()

        inventory.inv.forEach {
            if (!ItemStackUtils.isEmpty(it) && it!!.item !== null && it!!.item is ICraftingPatternItem) {
                val pattern = CraftingPattern((it.item as ICraftingPatternItem).getPatternForItem(it, getWorld()))

                craftingList.add(pattern)
            }
        }

        try {
            getGridNode(AEPartLocation.INTERNAL)!!.grid.postEvent(
                MENetworkCraftingPatternChange(
                    this,
                    getGridNode(AEPartLocation.INTERNAL)
                )
            )
        } catch (e: Throwable) {
            AELog.error(e)
        }
    }

    init {
        gridBlock = ECFluidGridBlock(this)
        inventory = FluidCrafterInventory()
        instance = this
    }

    fun removeSlot(index: Int) {
        val item = inventory.getStackInSlot(index)
        if (item != null && item.count > 0) {
            item.count = 0
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

    private fun dropItem(index: Int) {
        val rand = Random()
        val item = inventory.getStackInSlot(index)
        if (item != null && item.count > 0) {
            if (world.isRemote) {
                return
            }
            val rx = rand.nextFloat() * 0.8f + 0.1f
            val ry = rand.nextFloat() * 0.8f + 0.1f
            val rz = rand.nextFloat() * 0.8f + 0.1f
            val entityItem = EntityItem(
                world, (pos.x + rx).toDouble(), (pos.y + ry).toDouble(), (pos.z
                        + rz).toDouble(), item.copy()
            )
            if (item.hasTagCompound()) {
                entityItem.item.tagCompound = item.tagCompound!!.copy()
            }
            val factor = 0.05f
            entityItem.motionX = rand.nextGaussian() * factor
            entityItem.motionY = rand.nextGaussian() * factor + 0.2f
            entityItem.motionZ = rand.nextGaussian() * factor
            world.spawnEntity(entityItem)
            item.count = 0

            NetworkUtil.sendNetworkPacket(PacketCrafterDroppedItem(this, index), pos, world)
        }
    }

    inner class FluidCrafterInventory : IToggleableSlotsInventory {
        val inv = arrayOfNulls<ItemStack>(9)
        override val enabledSlots = mutableMapOf<Int, Boolean>(
            4 to true
        )
        override fun closeInventory(player: EntityPlayer) {}
        override fun decrStackSize(slot: Int, amt: Int): ItemStack? {
            var stack = getStackInSlot(slot)
            if (stack != null && !stack.isEmpty) {
                if (stack.count <= amt) {
                    setInventorySlotContents(slot, ItemStack.EMPTY)
                } else {
                    stack = stack.splitStack(amt)
                    if (stack.count == 0) {
                        setInventorySlotContents(slot, ItemStack.EMPTY)
                    }
                }
            }
            update = true
            onContentsChanged()
            return stack
        }

        override fun getName(): String {
            return "inventory.fluidCrafter"
        }

        override fun getInventoryStackLimit(): Int {
            return 1
        }

        override fun getSizeInventory(): Int {
            return inv.size
        }

        override fun isEmpty(): Boolean {
            for (i in inv.indices) {
                if (inv[i] != null && !inv[i]!!.isEmpty) return true
            }
            return false
        }

        override fun getStackInSlot(slot: Int): ItemStack? {
            return inv[slot]
        }

        override fun removeStackFromSlot(index: Int): ItemStack? {
            return ItemStack.EMPTY
        }

        override fun hasCustomName(): Boolean {
            return false
        }

        override fun isItemValidForSlot(slot: Int, stack: ItemStack): Boolean {
            if (stack.item is ICraftingPatternItem) {
                val details = (stack
                    .item as ICraftingPatternItem).getPatternForItem(stack, getWorld())
                return details != null && details.isCraftable
            }
            return false
        }

        override fun isUsableByPlayer(player: EntityPlayer): Boolean {
            return true
        }

        override fun markDirty() {}
        override fun openInventory(player: EntityPlayer) {}
        fun readFromNBT(tagCompound: NBTTagCompound) {
            val tagList = tagCompound.getTagList("Inventory", 10)
            for (i in 0 until tagList.tagCount()) {
                val tag = tagList.getCompoundTagAt(i)
                val slot = tag.getByte("Slot")
                if (slot >= 0 && slot < inv.size) {
                    inv[slot.toInt()] = ItemStack(tag)
                }
            }
        }

        override fun setInventorySlotContents(slot: Int, stack: ItemStack) {
            inv[slot] = stack
            if (stack != null && stack.count > inventoryStackLimit) {
                stack.count = inventoryStackLimit
            }
            onContentsChanged()
            update = true
        }

        fun writeToNBT(tagCompound: NBTTagCompound) {
            val itemList = NBTTagList()
            for (i in inv.indices) {
                val stack = inv[i]
                if (stack != null) {
                    val tag = NBTTagCompound()
                    tag.setByte("Slot", i.toByte())
                    stack.writeToNBT(tag)
                    itemList.appendTag(tag)
                }
            }
            tagCompound.setTag("Inventory", itemList)
        }

        override fun getField(id: Int): Int {
            return 0
        }

        override fun setField(id: Int, value: Int) {}
        override fun getFieldCount(): Int {
            return 0
        }

        override fun clear() {}
        override fun getDisplayName(): ITextComponent {
            return TextComponentString(name)
        }

        protected fun onContentsChanged() {
            saveData()
            onInventoryChanged()
            if (hasWorld()) {
                updateBlock()
            }

            postUpdateEvent()
        }

        init {
            for (i in inv.indices) {
                inv[i] = ItemStack.EMPTY
            }
        }
    }
}