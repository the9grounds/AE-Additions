package com.the9grounds.aeadditions.parts.fluid

import appeng.api.config.Actionable
import appeng.api.parts.IPartModel
import appeng.api.storage.IStorageChannel
import appeng.api.storage.data.IAEFluidStack
import appeng.fluids.items.FluidDummyItem
import appeng.fluids.util.AEFluidStack
import appeng.items.parts.PartModels
import appeng.me.helpers.PlayerSource
import appeng.parts.PartModel
import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.Logger
import com.the9grounds.aeadditions.integration.appeng.AppEng
import com.the9grounds.aeadditions.network.NetworkManager
import com.the9grounds.aeadditions.network.packets.UpdateSlotInHandPacket
import com.the9grounds.aeadditions.parts.AbstractConversionMonitorPart
import com.the9grounds.aeadditions.util.FluidSounds
import com.the9grounds.aeadditions.util.StorageChannels
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.inventory.EquipmentSlotType
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.TranslationTextComponent
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.FluidUtil
import net.minecraftforge.fluids.capability.IFluidHandler

class FluidConversionMonitorPart(itemStack: ItemStack) : AbstractConversionMonitorPart<IAEFluidStack>(itemStack) {

    companion object {
        @PartModels
        val MODEL_OFF = ResourceLocation(AEAdditions.ID, "part/fluid/conversion_monitor_off")

        @PartModels
        val MODEL_ON = ResourceLocation(AEAdditions.ID, "part/fluid/conversion_monitor_on")

        @PartModels
        val MODEL_LOCKED_OFF = ResourceLocation(AEAdditions.ID, "part/fluid/conversion_monitor_locked_off")

        @PartModels
        val MODEL_LOCKED_ON = ResourceLocation(AEAdditions.ID, "part/fluid/conversion_monitor_locked_on")

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
    
    override fun canExtractOrInsertFromHeldItem(player: PlayerEntity?, hand: Hand?, extract: Boolean): Boolean {
        
        val localWatchedItem = watchedItem
        
        if (localWatchedItem == null && !extract) {
            return false
        }
        
        val heldItem = player!!.getHeldItem(hand)
        
        if (heldItem.isEmpty) {
            return false
        }
        
        val fluidInItem = FluidUtil.getFluidContained(heldItem)
        
        if (extract) {
            if (!fluidInItem.isPresent) {
                return false
            }
            
            return true
        }
        
        if (fluidInItem.isPresent && fluidInItem.get().fluid == localWatchedItem!!.fluid) {
            return true
        }
        
        return !fluidInItem.isPresent
    }

    override fun insertStack(player: PlayerEntity?, hand: Hand?) {

        val localWatchedItem = watchedItem ?: return

        val heldItem = player!!.getHeldItem(hand)

        if (heldItem.isEmpty) {
            return
        }
        
        val fluidHandler = FluidUtil.getFluidHandler(heldItem).orElse(null) ?: return

        val monitor = proxy.storage.getInventory(getStorageChannel())
        val source = PlayerSource(player, this)
        
        val stack = localWatchedItem.copy()

        stack.stackSize = Int.MAX_VALUE.toLong()
        
        val amountAllowed = fluidHandler.fill(stack.fluidStack, IFluidHandler.FluidAction.SIMULATE)
        stack.stackSize = amountAllowed.toLong()
        
        val canPull = AppEng.API!!.storage().poweredExtraction(powerSource, monitor, stack, source, Actionable.SIMULATE)
        
        if (canPull == null || canPull.stackSize < 1) {
            return
        }
        
        val canFill = fluidHandler.fill(canPull.fluidStack, IFluidHandler.FluidAction.SIMULATE)
        
        if (canFill == 0) {
            return
        }

        stack.stackSize = canFill.toLong()
        val pulled = AppEng.API!!.storage().poweredExtraction(powerSource, monitor, stack, source, Actionable.MODULATE)
        
        if (pulled == null || pulled.stackSize < 1) {
            Logger.info("Unable to pull fluid out of the ME system even though the simulation said yes")
            
            return
        }
        
        val filled = fluidHandler.fill(pulled.fluidStack, IFluidHandler.FluidAction.EXECUTE)
        
        if (filled != canFill) {
            Logger.warn("Filled is different than can fill for {}", heldItem.displayName)
        }
        
        if (filled != pulled.stackSize.toInt()) {
            val insert = pulled.stackSize.toInt() - filled
            
            val stackToInsert = stack.copy()
            stackToInsert.stackSize = insert.toLong()
            
            monitor.injectItems(stackToInsert, Actionable.MODULATE, source)
            
            Logger.warn("Pulled amount did not equal filled, inserting difference back into system")
        }
        
        FluidSounds.playFillSound(player, pulled.fluidStack)

        val slot = when(hand) {
            Hand.MAIN_HAND -> EquipmentSlotType.MAINHAND
            else -> EquipmentSlotType.OFFHAND
        }
        player.setItemStackToSlot(slot, fluidHandler.container)

        NetworkManager.sendTo(UpdateSlotInHandPacket(slot, fluidHandler.container), player as ServerPlayerEntity)
    }

    override fun extractStack(player: PlayerEntity?, hand: Hand?) {
        val heldItem = player!!.getHeldItem(hand)

        if (heldItem.isEmpty) {
            return
        }

        val fluidHandler = FluidUtil.getFluidHandler(heldItem).orElse(null) ?: return

        val monitor = proxy.storage.getInventory(getStorageChannel())
        val source = PlayerSource(player, this)
        
        val extract = fluidHandler.drain(Int.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE)
        
        if (extract.isEmpty || extract.amount < 1) {
            return
        }
        
        val notStorable = AppEng.API!!.storage().poweredInsert(powerSource, monitor, AEFluidStack.fromFluidStack(extract), source, Actionable.SIMULATE)

        if (notStorable != null && notStorable.stackSize > 0) {
            val toStore = (extract.amount - notStorable.stackSize).toInt()
            val storable: FluidStack = fluidHandler.drain(toStore, IFluidHandler.FluidAction.SIMULATE)
            if (storable.isEmpty || storable.amount == 0) {
                return
            } else {
                extract.amount = storable.amount
            }
        }
        
        val drained = fluidHandler.drain(extract, IFluidHandler.FluidAction.EXECUTE)
        
        extract.amount = drained.amount
        
        val notInserted = AppEng.API!!.storage().poweredInsert(powerSource, monitor, AEFluidStack.fromFluidStack(extract), source, Actionable.MODULATE)
        
        if (notInserted != null && notInserted.stackSize > 0) {
            Logger.error("Fluid item {} reported a different possible amount to drain than it actually provided.", heldItem.displayName)
            
            fluidHandler.fill(notInserted.fluidStack, IFluidHandler.FluidAction.EXECUTE)
        }
        
        val slot = when(hand) {
            Hand.MAIN_HAND -> EquipmentSlotType.MAINHAND
            else -> EquipmentSlotType.OFFHAND
        }
        player.setItemStackToSlot(slot, fluidHandler.container)
        
        NetworkManager.sendTo(UpdateSlotInHandPacket(slot, fluidHandler.container), player as ServerPlayerEntity)
        
        FluidSounds.playEmptySound(player, extract)
    }

    override fun getWatchedItemName(): String {
        val itemStack = watchedItem!!.asItemStackRepresentation()

        val item = itemStack.item as FluidDummyItem

        val fluidStack = item.getFluidStack(itemStack)

        return TranslationTextComponent(fluidStack.translationKey).string
    }

    override fun getStackFromHeldItem(player: PlayerEntity?, hand: Hand?): IAEFluidStack? {
        val heldItem = player!!.getHeldItem(hand) ?: return null

        val fluidStack = FluidUtil.getFluidContained(heldItem)?.orElse(null) ?: return null

        return AEFluidStack.fromFluidStack(fluidStack)
    }

    override fun getStorageChannel(): IStorageChannel<IAEFluidStack> = StorageChannels.FLUID
}