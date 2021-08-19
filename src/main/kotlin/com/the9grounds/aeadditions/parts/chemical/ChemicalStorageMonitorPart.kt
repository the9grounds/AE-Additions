package com.the9grounds.aeadditions.parts.chemical

import appeng.api.parts.IPartModel
import appeng.api.storage.IStorageChannel
import appeng.items.parts.PartModels
import appeng.parts.PartModel
import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.api.chemical.IAEChemicalStack
import com.the9grounds.aeadditions.integration.mekanism.Mekanism
import com.the9grounds.aeadditions.integration.mekanism.chemical.AEChemicalStack
import com.the9grounds.aeadditions.item.ChemicalDummyItem
import com.the9grounds.aeadditions.parts.AbstractMonitorPart
import com.the9grounds.aeadditions.util.StorageChannels
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.TranslationTextComponent

class ChemicalStorageMonitorPart(itemStack: ItemStack) :
    AbstractMonitorPart<IAEChemicalStack>(itemStack) {

    companion object {
        @PartModels
        val MODEL_OFF = ResourceLocation(AEAdditions.ID, "part/chemical/storage_monitor_off")

        @PartModels
        val MODEL_ON = ResourceLocation(AEAdditions.ID, "part/chemical/storage_monitor_on")

        @PartModels
        val MODEL_LOCKED_OFF = ResourceLocation(AEAdditions.ID, "part/chemical/storage_monitor_locked_off")

        @PartModels
        val MODEL_LOCKED_ON = ResourceLocation(AEAdditions.ID, "part/chemical/storage_monitor_locked_on")

        val MODELS_ON = PartModel(MODEL_BASE, MODEL_ON, MODEL_STATUS_ON)
        val MODELS_OFF = PartModel(MODEL_BASE, MODEL_OFF, MODEL_STATUS_OFF)
        val MODELS_HAS_CHANNEL = PartModel(MODEL_BASE, MODEL_ON, MODEL_STATUS_HAS_CHANNEL)

        val MODELS_LOCKED_ON = PartModel(MODEL_BASE, MODEL_LOCKED_ON, MODEL_STATUS_ON)
        val MODELS_LOCKED_OFF = PartModel(MODEL_BASE, MODEL_LOCKED_OFF, MODEL_STATUS_OFF)
        val MODELS_LOCKED_HAS_CHANNEL = PartModel(MODEL_BASE, MODEL_LOCKED_ON, MODEL_STATUS_HAS_CHANNEL)
    }

    override fun getStaticModels(): IPartModel = selectModel(
        MODELS_ON,
        MODELS_OFF,
        MODELS_HAS_CHANNEL,
        MODELS_LOCKED_ON,
        MODELS_LOCKED_OFF,
        MODELS_LOCKED_HAS_CHANNEL
    )

    override fun getStackFromHeldItem(player: PlayerEntity?, hand: Hand?): IAEChemicalStack? {
        
        val heldItem = player!!.getHeldItem(hand) ?: return null

        val chemicalStack = Mekanism.getStoredChemicalStackFromStack(heldItem) ?: return null
        
        return AEChemicalStack(chemicalStack)
    }

    override fun getStorageChannel(): IStorageChannel<IAEChemicalStack> = StorageChannels.CHEMICAL

    override fun getWatchedItemName(): String {
        val itemStack = watchedItem!!.asItemStackRepresentation()
        
        val item = itemStack.item as ChemicalDummyItem
        
        val chemicalStack = item.getChemicalStack(itemStack)
        
        return TranslationTextComponent(chemicalStack.getType().getTranslationKey()).string
    }
}