package com.the9grounds.aeadditions.me.storage

import appeng.api.config.Actionable
import appeng.api.config.FuzzyMode
import appeng.api.config.IncludeExclude
import appeng.api.networking.security.IActionSource
import appeng.api.stacks.AEItemKey
import appeng.api.stacks.AEKey
import appeng.api.stacks.AEKeyType
import appeng.api.stacks.KeyCounter
import appeng.api.storage.cells.CellState
import appeng.api.storage.cells.ISaveProvider
import appeng.api.storage.cells.StorageCell
import appeng.api.upgrades.IUpgradeInventory
import appeng.core.AELog
import appeng.core.definitions.AEItems
import appeng.util.ConfigInventory
import appeng.util.prioritylist.FuzzyPriorityList
import appeng.util.prioritylist.IPartitionList
import com.the9grounds.aeadditions.api.IAEAdditionsStorageCell
import it.unimi.dsi.fastutil.longs.LongArrayList
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

class SuperCellInventory(val cell: IAEAdditionsStorageCell?, val itemStackLocal: ItemStack, val container: ISaveProvider?) : StorageCell {
    
    private var tagCompound: CompoundTag? = null
    private var maxItemTypes = MAX_ITEM_TYPES
    private var storedItems: Short
    get() = (cellItems?.size ?: 0).toShort()
    private var storedItemCount = 0L
    var partitionList: IPartitionList? = null
        private set
    var partitionListMode: IncludeExclude? = null
    private set
    private var maxItemsPerType: Long = 0
    private var hasVoidUpgrade = false
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
        val cellTypes = mutableListOf(
            AEKeyType.items(),
            AEKeyType.fluids()
        )
        private val MAX_ITEM_TYPES = 1000
        private val ITEM_TYPE_TAG = "it"
        private val ITEM_COUNT_TAG = "ic"
        private val ITEM_SLOT = "#"
        private val ITEM_SLOT_COUNT = "@"
        private const val STACK_KEYS = "keys"
        private const val STACK_AMOUNTS = "amts"
        private val ITEM_SLOT_KEYS = arrayOfNulls<String>(MAX_ITEM_TYPES)
        private val ITEM_SLOT_COUNT_KEYS = arrayOfNulls<String>(MAX_ITEM_TYPES)

        init {
            for (x in 0 until MAX_ITEM_TYPES) {
                ITEM_SLOT_KEYS[x] = ITEM_SLOT + x
                ITEM_SLOT_COUNT_KEYS[x] = ITEM_SLOT_COUNT + x
            }
        }
        
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
        
        private fun isCellEmpty(cellInventory: SuperCellInventory?): Boolean {
            if (cellInventory == null) {
                return true
            }
            return cellInventory.availableStacks.isEmpty
        }
        
        fun isCell(itemStack: ItemStack?): Boolean {
            return getStorageCell(itemStack) !== null
        }
        
