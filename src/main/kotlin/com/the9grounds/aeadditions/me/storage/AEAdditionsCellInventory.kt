package com.the9grounds.aeadditions.me.storage

import appeng.api.config.Actionable
import appeng.api.config.FuzzyMode
import appeng.api.config.IncludeExclude
import appeng.api.ids.AEComponents
import appeng.api.networking.security.IActionSource
import appeng.api.stacks.AEItemKey
import appeng.api.stacks.AEKey
import appeng.api.stacks.GenericStack
import appeng.api.stacks.KeyCounter
import appeng.api.storage.cells.CellState
import appeng.api.storage.cells.ISaveProvider
import appeng.api.storage.cells.StorageCell
import appeng.api.upgrades.IUpgradeInventory
import appeng.core.definitions.AEItems
import appeng.util.ConfigInventory
import appeng.util.prioritylist.FuzzyPriorityList
import appeng.util.prioritylist.IPartitionList
import com.the9grounds.aeadditions.api.IAEAdditionsStorageCell
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

class AEAdditionsCellInventory(val cellType: com.the9grounds.aeadditions.item.storage.StorageCell, val itemStackLocal: ItemStack, val container: ISaveProvider?) : StorageCell {

    private var maxItemTypes = MAX_ITEM_TYPES
    private var storedItems: Int
    private var storedItemCount = 0L
    var partitionList: IPartitionList? = null
        private set
    var partitionListMode: IncludeExclude? = null
    private set
    private var maxItemsPerType: Long = 0
    private var hasVoidUpgrade = false
    val keyType = cellType!!.getKeyType()
    protected var cellItems: MutableMap<AEKey, Long>? = null
    get() {
        if (field == null) {
            cellItems = Object2LongOpenHashMap()
            loadCellItems()
        }
        return field
    }
    protected var itemsPerByte = 0
    private var isPersisted = true

    companion object
    {
        private val MAX_ITEM_TYPES = 300
        
        private fun getStorageCell(input: ItemStack?): IAEAdditionsStorageCell? {
            if (input != null && input.item is com.the9grounds.aeadditions.item.storage.StorageCell) {
                return input.item as com.the9grounds.aeadditions.item.storage.StorageCell
            }
            
            return null
        }
        
        private fun getStorageCell(itemKey: AEItemKey): IAEAdditionsStorageCell? {
            if (itemKey.item is com.the9grounds.aeadditions.item.storage.StorageCell) {
                return itemKey.item as com.the9grounds.aeadditions.item.storage.StorageCell
            }
            
            return null
        }
        
        private fun isCellEmpty(cellInventory: AEAdditionsCellInventory?): Boolean {
            if (cellInventory == null) {
                return true
            }
            return cellInventory.availableStacks.isEmpty
        }
        
        fun isCell(itemStack: ItemStack?): Boolean {
            return getStorageCell(itemStack) !== null
        }
        
        fun createInventory(itemStack: ItemStack, container: ISaveProvider?): AEAdditionsCellInventory? {
            val item = itemStack.item
            if (!(item is com.the9grounds.aeadditions.item.storage.StorageCell)) {
                return null
            }
            
            if (!item.isStorageCell(itemStack)) {
                return null
            }
            
            return AEAdditionsCellInventory(item, itemStack, container)
        }
    }

