package com.the9grounds.aeadditions.integration.mekanism

import appeng.api.config.Actionable
import appeng.api.networking.energy.IEnergySource
import appeng.api.networking.security.IActionSource
import appeng.api.storage.IMEMonitor
import com.the9grounds.aeadditions.Logger
import com.the9grounds.aeadditions.api.chemical.IAEChemicalStack
import com.the9grounds.aeadditions.integration.appeng.AppEng
import com.the9grounds.aeadditions.integration.mekanism.chemical.AEChemicalStack
import mekanism.api.Action
import mekanism.api.chemical.Chemical
import mekanism.api.chemical.ChemicalStack
import mekanism.api.chemical.IChemicalHandler
import mekanism.api.chemical.gas.Gas
import mekanism.api.chemical.gas.GasStack
import mekanism.api.chemical.gas.IGasHandler
import mekanism.api.chemical.infuse.IInfusionHandler
import mekanism.api.chemical.infuse.InfuseType
import mekanism.api.chemical.infuse.InfusionStack
import mekanism.api.chemical.pigment.IPigmentHandler
import mekanism.api.chemical.pigment.Pigment
import mekanism.api.chemical.pigment.PigmentStack
import mekanism.api.chemical.slurry.ISlurryHandler
import mekanism.api.chemical.slurry.Slurry
import mekanism.api.chemical.slurry.SlurryStack
import mekanism.common.capabilities.Capabilities
import mekanism.common.util.StorageUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.inventory.container.PlayerContainer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.PacketBuffer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.capabilities.Capability

object Mekanism {
    fun getType(chemical: Chemical<*>): String {
        return when(chemical) {
            is Gas -> "gas"
            is Pigment -> "pigment"
            is InfuseType -> "infusion"
            is Slurry -> "slurry"
            else -> throw RuntimeException("Invalid chemical type")
        }
    }
    
    fun readChemicalFromNbt(nbt: CompoundNBT): Chemical<*> {
        return when(nbt.getString("chemicalType")) {
            "gas" -> Gas.readFromNBT(nbt)
            "pigment" -> Pigment.readFromNBT(nbt)
            "infusion" -> InfuseType.readFromNBT(nbt)
            "slurry" -> Slurry.readFromNBT(nbt)
            else -> throw RuntimeException("Invalid chemical type")
        }
    }
    
    fun readChemicalStackFromNbt(nbt: CompoundNBT): ChemicalStack<*> {
        return when(nbt.getString("chemicalType")) {
            "gas" -> GasStack.readFromNBT(nbt)
            "pigment" -> PigmentStack.readFromNBT(nbt)
            "infusion" -> InfusionStack.readFromNBT(nbt)
            "slurry" -> SlurryStack.readFromNBT(nbt)
            else -> throw RuntimeException("Invalid chemical type")
        }
    }
    
    fun writeChemicalWithTypeToNbt(nbt: CompoundNBT, chemical: Chemical<*>) {
        val chemicalType = getType(chemical)
        
        nbt.putString("chemicalType", chemicalType)
        chemical.write(nbt)
    }
    
    fun capabilityFromChemicalStorageItem(itemStack: ItemStack): IChemicalHandler<*, *>? {
        val chemicalStack = getStoredChemicalStackFromStack(itemStack)?: return null
        
        return getCapabilityFromChemicalStorageItemForChemicalStack(itemStack, chemicalStack)
    }
    
    fun getCapabilityFromChemicalStorageItemForChemicalStack(itemStack: ItemStack, chemicalStack: ChemicalStack<*>): IChemicalHandler<*, *>? {
        return when(chemicalStack) {
            is GasStack -> getCapabilityFromChemicalStorageItem(itemStack, Capabilities.GAS_HANDLER_CAPABILITY)
            is InfusionStack -> getCapabilityFromChemicalStorageItem(itemStack, Capabilities.INFUSION_HANDLER_CAPABILITY)
            is PigmentStack -> getCapabilityFromChemicalStorageItem(itemStack, Capabilities.PIGMENT_HANDLER_CAPABILITY)
            is SlurryStack -> getCapabilityFromChemicalStorageItem(itemStack, Capabilities.SLURRY_HANDLER_CAPABILITY)
            else -> null
        }
    }
    
    fun isItemStackAChemicalContainer(itemStack: ItemStack): Boolean {
        return when {
            getCapabilityFromChemicalStorageItem(itemStack, Capabilities.GAS_HANDLER_CAPABILITY) != null -> true
            getCapabilityFromChemicalStorageItem(itemStack, Capabilities.INFUSION_HANDLER_CAPABILITY) != null -> true
            getCapabilityFromChemicalStorageItem(itemStack, Capabilities.PIGMENT_HANDLER_CAPABILITY) != null -> true
            getCapabilityFromChemicalStorageItem(itemStack, Capabilities.SLURRY_HANDLER_CAPABILITY) != null -> true
            else -> false
        }
    }
    
    fun <T: IChemicalHandler<*, *>> getCapabilityFromChemicalStorageItem(itemStack: ItemStack, capability: Capability<T>): IChemicalHandler<*, *>? {
        return itemStack.getCapability(capability).resolve().orElse(null)
    }
    
