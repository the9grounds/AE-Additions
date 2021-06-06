package extracells.util

import appeng.api.config.AccessRestriction
import appeng.api.config.Actionable
import appeng.api.networking.security.IActionSource
import appeng.api.storage.IMEInventoryHandler
import appeng.api.storage.IMEMonitor
import appeng.api.storage.IMEMonitorHandlerReceiver
import appeng.api.storage.IStorageChannel
import appeng.api.storage.data.IAEStack
import appeng.api.storage.data.IItemList

class MEMonitorHandler<T : IAEStack<T>>(val internalHandler: IMEInventoryHandler<T>, val chan: IStorageChannel<T>) : IMEMonitor<T> {

    private final var cachedList: IItemList<T> = chan.createList()
    private final val listeners = mutableMapOf<IMEMonitorHandlerReceiver<T>, Any>()
    protected var hasChanged = true

    override fun injectItems(input: T, mode: Actionable?, src: IActionSource?): T? {
        if (mode == Actionable.SIMULATE) {
            return this.internalHandler.injectItems(input, mode, src)
        }

        return this.monitorDifference(input.copy(), this.internalHandler.injectItems(input, mode, src), false, src)
    }

    private fun monitorDifference(original: T, leftOvers: T?, extraction: Boolean, src: IActionSource?) : T? {
        val diff = original.copy()
        if (extraction) {
            diff.stackSize = if (leftOvers == null) 0 else -leftOvers.stackSize
        } else if (leftOvers != null) {
            diff.decStackSize(leftOvers.stackSize)
        }

        if (diff.stackSize != 0L) {
            this.postChangesToListeners(listOf(diff), src)
        }

        return leftOvers
    }

    protected fun postChangesToListeners(changes: Iterable<T>, src: IActionSource?) {
        this.notifyListenersOfChange(changes, src)
    }

    protected fun notifyListenersOfChange(diff: Iterable<T>, src: IActionSource?) {
        this.hasChanged = true
        val i = this.listeners.iterator()

        while (i.hasNext()) {
            val o = i.next()
            val receiver = o.key

            if (receiver.isValid(o.value)) {
                receiver.postChange(this, diff, src)
            } else {
                i.remove()
            }
        }
    }

    override fun extractItems(request: T, mode: Actionable?, src: IActionSource?): T? {
        if (mode == Actionable.SIMULATE) {
            return this.internalHandler.extractItems(request, mode, src)
        }

        return this.monitorDifference(request.copy(), this.internalHandler.extractItems(request, mode, src), true, src)
    }

    override fun getAvailableItems(out: IItemList<T>?): IItemList<T> {
        return this.internalHandler.getAvailableItems(out)
    }

    override fun getChannel(): IStorageChannel<T> {
        return this.internalHandler.channel
    }

    override fun getAccess(): AccessRestriction {
        return this.internalHandler.access
    }

    override fun isPrioritized(input: T): Boolean {
        return this.internalHandler.isPrioritized(input)
    }

    override fun canAccept(input: T): Boolean {
        return this.internalHandler.canAccept(input)
    }

    override fun getPriority(): Int {
        return this.internalHandler.priority
    }

    override fun getSlot(): Int {
        return this.internalHandler.slot
    }

    override fun validForPass(i: Int): Boolean {
        return this.internalHandler.validForPass(i)
    }

    override fun addListener(listener: IMEMonitorHandlerReceiver<T>?, verificationToken: Any?) {
        if (listener != null && verificationToken != null) {
            this.listeners[listener] = verificationToken
        }
    }

    override fun removeListener(listener: IMEMonitorHandlerReceiver<T>?) {
        if (listener != null) {
            this.listeners.remove(listener)
        }
    }

    override fun getStorageList(): IItemList<T> {
        if (this.hasChanged) {
            this.hasChanged = false
            this.cachedList.resetStatus()
            return this.getAvailableItems(this.cachedList)
        }

        return this.cachedList
    }
}