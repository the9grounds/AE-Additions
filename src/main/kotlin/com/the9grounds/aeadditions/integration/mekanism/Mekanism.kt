package com.the9grounds.aeadditions.integration.mekanism

import mekanism.api.chemical.Chemical
import mekanism.api.chemical.ChemicalStack
import mekanism.api.chemical.IChemicalHandler
import mekanism.api.chemical.gas.Gas
import mekanism.api.chemical.gas.GasStack
import mekanism.api.chemical.infuse.InfuseType
import mekanism.api.chemical.infuse.InfusionStack
import mekanism.api.chemical.pigment.Pigment
import mekanism.api.chemical.pigment.PigmentStack
import mekanism.api.chemical.slurry.Slurry
import mekanism.api.chemical.slurry.SlurryStack
import mekanism.common.Mekanism
import mekanism.common.capabilities.Capabilities
import mekanism.common.util.StorageUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.inventory.container.PlayerContainer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.PacketBuffer
import net.minecraft.util.ResourceLocation

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
    
    fun capabilityFromChemicalStorageItem(itemStack: ItemStack): IChemicalHandler<*, *>? {
        val chemicalStack = getStoredChemicalStackFromStack(itemStack)?: return null
        
        return getCapabilityFromChemicalStorageItemForChemicalStack(itemStack, chemicalStack)
    }
    
    fun getCapabilityFromChemicalStorageItemForChemicalStack(itemStack: ItemStack, chemicalStack: ChemicalStack<*>): IChemicalHandler<*, *>? {
        return when(chemicalStack) {
            is GasStack -> itemStack.getCapability(Capabilities.GAS_HANDLER_CAPABILITY).resolve().get()
            is InfusionStack -> itemStack.getCapability(Capabilities.INFUSION_HANDLER_CAPABILITY).resolve().get()
            is PigmentStack -> itemStack.getCapability(Capabilities.PIGMENT_HANDLER_CAPABILITY).resolve().get()
            is SlurryStack -> itemStack.getCapability(Capabilities.SLURRY_HANDLER_CAPABILITY).resolve().get()
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
        return Minecraft.getInstance().getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(spriteLocation)
    }

    fun getRed(color: Int): Float {
        return (color shr 16 and 0xFF) / 255.0f
    }

    fun getGreen(color: Int): Float {
        return (color shr 8 and 0xFF) / 255.0f
    }

    fun getBlue(color: Int): Float {
        return (color and 0xFF) / 255.0f
    }
}