package com.the9grounds.aeadditions.me.storage

import appeng.api.config.Actionable
import appeng.api.config.IncludeExclude
import appeng.api.networking.security.IActionSource
import appeng.api.stacks.*
import appeng.api.storage.cells.CellState
import appeng.api.storage.cells.ISaveProvider
import appeng.api.storage.cells.StorageCell
import appeng.core.AELog
import appeng.util.prioritylist.FuzzyPriorityList
import appeng.util.prioritylist.IPartitionList
import com.the9grounds.aeadditions.item.storage.SuperStorageCell
import it.unimi.dsi.fastutil.longs.LongArrayList
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

class SuperCellInventory(val cell: SuperStorageCell?, val itemStackLocal: ItemStack, val container: ISaveProvider?) : StorageCell {
    
    private var tagCompound: CompoundTag? = null
    private var maxItemTypes = MAX_ITEM_TYPES
    private var storedItems: Short
    get() = (cellItems?.size ?: 0).toShort()
    private var storedItemCount = 0L
    var partitionList: IPartitionList? = null
        private set
    var partitionListMode: IncludeExclude? = null
    private set
    private var maxItemsPerType: Long = Long.MAX_VALUE
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
    
    private var computedUsedBytes = 0L
    
    var numberOfTypesByKeyType = mutableMapOf<AEKeyType, Int>()
    var storedItemCountByKeyType = mutableMapOf<AEKeyType, Long>()
    val cellTypes = AEKeyTypes.getAll()