    init {
        itemsPerByte = keyType!!.amountPerByte
        maxItemTypes = this.cellType!!.getTotalTypes(itemStackLocal)
        if (maxItemTypes > MAX_ITEM_TYPES) {
            maxItemTypes = MAX_ITEM_TYPES
        }
        if (maxItemTypes < 1) {
            maxItemTypes = 1
        }
        val storedStacks = storedStacks
        storedItems = storedStacks.size
        storedItemCount = storedStacks.stream().mapToLong(GenericStack::amount).sum()
        cellItems = null


        // Updates the partition list and mode based on installed upgrades and the configured filter.
        val builder = IPartitionList.builder()

        val upgrades = getUpgradesInventory()
        val config = getConfigInventory()

        val hasInverter = upgrades!!.isInstalled(AEItems.INVERTER_CARD)
        val isFuzzy = upgrades.isInstalled(AEItems.FUZZY_CARD)
        if (isFuzzy) {
            builder.fuzzyMode(getFuzzyMode())
        }

        builder.addAll(config!!.keySet())

        partitionListMode = if (hasInverter) IncludeExclude.BLACKLIST else IncludeExclude.WHITELIST
        partitionList = builder.build()


        // Check for equal distribution card.
        if (upgrades.isInstalled(AEItems.EQUAL_DISTRIBUTION_CARD)) {
            // Compute max possible amount of types based on whitelist size, and bound by type limit.
            var maxTypes = Int.MAX_VALUE.toLong()
            if (!isFuzzy && partitionListMode == IncludeExclude.WHITELIST && !config.keySet().isEmpty()) {
                maxTypes = config.keySet().size.toLong()
            }
            maxTypes = Math.min(maxTypes, maxItemTypes.toLong())
            val totalStorage = (getTotalBytes() - getBytesPerType() * maxTypes) * keyType.amountPerByte
            // Technically not exactly evenly distributed, but close enough!
            this.maxItemsPerType = Math.max(0, (totalStorage + maxTypes - 1) / maxTypes)
        } else {
            this.maxItemsPerType = Long.MAX_VALUE
        }

        this.hasVoidUpgrade = upgrades.isInstalled(AEItems.VOID_CARD)
    }

    private val storedStacks: List<GenericStack>
        get() = itemStackLocal.getOrDefault(AEComponents.STORAGE_CELL_INV, listOf())

    override fun persist() {
        if (isPersisted) {
            return
        }

        var itemCount: Long = 0

        val stacks: MutableList<GenericStack> = mutableListOf()

        for (entry in cellItems!!) {
            val amount = entry.value
            if (amount > 0) {
                itemCount += amount
                stacks.add(GenericStack(entry.key, amount))
            }
        }

        if (stacks.isEmpty()) {
            itemStackLocal.remove(AEComponents.STORAGE_CELL_INV)
        } else {
            itemStackLocal.set(AEComponents.STORAGE_CELL_INV, stacks)
        }

        storedItems = cellItems!!.size
        storedItemCount = itemCount
        isPersisted = true
    }

    protected open fun saveChanges() {
        // recalculate values
        storedItems = cellItems!!.size
        storedItemCount = 0
        for (storedAmount in cellItems!!.values) {
            storedItemCount += storedAmount
        }
        isPersisted = false
        if (container != null) {
            container.saveChanges()
        } else {
            // if there is no ISaveProvider, store to NBT immediately
            persist()
        }
    }

    private fun loadCellItems() {
        val storedStacks = storedStacks

        for (storedStack in storedStacks) {
            cellItems!![storedStack.what] = storedStack.amount
        }
    }

    override fun getAvailableStacks(out: KeyCounter) {
        for (entry in cellItems!!) {
            out.add(entry.key, entry.value)
        }
    }

    override fun getIdleDrain(): Double {
        return cellType!!.getIdleDrain()
    }

    open fun getFuzzyMode(): FuzzyMode? {
        return cellType!!.getFuzzyMode(itemStackLocal)
    }

    open fun getConfigInventory(): ConfigInventory? {
        return cellType!!.getConfigInventory(itemStackLocal)
    }

    open fun getUpgradesInventory(): IUpgradeInventory? {
        return cellType!!.getUpgrades(itemStackLocal)
    }

    open fun getBytesPerType(): Int {
        return cellType!!.getBytesPerType(itemStackLocal)
    }

    open fun canHoldNewItem(): Boolean {
        val bytesFree = getFreeBytes()
        return ((bytesFree > getBytesPerType()
                || bytesFree == getBytesPerType().toLong() && getUnusedItemCount() > 0)
                && getRemainingItemTypes() > 0)
    }

    val isPreformatted: Boolean
    get() = !partitionList!!.isEmpty

    fun getTotalBytes(): Long {
        return cellType!!.getBytes(this.itemStackLocal).toLong()
    }

    open fun getFreeBytes(): Long {
        return getTotalBytes() - getUsedBytes()
    }

    open fun getTotalItemTypes(): Long {
        return maxItemTypes.toLong()
    }

    open fun getStoredItemCount(): Long {
        return storedItemCount
    }

    open fun getStoredItemTypes(): Long {
        return storedItems.toLong()
    }

