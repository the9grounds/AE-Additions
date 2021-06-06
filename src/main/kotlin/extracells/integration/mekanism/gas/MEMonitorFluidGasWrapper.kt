package extracells.integration.mekanism.gas

import appeng.api.config.AccessRestriction
import appeng.api.config.Actionable
import appeng.api.networking.security.IActionSource
import appeng.api.networking.storage.IBaseMonitor
import appeng.api.storage.IMEMonitor
import appeng.api.storage.IMEMonitorHandlerReceiver
import appeng.api.storage.IStorageChannel
import appeng.api.storage.data.IAEFluidStack
import appeng.api.storage.data.IItemList
import extracells.api.gas.IAEGasStack
import extracells.util.GasUtil
import extracells.util.StorageChannels

class MEMonitorFluidGasWrapper(val gasMonitor: IMEMonitor<IAEGasStack>) : IMEMonitor<IAEFluidStack>, IMEMonitorHandlerReceiver<IAEGasStack> {

    final val listeners = mutableMapOf<IMEMonitorHandlerReceiver<IAEFluidStack>, Any>()

    override fun injectItems(fluidStack: IAEFluidStack?, actionable: Actionable?, actionSource: IActionSource?): IAEFluidStack {
        return GasUtil.createAEFluidStack(gasMonitor.injectItems(GasUtil.createAEGasStack(fluidStack), actionable, actionSource))
    }

    override fun extractItems(fluidStack: IAEFluidStack?, actionable: Actionable?, actionSource: IActionSource?): IAEFluidStack {
        return GasUtil.createAEFluidStack(gasMonitor.extractItems(GasUtil.createAEGasStack(fluidStack), actionable, actionSource))
    }

    override fun getAvailableItems(itemList: IItemList<IAEFluidStack>?): IItemList<IAEFluidStack> {
        return GasUtil.createAEFluidItemList(gasMonitor.getAvailableItems(GasUtil.createAEGasItemList(itemList)))
    }

    override fun getChannel(): IStorageChannel<IAEFluidStack> = StorageChannels.FLUID

    override fun getAccess(): AccessRestriction = gasMonitor.access

    override fun isPrioritized(fluidStack: IAEFluidStack?): Boolean = gasMonitor.isPrioritized(GasUtil.createAEGasStack(fluidStack))

    override fun canAccept(fluidStack: IAEFluidStack?): Boolean = gasMonitor.canAccept(GasUtil.createAEGasStack(fluidStack))

    override fun getPriority(): Int = gasMonitor.priority

    override fun getSlot(): Int = gasMonitor.slot

    override fun validForPass(i: Int): Boolean = gasMonitor.validForPass(i)

    override fun addListener(listener: IMEMonitorHandlerReceiver<IAEFluidStack>, verificationToken: Any) {
        this.listeners[listener] = verificationToken
        // TODO: Look at this
        if (listeners.isEmpty()) {
            gasMonitor.addListener(this, null)
        }
    }

    override fun removeListener(listener: IMEMonitorHandlerReceiver<IAEFluidStack>?) {
        this.listeners.remove(listener)
        if (listeners.isEmpty()) {
            gasMonitor.removeListener(this)
        }
    }

    override fun getStorageList(): IItemList<IAEFluidStack> = GasUtil.createAEFluidItemList(gasMonitor.storageList)

    override fun isValid(p0: Any?): Boolean = true

    override fun postChange(baseMonitor: IBaseMonitor<IAEGasStack>?, iterable: MutableIterable<IAEGasStack>, actionSource: IActionSource?) {
        val changes = mutableListOf<IAEFluidStack>()

        iterable.forEach {
            changes.add(GasUtil.createAEFluidStack(it))
        }

        this.listeners.forEach { (k, _) ->
                k.postChange(this, changes, actionSource)
        }
    }

    override fun onListUpdate() {
        this.listeners.forEach { (k, _) ->
            k.onListUpdate()
        }
    }
}