        fun createInventory(itemStack: ItemStack, container: ISaveProvider?): SuperCellInventory? {
            val item = itemStack.item
            if (!(item is com.the9grounds.aeadditions.item.storage.StorageCell)) {
                return null
            }
            
            if (!item.isStorageCell(itemStack)) {
                return null
            }
            
            return SuperCellInventory(item, itemStack, container)
        }
    }

    init {
        maxItemTypes = this.cell!!.getTotalTypes(itemStackLocal)
        if (maxItemTypes > MAX_ITEM_TYPES) {
            maxItemTypes = MAX_ITEM_TYPES
        }
        if (maxItemTypes < 1) {
            maxItemTypes = 1
        }
        tagCompound = itemStackLocal.orCreateTag
        storedItems = tagCompound!!.getShort(ITEM_TYPE_TAG)
        storedItemCount = tagCompound!!.getLong(ITEM_COUNT_TAG)
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

        this.hasVoidUpgrade = upgrades.isInstalled(AEItems.VOID_CARD)
    }

    override fun persist() {
        if (isPersisted) {
            return
        }

        var itemCount: Long = 0

        // add new pretty stuff...

        // add new pretty stuff...
        val amounts = LongArrayList(cellItems!!.size)
        val keys = ListTag()

        for (entry in cellItems!!) {
            val amount = entry.value
            if (amount > 0) {
                itemCount += amount
                keys.add(entry.key.toTagGeneric())
                amounts.add(amount)
            }
        }

        if (keys.isEmpty()) {
            getTag()!!.remove(STACK_KEYS)
            getTag()!!.remove(STACK_AMOUNTS)
        } else {
            getTag()!!.put(STACK_KEYS, keys)
            getTag()!!.putLongArray(STACK_AMOUNTS, amounts.toArray(LongArray(0)))
        }

        storedItems = cellItems!!.size.toShort()

        storedItemCount = itemCount
        if (itemCount == 0L) {
            getTag()!!.remove(ITEM_COUNT_TAG)
        } else {
            getTag()!!.putLong(ITEM_COUNT_TAG, itemCount)
        }

        isPersisted = true
    }

    open fun getTag(): CompoundTag? {
        // On Fabric, the tag itself may be copied and then replaced later in case a portable cell is being charged.
        // In that case however, we can rely on the itemstack reference not changing due to the special logic in the
        // transactional inventory wrappers. So we must always re-query the tag from the stack.
        return itemStackLocal.getOrCreateTag()
    }

    protected open fun saveChanges() {
        // recalculate values
        storedItems = cellItems!!.size.toShort()
        storedItemCount = 0
        for (storedAmount in cellItems!!.values) {
            storedItemCount += storedAmount!!
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
        var corruptedTag = false
        val amounts = getTag()!!.getLongArray(STACK_AMOUNTS)
        val tags = getTag()!!.getList(STACK_KEYS, Tag.TAG_COMPOUND.toInt())
        if (amounts.size != tags.size) {
            AELog.warn(
                "Loading storage cell with mismatched amounts/tags: %d != %d",
                amounts.size, tags.size
            )
        }
        for (i in amounts.indices) {
            val amount = amounts[i]
            val key = AEKey.fromTagGeneric(tags.getCompound(i))
            if (amount <= 0 || key == null) {
                corruptedTag = true
            } else {
                cellItems!!.put(key, amount)
            }
        }
        if (corruptedTag) {
            saveChanges()
        }
    }

    override fun getAvailableStacks(out: KeyCounter) {
        for (entry in cellItems!!) {
            out.add(entry.key, entry.value)
        }
    }

    override fun getIdleDrain(): Double {
        return cell!!.getIdleDrain()
    }

    open fun getFuzzyMode(): FuzzyMode? {
        return cell!!.getFuzzyMode(itemStackLocal)
    }

    open fun getConfigInventory(): ConfigInventory? {
        return cell!!.getConfigInventory(itemStackLocal)
    }

    open fun getUpgradesInventory(): IUpgradeInventory? {
        return cell!!.getUpgrades(itemStackLocal)
    }

    open fun getBytesPerType(): Int {
        return cell!!.getBytesPerType(itemStackLocal)
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
        return cell!!.getBytes(this.itemStackLocal).toLong()
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
        if (!partitionList!!.matchesFilter(what, partitionListMode)) {
            return 0
        }
        if (cell!!.isBlackListed(itemStackLocal, what)) {
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
//        if (currentAmount <= 0) {
//            if (!canHoldNewItem()) {
//                // No space for more types
//                return 0
//            }
//            remainingItemCount -= getBytesPerType().toLong() * keyType!!.amountPerByte
//            if (remainingItemCount <= 0) {
//                return 0
//            }
//        }

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

        return amount
    }

    override fun extract(what: AEKey?, amount: Long, mode: Actionable, source: IActionSource?): Long {
        // To avoid long-overflow on the extracting callers side
        val extractAmount = Math.min(Int.MAX_VALUE.toLong(), amount)
        val currentAmount: Long? = cellItems!!.get(what)
        return when {
            currentAmount == null -> 0
            currentAmount > 0 -> {
                if (mode == Actionable.MODULATE) {
                    cellItems!!.remove(what, currentAmount)
                    saveChanges()
                }
                currentAmount
            }
            else -> {
                if (mode == Actionable.MODULATE) {
                    cellItems!!.put(what!!, currentAmount - extractAmount)
                    saveChanges()
                }
                extractAmount
            }
        }
    }

    override fun getDescription(): Component = itemStackLocal.hoverName

    fun isFuzzy(): Boolean {
        return partitionList is FuzzyPriorityList
    }
}