    fun getRemainingItemTypes(): Long {
        val basedOnStorage = this.getFreeBytes() / this.getBytesPerType()
        val baseOnTotal = this.getTotalItemTypes() - this.getStoredItemTypes()
        return if (basedOnStorage > baseOnTotal) baseOnTotal else basedOnStorage
    }

    fun getUsedBytes(): Long {
        val bytesForItemCount = (getStoredItemCount() + this.getUnusedItemCount()) / itemsPerByte
        return this.getStoredItemTypes() * this.getBytesPerType() + bytesForItemCount
    }

    fun getRemainingItemCount(): Long {
        val remaining = this.getFreeBytes() * itemsPerByte + this.getUnusedItemCount()
        return if (remaining > 0) remaining else 0
    }

    fun getUnusedItemCount(): Int {
        val div = (getStoredItemCount() % 8).toInt()
        return if (div == 0) {
            0
        } else itemsPerByte - div
    }

    override fun getStatus(): CellState? {
        if (getStoredItemTypes() == 0L) {
            return CellState.EMPTY
        }
        if (canHoldNewItem()) {
            return CellState.NOT_EMPTY
        }
        return if (getRemainingItemCount() > 0) {
            CellState.TYPES_FULL
        } else CellState.FULL
    }

    private fun isStorageCell(key: AEItemKey): Boolean {
        val type = getStorageCell(key)
        return type != null && !type.storableInStorageCell()
    }

    override fun insert(what: AEKey?, amount: Long, mode: Actionable?, source: IActionSource?): Long {
        if (amount == 0L || !keyType!!.contains(what)) {
            return 0
        }
        if (!partitionList!!.matchesFilter(what, partitionListMode)) {
            return 0
        }
        if (cellType!!.isBlackListed(itemStackLocal, what)) {
            return 0
        }

        // Run regular insert logic and then apply void upgrade to the returned value.
        val inserted = innerInsert(what!!, amount, mode!!, source!!)
        return if (hasVoidUpgrade) amount else inserted
    }

    private fun innerInsert(what: AEKey, amount: Long, mode: Actionable, source: IActionSource): Long {
        if (what is AEItemKey && isStorageCell(what)) {

            // TODO: make it work for any cell, and not just BasicCellInventory!
            val meInventory = createInventory(what.toStack(), null)
            if (!isCellEmpty(meInventory)) {
                return 0
            }
        }
        
        val currentAmount: Long = cellItems!!.get(what)?: 0
        var remainingItemCount = getRemainingItemCount()

        // Deduct the required storage for a new type if the type is new

        // Deduct the required storage for a new type if the type is new
        if (currentAmount <= 0) {
            if (!canHoldNewItem()) {
                // No space for more types
                return 0
            }
            remainingItemCount -= getBytesPerType().toLong() * keyType!!.amountPerByte
            if (remainingItemCount <= 0) {
                return 0
            }
        }

        // Apply max items per type

        // Apply max items per type
        remainingItemCount = Math.max(0, Math.min(maxItemsPerType - currentAmount, remainingItemCount))
        
        var _amount = amount

        if (_amount > remainingItemCount) {
            _amount = remainingItemCount
        }

        if (mode == Actionable.MODULATE) {
            cellItems!!.put(what, currentAmount + _amount)
            saveChanges()
        }

        return _amount
    }

    override fun extract(what: AEKey?, amount: Long, mode: Actionable, source: IActionSource?): Long {
        // To avoid long-overflow on the extracting callers side
        val extractAmount = Math.min(Int.MAX_VALUE.toLong(), amount)
        val currentAmount: Long? = cellItems!!.get(what)
        
        if (currentAmount != null && currentAmount > 0L) {
            if (extractAmount > currentAmount) {
                if (mode == Actionable.MODULATE) {
                    cellItems!!.remove(what, currentAmount)
                    saveChanges()
                }

                return currentAmount
            } else {
                if (mode == Actionable.MODULATE) {
                    cellItems!!.put(what!!, currentAmount - extractAmount)
                    saveChanges()
                }

                return extractAmount
            }
        }
        
        return 0
    }

    override fun getDescription(): Component = itemStackLocal.hoverName

    fun isFuzzy(): Boolean {
        return partitionList is FuzzyPriorityList
    }
}