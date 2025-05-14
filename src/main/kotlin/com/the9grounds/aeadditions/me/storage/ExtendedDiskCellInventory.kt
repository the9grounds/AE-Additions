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
import com.the9grounds.aeadditions.api.IAEAdditionsDiskCell
import com.the9grounds.aeadditions.item.storage.DiskCell
import io.github.projectet.ae2things.AE2Things
import io.github.projectet.ae2things.util.Constants
import io.github.projectet.ae2things.util.DataStorage
import io.github.projectet.ae2things.util.StorageManager
import it.unimi.dsi.fastutil.longs.LongArrayList
import it.unimi.dsi.fastutil.objects.Object2LongMap
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap
import me.ramidzkh.mekae2.ae2.MekanismKeyType
import net.minecraft.nbt.ListTag
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import java.util.*
import kotlin.math.min

class ExtendedDiskCellInventory(
    private val cellType: IAEAdditionsDiskCell,
    private val i: ItemStack,
    private val container: ISaveProvider?
) : StorageCell {
    private val keyType: AEKeyType = cellType.getKeyType()!!
    private var partitionList: IPartitionList? = null
    var partitionListMode: IncludeExclude? = null
        private set
    private var storedItems = 0
    var storedItemCount: Long = 0
        private set
    private var storedAmounts: Object2LongMap<AEKey>? = null
    private var isPersisted = true

    init {
        initData()

        updateFilter()
    }

    private fun updateFilter() {
        val builder = IPartitionList.builder()

        val upgrades = upgradesInventory
        val config = configInventory

        val hasInverter = upgrades.isInstalled(AEItems.INVERTER_CARD)
        if (upgrades.isInstalled(AEItems.FUZZY_CARD)) {
            builder.fuzzyMode(fuzzyMode)
        }

        builder.addAll(config.keySet())

        partitionListMode = (if (hasInverter) IncludeExclude.BLACKLIST else IncludeExclude.WHITELIST)
        partitionList = builder.build()
    }

    private val diskStorage: DataStorage
        get() {
            return if (diskUUID != null) storageInstance.getOrCreateDisk(diskUUID)
            else DataStorage.EMPTY
        }

    private fun initData() {
        if (hasDiskUUID()) {
            this.storedItems = diskStorage.stackAmounts.size
            this.storedItemCount = diskStorage.itemCount
        } else {
            this.storedItems = 0
            this.storedItemCount = 0
            cellItems
        }
    }

    val isPreformatted: Boolean
        get() = !partitionList!!.isEmpty

    val isFuzzy: Boolean
        get() = partitionList is FuzzyPriorityList

    val configInventory: ConfigInventory
        get() = cellType.getConfigInventory(this.i)!!

    val fuzzyMode: FuzzyMode
        get() = cellType.getFuzzyMode(this.i)

    val upgradesInventory: IUpgradeInventory
        get() = cellType.getUpgrades(this.i)

    override fun getStatus(): CellState {
        if (this.storedItemCount == 0L) {
            return CellState.EMPTY
        }
        if (this.canHoldNewItem()) {
            return CellState.NOT_EMPTY
        }
        return CellState.FULL
    }

    val clientStatus: CellState
        get() {
            if (this.nbtItemCount == 0L) {
                return CellState.EMPTY
            }
            if ((nbtItemCount > 0 && nbtItemCount != totalBytes)) {
                return CellState.NOT_EMPTY
            }
            return CellState.FULL
        }

    override fun getIdleDrain(): Double {
        return cellType.getIdleDrain()
    }

    override fun persist() {
        if (this.isPersisted) {
            return
        }

        if (storedItemCount == 0L) {
            if (hasDiskUUID()) {
                storageInstance.removeDisk(diskUUID)
                i.tag!!.remove(Constants.DISKUUID)
                i.tag!!.remove(ITEM_COUNT_TAG)
                initData()
            }
            return
        }

        var itemCount: Long = 0

        // add new pretty stuff...
        val amounts: LongArrayList = LongArrayList(storedAmounts!!.size)
        val keys = ListTag()

        for (entry in storedAmounts!!.object2LongEntrySet()) {
            val amount = entry.longValue

            if (amount > 0) {
                itemCount += amount
                keys.add(entry.key.toTagGeneric())
                amounts.add(amount)
            }
        }

        if (keys.isEmpty()) {
            storageInstance.updateDisk(diskUUID, DataStorage())
        } else {
            storageInstance.modifyDisk(diskUUID, keys, amounts.toArray(LongArray(0)), itemCount)
        }

        this.storedItems = storedAmounts!!.size.toShort().toInt()

        this.storedItemCount = itemCount
        i.getOrCreateTag().putLong(ITEM_COUNT_TAG, itemCount)

        this.isPersisted = true
    }

    override fun getDescription(): Component? {
        return null
    }

    fun hasDiskUUID(): Boolean {
        return i.hasTag() && i.getOrCreateTag().contains(Constants.DISKUUID)
    }

    val diskUUID: UUID?
        get() {
            return if (hasDiskUUID()) i.getOrCreateTag().getUUID(Constants.DISKUUID)
            else null
        }

    private fun isStorageCell(key: AEItemKey): Boolean {
        val type = getStorageCell(key)
        return type != null && !type.storableInStorageCell()
    }

    protected val cellItems: Object2LongMap<AEKey>
        get() {
            if (this.storedAmounts == null) {
                this.storedAmounts = Object2LongOpenHashMap()
                this.loadCellItems()
            }

            return this.storedAmounts!!
        }

    override fun getAvailableStacks(out: KeyCounter) {
        for (entry in cellItems!!.object2LongEntrySet()) {
            out.add(entry.key, entry.longValue)
        }
    }

    private fun loadCellItems() {
        var corruptedTag = false

        if (!i.hasTag()) {
            return
        }

        val amounts = diskStorage.stackAmounts
        val tags = diskStorage.stackKeys
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
                storedAmounts!!.put(key, amount)
            }
        }

        if (corruptedTag) {
            this.saveChanges()
        }
    }

    private val storageInstance: StorageManager
        get() = AE2Things.STORAGE_INSTANCE

    protected fun saveChanges() {
        // recalculate values
        this.storedItems = storedAmounts!!.size
        this.storedItemCount = 0
        for (storedAmount in storedAmounts!!.values) {
            this.storedItemCount += storedAmount
        }

        this.isPersisted = false
        if (this.container != null) {
            container.saveChanges()
        } else {
            // if there is no ISaveProvider, store to NBT immediately
            this.persist()
        }
    }

    val remainingItemCount: Long
        get() {
            val beforeMultiply = if (this.freeBytes > 0) freeBytes else 0

            return beforeMultiply * divisible
        }

    override fun insert(what: AEKey, amount: Long, mode: Actionable, source: IActionSource): Long {
        var amount = amount
        if (amount == 0L || !keyType.contains(what)) {
            return 0
        }

        if (!partitionList!!.matchesFilter(what, this.partitionListMode)) {
            return 0
        }

        if (cellType.isBlackListed(this.i, what)) {
            return 0
        }

        // This is slightly hacky as it expects a read-only access, but fine for now.
        // TODO: Guarantee a read-only access. E.g. provide an isEmpty() method and
        // ensure CellInventory does not write
        // any NBT data for empty cells instead of relying on an empty IAEStackList
        if (what is AEItemKey && this.isStorageCell(what)) {
            // TODO: make it work for any cell, and not just BasicCellInventory!
            val meInventory = createInventory(what.toStack(), null)
            if (!isCellEmpty(meInventory)) {
                return 0
            }
        }

        if (!hasDiskUUID()) {
            i.getOrCreateTag().putUUID(Constants.DISKUUID, UUID.randomUUID())
            storageInstance.getOrCreateDisk(diskUUID)
            loadCellItems()
        }

        val currentAmount = cellItems!!.getLong(what)
        val remainingItemCount = remainingItemCount

        if (amount > remainingItemCount) {
            amount = remainingItemCount
        }

        if (mode == Actionable.MODULATE) {
            cellItems!!.put(what, currentAmount + amount)
            this.saveChanges()
        }

        return amount
    }

    override fun extract(what: AEKey, amount: Long, mode: Actionable, source: IActionSource): Long {
        // To avoid long-overflow on the extracting callers side
        val extractAmount = min(Int.MAX_VALUE.toDouble(), amount.toDouble()).toLong()

        val currentAmount = cellItems.getLong(what)
        if (currentAmount > 0) {
            if (extractAmount >= currentAmount) {
                if (mode == Actionable.MODULATE) {
                    cellItems.remove(what)
                    this.saveChanges()
                }

                return currentAmount
            } else {
                if (mode == Actionable.MODULATE) {
                    cellItems!!.put(what, currentAmount - extractAmount)
                    this.saveChanges()
                }

                return extractAmount
            }
        }

        return 0
    }

    val totalBytes: Long
        get() = cellType.getBytes(this.i).toLong()

    val divisible: Int
        get() {
            return when(keyType) {
                AEKeyType.fluids() -> 1000
                AEKeyType.items() -> 1
                MekanismKeyType.TYPE -> 1000
                else -> 1
            }
        }

    val freeBytes: Long
        get() {
            return totalBytes - (storedItemCount / divisible)
        }

    val usedSpace: Long
        get() = totalBytes - freeBytes

    val nbtItemCount: Long
        get() {
            if (hasDiskUUID()) {
                return i.tag!!.getLong(ITEM_COUNT_TAG)
            }
            return 0
        }

    val storedItemTypes: Long
        get() = storedItems.toLong()

    fun canHoldNewItem(): Boolean {
        return (freeBytes > 0 && freeBytes != totalBytes)
    }

    companion object {
        const val ITEM_COUNT_TAG: String = "ic"
        const val STACK_KEYS: String = "keys"
        const val STACK_AMOUNTS: String = "amts"

        fun createInventory(stack: ItemStack, saveProvider: ISaveProvider?): ExtendedDiskCellInventory? {
            Objects.requireNonNull(stack, "Cannot create cell inventory for null itemstack")

            if (stack.item !is IAEAdditionsDiskCell) {
                return null
            }

            val cellType = stack.item as IAEAdditionsDiskCell

            if (!cellType.isStorageCell(stack)) {
                // This is not an error. Items may decide to not be a storage cell temporarily.
                return null
            }

            // The cell type's channel matches, so this cast is safe
            return ExtendedDiskCellInventory(cellType, stack, saveProvider)
        }

        fun hasDiskUUID(disk: ItemStack): Boolean {
            if (disk.item is IAEAdditionsDiskCell) {
                return disk.hasTag() && disk.getOrCreateTag().contains(Constants.DISKUUID)
            }
            return false
        }

        private fun getStorageCell(itemKey: AEItemKey): DiskCell? {
            if (itemKey.item is DiskCell) {
                return itemKey.item as DiskCell
            }

            return null
        }

        private fun isCellEmpty(inv: ExtendedDiskCellInventory?): Boolean {
            if (inv != null) {
                return inv.availableStacks.isEmpty
            }
            return true
        }
    }
}