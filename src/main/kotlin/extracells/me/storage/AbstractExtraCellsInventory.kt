package extracells.me.storage

import appeng.api.config.FuzzyMode
import appeng.api.implementations.items.IStorageCell
import appeng.api.storage.ICellInventory
import appeng.api.storage.ISaveProvider
import appeng.api.storage.data.IAEStack
import appeng.api.storage.data.IItemList
import appeng.util.Platform
import extracells.api.IExtraCellsStorageCell
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.items.IItemHandler

abstract class AbstractExtraCellsInventory<T : IAEStack<T>?>(cellType: IExtraCellsStorageCell<T>?, o: ItemStack?, container: ISaveProvider?) : ICellInventory<T> {
    protected var container: ISaveProvider? = null
    private var maxItemTypes = MAX_ITEM_TYPES
    private var storedItems: Short = 0
    private var storedItemCount = 0L
    private var i: ItemStack? = null
    protected var cellType: IExtraCellsStorageCell<T>? = null
    protected var itemsPerByte = 0
    private var isPersisted = true
    private var tagCompound: NBTTagCompound? = null

    protected var cellItems: IItemList<T>? = null
    get() {
        if (field == null) {
            field = this.channel.createList()
            loadCellItems()
        }
        return field
    }

    companion object
    {
        private val MAX_ITEM_TYPES = 63
        private val ITEM_TYPE_TAG = "it"
        private val ITEM_COUNT_TAG = "ic"
        private val ITEM_SLOT = "#"
        private val ITEM_SLOT_COUNT = "@"
        protected val ITEM_PRE_FORMATTED_COUNT = "PF"
        protected val ITEM_PRE_FORMATTED_SLOT = "PF#"
        protected val ITEM_PRE_FORMATTED_NAME = "PN"
        protected val ITEM_PRE_FORMATTED_FUZZY = "FP"
        private val ITEM_SLOT_KEYS = arrayOfNulls<String>(MAX_ITEM_TYPES)
        private val ITEM_SLOT_COUNT_KEYS = arrayOfNulls<String>(MAX_ITEM_TYPES)

        init {

            for (x in 0 until MAX_ITEM_TYPES) {
                ITEM_SLOT_KEYS[x] = ITEM_SLOT + x
                ITEM_SLOT_COUNT_KEYS[x] = ITEM_SLOT_COUNT + x
            }
        }
    }

    init {
        i = o
        this.cellType = cellType
        itemsPerByte = this.cellType!!.getChannel().unitsPerByte
        maxItemTypes = this.cellType!!.getTotalTypes(i!!)
        if (maxItemTypes > MAX_ITEM_TYPES) {
            maxItemTypes = MAX_ITEM_TYPES
        }
        if (maxItemTypes < 1) {
            maxItemTypes = 1
        }
        this.container = container
        tagCompound = Platform.openNbtData(o)
        storedItems = tagCompound!!.getShort(ITEM_TYPE_TAG)
        storedItemCount = tagCompound!!.getLong(ITEM_COUNT_TAG)
        cellItems = null
    }

//    protected open fun getCellItems(): IItemList<T>? {
//        if (cellItems == null) {
//            cellItems = this.channel.createList()
//            loadCellItems()
//        }
//        return cellItems
//    }

    override fun persist() {
        if (isPersisted) {
            return
        }
        var itemCount = 0L

        // add new pretty stuff...
        var x = 0
        for (v in cellItems!!) {
            itemCount += v!!.getStackSize()
            val g = NBTTagCompound()
            v.writeToNBT(g)
            tagCompound!!.setTag(ITEM_SLOT_KEYS[x], g)
            tagCompound!!.setLong(ITEM_SLOT_COUNT_KEYS[x], v.getStackSize())
            x++
        }
        val oldStoredItems = storedItems
        storedItems = cellItems!!.size().toShort()
        if (cellItems!!.isEmpty) {
            tagCompound!!.removeTag(ITEM_TYPE_TAG)
        } else {
            tagCompound!!.setShort(ITEM_TYPE_TAG, storedItems)
        }
        storedItemCount = itemCount
        if (itemCount == 0L) {
            tagCompound!!.removeTag(ITEM_COUNT_TAG)
        } else {
            tagCompound!!.setLong(ITEM_COUNT_TAG, itemCount)
        }

        // clean any old crusty stuff...
        while (x < oldStoredItems && x < maxItemTypes) {
            tagCompound!!.removeTag(ITEM_SLOT_KEYS[x])
            tagCompound!!.removeTag(ITEM_SLOT_COUNT_KEYS[x])
            x++
        }
        isPersisted = true
    }