    companion object
    {
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
        
        private fun getStorageCell(input: ItemStack?): SuperStorageCell? {
            if (input != null && input.item is SuperStorageCell) {
                return input.item as SuperStorageCell
            }
            
            return null
        }
        
        private fun getStorageCell(itemKey: AEItemKey): SuperStorageCell? {
            if (itemKey.item is SuperStorageCell) {
                return itemKey.item as SuperStorageCell
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
            if (item !is SuperStorageCell) {
                return null
            }
            
            if (!item.isStorageCell(itemStack)) {
                return null
            }
            
            return SuperCellInventory(item, itemStack, container)
        }
    }

    init {
        for (keyType in cellTypes) {
            numberOfTypesByKeyType[keyType] = 0
        }
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
        computedUsedBytes = tagCompound!!.getLong("usedBytes")
        cellItems = null
        
        recalculateValues()
    }

    override fun persist() {
        if (isPersisted) {
            return
        }

        var itemCount: Long = 0
        var localItemCountByKeyType = mutableMapOf<AEKeyType, Long>()
        var localNumberOfTypesByKeyType = mutableMapOf<AEKeyType, Int>()

        // add new pretty stuff...

        // add new pretty stuff...
        val amounts = LongArrayList(cellItems!!.size)
        val keys = ListTag()

        for (entry in cellItems!!) {
            val amount = entry.value
            if (amount > 0) {
                val currentAmount = localItemCountByKeyType[entry.key.type] ?: 0
                localItemCountByKeyType[entry.key.type] = currentAmount + amount
                keys.add(entry.key.toTagGeneric())
                amounts.add(amount)
                
                val currentNumber = localNumberOfTypesByKeyType[entry.key.type] ?: 0

                localNumberOfTypesByKeyType[entry.key.type] = currentNumber + 1
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
        storedItemCountByKeyType = localItemCountByKeyType
        numberOfTypesByKeyType = localNumberOfTypesByKeyType
        
        var localComputedUsedBytes = 0L
        
        for (keyType in cellTypes) {
            localComputedUsedBytes += getUsedBytesForType(keyType)
        }
        
        computedUsedBytes = localComputedUsedBytes
        
        if (computedUsedBytes == 0L) {
            getTag()!!.remove(ITEM_COUNT_TAG)
        } else {
            getTag()!!.putLong(ITEM_COUNT_TAG, computedUsedBytes)
            getTag()!!.putLong("usedBytes", computedUsedBytes)
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
        recalculateValues()

        isPersisted = false
        if (container != null) {
            container.saveChanges()
        } else {
            // if there is no ISaveProvider, store to NBT immediately
            persist()
        }
    }

    private fun recalculateValues() {
        storedItems = cellItems!!.size.toShort()
        storedItemCount = 0
        var localItemCountByKeyType = mutableMapOf<AEKeyType, Long>()
        var localNumberOfTypesByKeyType = mutableMapOf<AEKeyType, Int>()

        for (entry in cellItems!!) {
            val currentAmount = localItemCountByKeyType[entry.key.type] ?: 0
            localItemCountByKeyType[entry.key.type] = currentAmount + entry.value
            storedItemCount += entry.value!!
            val currentNumber = localNumberOfTypesByKeyType[entry.key.type] ?: 0

            localNumberOfTypesByKeyType[entry.key.type] = currentNumber + 1
        }

        storedItemCountByKeyType = localItemCountByKeyType
        numberOfTypesByKeyType = localNumberOfTypesByKeyType

        var localComputedUsedBytes = 0L

        for (keyType in cellTypes) {
            localComputedUsedBytes += getUsedBytesForType(keyType)
        }

        computedUsedBytes = localComputedUsedBytes
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
        recalculateValues()
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

    open fun getBytesPerType(): Int {
        return cell!!.getBytesPerType(itemStackLocal)
    }

    fun canHoldNewItem(): Boolean {
        return getTotalBytes() > getUsedBytes() && getRemainingItemTypes() > 0
    }

    fun getTotalBytes(): Long {
        return cell!!.getBytes(this.itemStackLocal).toLong()
    }

    fun getFreeBytes(): Long {
        return getTotalBytes() - getUsedBytes()
    }

    open fun getTotalItemTypes(): Long {
        return maxItemTypes.toLong()
    }

    open fun getStoredItemCount(): Long {
        var storedItemCount = 0L
        for (keyType in storedItemCountByKeyType) {
            storedItemCount += keyType.value
        }
        
        return storedItemCount
    }

    fun getStoredItemCountForKeyType(keyType: AEKeyType): Long {
        return storedItemCountByKeyType[keyType] ?: 0L;
    }

    open fun getStoredItemTypes(): Long {
        return storedItems.toLong()
    }

    fun getRemainingItemTypes(): Long {
        val basedOnStorage = this.getFreeBytes() / this.getBytesPerType()
        val baseOnTotal = this.getTotalItemTypes() - this.getStoredItemTypes()
        return if (basedOnStorage > baseOnTotal) baseOnTotal else basedOnStorage
    }

    fun getUsedBytesForType(keyType: AEKeyType): Long {
        val bytesForItemCount = (getStoredItemCountForKeyType(keyType) + this.getUnusedItemCountForKeyType(keyType)) / keyType.amountPerByte
        return (numberOfTypesByKeyType[keyType]?: 0) * this.getBytesPerType() + bytesForItemCount
    }

    fun getUsedBytes(): Long {
        return computedUsedBytes
    }

    fun getRemainingItemCount(): Long {
        val remaining = this.getFreeBytes() * itemsPerByte + this.getUnusedItemCount()
        return if (remaining > 0) remaining else 0
    }
    
    fun getRemainingItemCountForKeyType(keyType: AEKeyType): Long {
        return this.getFreeBytes() * keyType.amountPerByte + this.getUnusedItemCountForKeyType(keyType) ?: 0L
    }

    fun getUnusedItemCount(): Int {
        val div = (getStoredItemCount() % 8).toInt()
        return if (div == 0) {
            0
        } else itemsPerByte - div
    }

    fun getUnusedItemCountForKeyType(keyType: AEKeyType): Int {
        val div = (getStoredItemCountForKeyType(keyType) % 8).toInt()
        return if (div == 0) {
            0
        } else keyType.amountPerByte - div
    }

    override fun getStatus(): CellState? {
        if (getStoredItemTypes() == 0L) {
            return CellState.EMPTY
        }
        if (canHoldNewItem()) {
            return CellState.NOT_EMPTY
        }
        return CellState.FULL
    }

    private fun isStorageCell(key: AEItemKey): Boolean {
        val type = getStorageCell(key)
        return type != null && !type.storableInStorageCell()
    }

    override fun insert(what: AEKey?, amount: Long, mode: Actionable?, source: IActionSource?): Long {
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
        var remainingItemCount = getRemainingItemCountForKeyType(what.type)

        // Deduct the required storage for a new type if the type is new

        // Deduct the required storage for a new type if the type is new
        if (currentAmount <= 0) {
            if (!canHoldNewItem()) {
                // No space for more types
                return 0
            }
            remainingItemCount -= getBytesPerType().toLong() * what.type!!.amountPerByte
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