    fun insertChemicalIntoContainer(chemicalStack: IAEChemicalStack, heldItem: ItemStack, handler: IChemicalHandler<*, *>, monitor: IMEMonitor<IAEChemicalStack>, powerSource: IEnergySource, source: IActionSource): Boolean {
        val stack = chemicalStack.copy()
        
        stack.stackSize = Integer.MAX_VALUE.toLong()

        val maxFill = fillContainer(stack, handler) ?: return false

        stack.stackSize = maxFill.getAmount()

        val canPull = AppEng.API!!.storage().poweredExtraction(powerSource, monitor, stack, source, Actionable.SIMULATE)

        if (canPull == null || canPull.stackSize < 1) {
            return false
        }

        val cannotFill = fillContainer(canPull, handler) ?: return false

        if (cannotFill.getAmount() == 0L) {
            return false
        }

        stack.stackSize = canPull.stackSize - cannotFill.getAmount()

        val pulled = AppEng.API!!.storage().poweredExtraction(powerSource, monitor, stack, source, Actionable.MODULATE)

        if (pulled == null || pulled.stackSize < 1) {
            Logger.warn("Unable to pull chemical from ME system even though simulation said yes")
            return false
        }

        val notFilled = fillContainer(pulled, handler, Action.EXECUTE)

        if (notFilled == null || notFilled.getAmount() == pulled.stackSize) {
            Logger.warn("Unable to insert chemical into held tank, even though simulation worked, reinserting into ME system")

            monitor!!.injectItems(pulled, Actionable.MODULATE, source)

            return false
        }

        if (notFilled.getAmount() != 0L) {
            monitor!!.injectItems(AEChemicalStack(notFilled), Actionable.MODULATE, source)
        }

        if (notFilled.getAmount() != cannotFill.getAmount()) {
            Logger.warn("Filled is different than can fill for {}", heldItem.displayName)
        }

        return true
    }
    
    fun extractChemicalFromContainer(heldItem: ItemStack, monitor: IMEMonitor<IAEChemicalStack>, powerSource: IEnergySource, source: IActionSource): Boolean {
        val handler = capabilityFromChemicalStorageItem(heldItem)?: return false

        val extracted = extractFromContainer(Int.MAX_VALUE.toLong(), handler)

        if (extracted == null || extracted.isEmpty() || extracted.getAmount() < 1) {
            return false
        }

        val notStorable = AppEng.API!!.storage().poweredInsert(powerSource, monitor, AEChemicalStack(extracted), source, Actionable.SIMULATE)

        if (notStorable != null && notStorable.stackSize > 0) {
            val toStore = extracted.getAmount() - notStorable.stackSize
            val storable = extractFromContainer(toStore, handler)

            if (storable == null || storable.isEmpty() || storable.getAmount() < 1) {
                return false
            }

            extracted.setAmount(storable.getAmount())
        }

        val drained = extractFromContainer(extracted.getAmount(), handler, Action.EXECUTE)

        extracted.setAmount(drained!!.getAmount())

        val notInserted = AppEng.API!!.storage().poweredInsert(powerSource, monitor, AEChemicalStack(extracted), source, Actionable.MODULATE)

        if (notInserted != null && notInserted.stackSize > 0) {
            Logger.warn("Chemical Item {} reported a different possible amount to drain than it actually provided.", heldItem.displayName)
        }
        
        return true
    }

    private fun extractFromContainer(
        amount: Long,
        handler: IChemicalHandler<*, *>,
        action: Action = Action.SIMULATE
    ): ChemicalStack<*>? {
        return when (handler) {
            is IGasHandler -> handler.extractChemical(amount, action)
            is IInfusionHandler -> handler.extractChemical(amount, action)
            is IPigmentHandler -> handler.extractChemical(amount, action)
            is ISlurryHandler -> handler.extractChemical(amount, action)
            else -> null
        }
    }

    private fun fillContainer(
        stack: IAEChemicalStack,
        handler: IChemicalHandler<*, *>,
        action: Action = Action.SIMULATE
    ): ChemicalStack<*>? {
        return when (handler) {
            is IGasHandler -> handler.insertChemical(stack.getChemicalStack() as GasStack, action)
            is IInfusionHandler -> handler.insertChemical(stack.getChemicalStack() as InfusionStack, action)
            is IPigmentHandler -> handler.insertChemical(stack.getChemicalStack() as PigmentStack, action)
            is ISlurryHandler -> handler.insertChemical(stack.getChemicalStack() as SlurryStack, action)
            else -> null
        }
    }
    
    fun getStoredChemicalStackFromStack(itemStack: ItemStack): ChemicalStack<*>? {
        val gasStack = StorageUtils.getStoredGasFromNBT(itemStack)
        val infusionStack = StorageUtils.getStoredInfusionFromNBT(itemStack)
        val pigmentStack = StorageUtils.getStoredPigmentFromNBT(itemStack)
        val slurryStack = StorageUtils.getStoredSlurryFromNBT(itemStack)
        return when(true) {
            !gasStack.isEmpty -> gasStack
            !infusionStack.isEmpty -> infusionStack
            !pigmentStack.isEmpty -> pigmentStack
            !slurryStack.isEmpty -> slurryStack
            else -> null
        }
    }
    
