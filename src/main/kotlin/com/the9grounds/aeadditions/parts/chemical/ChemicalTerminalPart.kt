package com.the9grounds.aeadditions.parts.chemical

import appeng.api.config.Settings
import appeng.api.config.SortDir
import appeng.api.config.SortOrder
import appeng.api.config.ViewItems
import appeng.api.implementations.tiles.IViewCellStorage
import appeng.api.parts.IPart
import appeng.api.parts.IPartModel
import appeng.api.storage.IMEMonitor
import appeng.api.storage.IStorageChannel
import appeng.api.storage.ITerminalHost
import appeng.api.storage.data.IAEStack
import appeng.api.util.IConfigManager
import appeng.items.parts.PartModels
import appeng.me.GridAccessException
import appeng.parts.PartModel
import appeng.util.ConfigManager
import appeng.util.IConfigManagerHost
import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.parts.AbstractDisplayPart
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.ResourceLocation

class ChemicalTerminalPart(itemStack: ItemStack) : AbstractDisplayPart(itemStack), ITerminalHost, IConfigManagerHost {
    
    val cm: IConfigManager = ConfigManager(this)
    
    init {
        cm.registerSetting(Settings.SORT_BY, SortOrder.NAME)
        cm.registerSetting(Settings.SORT_DIRECTION, SortDir.ASCENDING)
    }
    
    @PartModels
    val MODEL_OFF: ResourceLocation = ResourceLocation(AEAdditions.ID, "part/chemical_terminal_off")
    
    @PartModels
    val MODEL_ON: ResourceLocation = ResourceLocation(AEAdditions.ID, "part/chemical_terminal_on")
    
    val MODELS_OFF = PartModel(MODEL_BASE, MODEL_OFF, MODEL_STATUS_OFF)
    val MODELS_ON = PartModel(MODEL_BASE, MODEL_ON, MODEL_STATUS_ON)
    val MODELS_HAS_CHANNEL = PartModel(MODEL_BASE, MODEL_ON, MODEL_STATUS_HAS_CHANNEL)

    override fun getConfigManager(): IConfigManager = cm

    override fun readFromNBT(data: CompoundNBT?) {
        super.readFromNBT(data)
        cm.readFromNBT(data)
    }

    override fun writeToNBT(data: CompoundNBT?) {
        super.writeToNBT(data)
        cm.writeToNBT(data)
    }

    override fun getStaticModels(): IPartModel = this.selectModel(MODELS_OFF, MODELS_ON, MODELS_HAS_CHANNEL)
    
    override fun <T : IAEStack<T>?> getInventory(channel: IStorageChannel<T>?): IMEMonitor<T>? {
        return try {
            this.proxy.storage.getInventory(channel)
        } catch (e: GridAccessException) {
            null
        }
    }

    override fun updateSetting(manager: IConfigManager?, settingName: Settings?, newValue: Enum<*>?) {
        //
    }
}