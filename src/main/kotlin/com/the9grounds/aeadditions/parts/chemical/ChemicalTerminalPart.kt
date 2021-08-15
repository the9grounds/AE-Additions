package com.the9grounds.aeadditions.parts.chemical

import appeng.api.parts.IPartModel
import appeng.api.storage.IMEMonitor
import appeng.api.storage.IStorageChannel
import appeng.api.storage.ITerminalHost
import appeng.api.storage.data.IAEStack
import appeng.me.GridAccessException
import appeng.parts.PartModel
import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.container.chemical.ChemicalTerminalContainer
import com.the9grounds.aeadditions.parts.AbstractDisplayPart
import appeng.items.parts.PartModels
import com.the9grounds.aeadditions.container.ContainerOpener
import com.the9grounds.aeadditions.container.Locator
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.container.ContainerType
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.vector.Vector3d

class ChemicalTerminalPart(itemStack: ItemStack) : AbstractDisplayPart(itemStack), ITerminalHost {
    
    companion object {
        @PartModels val MODEL_OFF: ResourceLocation = ResourceLocation(AEAdditions.ID, "part/chemical_terminal_off")

        @PartModels val MODEL_ON: ResourceLocation = ResourceLocation(AEAdditions.ID, "part/chemical_terminal_on")
    }
    
    val MODELS_OFF = PartModel(MODEL_BASE, MODEL_OFF, MODEL_STATUS_OFF)
    val MODELS_ON = PartModel(MODEL_BASE, MODEL_ON, MODEL_STATUS_ON)
    val MODELS_HAS_CHANNEL = PartModel(MODEL_BASE, MODEL_ON, MODEL_STATUS_HAS_CHANNEL)

    override fun onPartActivate(player: PlayerEntity?, hand: Hand?, pos: Vector3d?): Boolean {
        if (!super.onPartActivate(player, hand, pos)) {
            ContainerOpener.openContainer(getContainerType(), player!!, Locator.forPart(this))
        }

        return true
    }
    
    fun getContainerType(): ContainerType<*> = ChemicalTerminalContainer.TYPE

    override fun getStaticModels(): IPartModel = this.selectModel(MODELS_OFF, MODELS_ON, MODELS_HAS_CHANNEL)
    
    override fun <T : IAEStack<T>?> getInventory(channel: IStorageChannel<T>?): IMEMonitor<T>? {
        return try {
            this.proxy.storage.getInventory(channel)
        } catch (e: GridAccessException) {
            null
        }
    }

    override val hasCustomInventoryName: Boolean
        get() = true
}