    fun readChemicalFromPacket(packet: PacketBuffer): Chemical<*> {
        val chemicalType = packet.readString()
        
        val chemicalName = packet.readString()
        
        return when(chemicalType) {
            "gas" -> Gas.getFromRegistry(ResourceLocation(chemicalName))
            "pigment" -> Pigment.getFromRegistry(ResourceLocation(chemicalName))
            "infusion" -> InfuseType.getFromRegistry(ResourceLocation(chemicalName))
            "slurry" -> Slurry.getFromRegistry(ResourceLocation(chemicalName))
            else -> throw RuntimeException("Invalid chemical type")
        }
    }

    fun getChemicalTexture(chemical: Chemical<*>): TextureAtlasSprite? {
        return getSprite(chemical.icon)
    }

    fun getSprite(spriteLocation: ResourceLocation?): TextureAtlasSprite? {
        return Minecraft.getInstance().getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE)
            .apply(spriteLocation)
    }
    
    fun insertChemicalForChemicalCapability(tileEntity: TileEntity, chemicalStack: ChemicalStack<*>, action: Action): ChemicalStack<*>? {
        return when(chemicalStack.getType()) {
            is Gas -> insertChemicalForCapability(tileEntity, Capabilities.GAS_HANDLER_CAPABILITY, chemicalStack as GasStack, action)
            is Pigment -> insertChemicalForCapability(tileEntity, Capabilities.PIGMENT_HANDLER_CAPABILITY, chemicalStack as PigmentStack, action)
            is Slurry -> insertChemicalForCapability(tileEntity, Capabilities.SLURRY_HANDLER_CAPABILITY, chemicalStack as SlurryStack, action)
            is InfuseType -> insertChemicalForCapability(tileEntity, Capabilities.INFUSION_HANDLER_CAPABILITY, chemicalStack as InfusionStack, action)
            else -> null
        }
    }
    
    fun insertChemical(tileEntity: TileEntity, chemicalStack: ChemicalStack<*>, action: Action): ChemicalStack<*>? {
        val gasStack = insertChemicalForCapability(tileEntity, Capabilities.GAS_HANDLER_CAPABILITY, chemicalStack as GasStack, action)
        if (gasStack == null || gasStack.getAmount() != chemicalStack.getAmount()) {
            return gasStack
        }
        val slurryStack = insertChemicalForCapability(tileEntity, Capabilities.SLURRY_HANDLER_CAPABILITY, chemicalStack as SlurryStack, action)
        if (slurryStack == null || slurryStack.getAmount() != chemicalStack.getAmount()) {
            return slurryStack
        }
        val infusionStack = insertChemicalForCapability(tileEntity, Capabilities.INFUSION_HANDLER_CAPABILITY, chemicalStack as InfusionStack, action)
        if (infusionStack == null || infusionStack.getAmount() != chemicalStack.getAmount()) {
            return infusionStack
        }
        val pigmentStack = insertChemicalForCapability(tileEntity, Capabilities.PIGMENT_HANDLER_CAPABILITY, chemicalStack as PigmentStack, action)
        if (pigmentStack == null || pigmentStack.getAmount() != chemicalStack.getAmount()) {
            return pigmentStack
        }

        return null
    }
    
    fun <T: IChemicalHandler<CHEMICAL, STACK>, CHEMICAL: Chemical<CHEMICAL>, STACK: ChemicalStack<CHEMICAL>> insertChemicalForCapability(tileEntity: TileEntity, capability: Capability<T>, chemicalStack: STACK, action: Action): ChemicalStack<*>? {
        return tileEntity.getCapability(capability).resolve().get().insertChemical(chemicalStack, action)
    }
    
    fun extractChemical(tileEntity: TileEntity, amount: Long, action: Action): ChemicalStack<*>? {
        val gasStack = extractChemicalForCapability(tileEntity, Capabilities.GAS_HANDLER_CAPABILITY, amount, action)
        if (gasStack != null) {
            return gasStack
        }
        val slurryStack = extractChemicalForCapability(tileEntity, Capabilities.SLURRY_HANDLER_CAPABILITY, amount, action)
        if (slurryStack != null) {
            return slurryStack
        }
        val infusionStack = extractChemicalForCapability(tileEntity, Capabilities.INFUSION_HANDLER_CAPABILITY, amount, action)
        if (infusionStack != null) {
            return infusionStack
        }
        val pigmentStack = extractChemicalForCapability(tileEntity, Capabilities.PIGMENT_HANDLER_CAPABILITY, amount, action)
        if (pigmentStack != null) {
            return pigmentStack
        }
        
        return null
    }
    
    fun <T: IChemicalHandler<*, *>> extractChemicalForCapability(tileEntity: TileEntity, capability: Capability<T>, amount: Long, action: Action): ChemicalStack<*>? {
        return tileEntity.getCapability(capability).resolve().get().extractChemical(amount, action)
    }
}