    protected open fun saveChanges() {
        // recalculate values
        storedItems = cellItems!!.size().toShort()
        storedItemCount = 0
        for (v in cellItems!!) {
            storedItemCount += v!!.getStackSize().toInt()
        }
        isPersisted = false
        if (container != null) {
            container!!.saveChanges(this)
        } else {
            // if there is no ISaveProvider, store to NBT immediately
            persist()
        }
    }

    protected open fun loadCellItems() {
        if (cellItems == null) {
            cellItems = this.channel.createList()
        }
        cellItems!!.resetStatus() // clears totals and stuff.
        val types = this.storedItemTypes.toInt()
        var needsUpdate = false
        for (slot in 0 until types) {
            val compoundTag = tagCompound!!.getCompoundTag(ITEM_SLOT_KEYS[slot])
            val stackSize = tagCompound!!.getLong(ITEM_SLOT_COUNT_KEYS[slot])
            needsUpdate = needsUpdate or !loadCellItem(compoundTag, stackSize)
        }
        if (needsUpdate) {
            saveChanges()
        }
    }

    /**
     * Load a single item.
     *
     * @param compoundTag
     * @param stackSize
     * @return true when successfully loaded
     */
    protected abstract fun loadCellItem(compoundTag: NBTTagCompound?, stackSize: Long): Boolean

    override fun getAvailableItems(out: IItemList<T>): IItemList<T>? {
        for (item in cellItems!!) {
            out.add(item)
        }
        return out
    }

    override fun getItemStack(): ItemStack? {
        return i
    }

    override fun getIdleDrain(): Double {
        return cellType!!.getIdleDrain()
    }

    override fun getFuzzyMode(): FuzzyMode? {
        return cellType!!.getFuzzyMode(i)
    }

    override fun getConfigInventory(): IItemHandler? {
        return cellType!!.getConfigInventory(i)
    }

    override fun getUpgradesInventory(): IItemHandler? {
        return cellType!!.getUpgradesInventory(i)
    }

    override fun getBytesPerType(): Int {
        return cellType!!.getBytesPerType(i!!)
    }

    override fun canHoldNewItem(): Boolean {
        val bytesFree = this.freeBytes
        return (bytesFree > this.bytesPerType || bytesFree == this.bytesPerType.toLong() && this.unusedItemCount > 0) && this
            .remainingItemTypes > 0
    }

    override fun getTotalBytes(): Long {
        return cellType!!.getBytes(i!!).toLong()
    }

    override fun getFreeBytes(): Long {
        return this.totalBytes - this.usedBytes
    }

    override fun getTotalItemTypes(): Long {
        return maxItemTypes.toLong()
    }

    override fun getStoredItemCount(): Long {
        return storedItemCount.toLong()
    }

    override fun getStoredItemTypes(): Long {
        return storedItems.toLong()
    }

    override fun getRemainingItemTypes(): Long {
        val basedOnStorage = this.freeBytes / this.bytesPerType
        val baseOnTotal = this.totalItemTypes - this.storedItemTypes
        return if (basedOnStorage > baseOnTotal) baseOnTotal else basedOnStorage
    }

    override fun getUsedBytes(): Long {
        val bytesForItemCount = (getStoredItemCount() + this.unusedItemCount) / itemsPerByte
        return this.storedItemTypes * this.bytesPerType + bytesForItemCount
    }

    override fun getRemainingItemCount(): Long {
        val remaining = this.freeBytes * itemsPerByte + this.unusedItemCount
        return if (remaining > 0) remaining else 0
    }

    override fun getUnusedItemCount(): Int {
        val div = (getStoredItemCount() % 8).toInt()
        return if (div == 0) {
            0
        } else itemsPerByte - div
    }

    override fun getStatusForCell(): Int {
        if (canHoldNewItem()) {
            return 1
        }
        return if (this.remainingItemCount > 0) {
            2
        } else 3
    }
}