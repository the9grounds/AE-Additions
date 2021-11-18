package com.the9grounds.aeadditions.parts.chemical

import appeng.api.config.Actionable
import appeng.api.networking.IGridNode
import appeng.api.networking.ticking.IGridTickable
import appeng.api.networking.ticking.TickRateModulation
import appeng.api.networking.ticking.TickingRequest
import appeng.api.parts.IPartCollisionHelper
import appeng.api.parts.IPartModel
import appeng.api.util.AECableType
import appeng.items.parts.PartModels
import appeng.me.GridAccessException
import appeng.me.helpers.MachineSource
import appeng.parts.PartModel
import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.container.ContainerOpener
import com.the9grounds.aeadditions.container.Locator
import com.the9grounds.aeadditions.container.chemical.ChemicalIOContainer
import com.the9grounds.aeadditions.core.TickRates
import com.the9grounds.aeadditions.integration.Mods
import com.the9grounds.aeadditions.integration.appeng.AppEng
import com.the9grounds.aeadditions.integration.mekanism.Mekanism
import com.the9grounds.aeadditions.integration.mekanism.chemical.AEChemicalStack
import com.the9grounds.aeadditions.parts.SharedIOBus
import com.the9grounds.aeadditions.util.StorageChannels
import mekanism.api.Action
import mekanism.api.chemical.Chemical
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.vector.Vector3d

class ChemicalExportBus(itemStack: ItemStack) : SharedIOBus(itemStack), IGridTickable {
    
    companion object {
        val MODEL_BASE = ResourceLocation(AEAdditions.ID, "part/chemical/export_bus_base")
        
        @PartModels val MODELS_OFF = PartModel(MODEL_BASE, ResourceLocation(AEAdditions.ID, "part/chemical/export_bus_off"))
        @PartModels val MODELS_ON = PartModel(MODEL_BASE, ResourceLocation(AEAdditions.ID, "part/chemical/export_bus_on"))
        @PartModels val MODELS_HAS_CHANNEL = PartModel(MODEL_BASE, ResourceLocation(AEAdditions.ID, "part/chemical/export_bus_has_channel"))
    }

    override fun getStaticModels(): IPartModel {
        return when {
            isActive && isPowered -> MODELS_HAS_CHANNEL
            isPowered -> MODELS_ON
            else -> MODELS_OFF
        }
    }

    override fun getBoxes(bch: IPartCollisionHelper?) {
        bch!!.addBox(6.0, 6.0, 11.0, 10.0, 10.0, 13.0)
        bch.addBox(5.0, 5.0, 13.0, 11.0, 11.0, 14.0)
        bch.addBox(4.0, 4.0, 14.0, 12.0, 12.0, 16.0)
    }
    
    override fun getCableConnectionLength(cable: AECableType?): Float = 5f

    override fun onPartActivate(player: PlayerEntity?, hand: Hand?, pos: Vector3d?): Boolean {
        
        if (!isRemote) {
            ContainerOpener.openContainer(ChemicalIOContainer.EXPORT_BUS, player!!, Locator.forPart(this))
        }
        
        return true
    }

    override fun getTickingRequest(node: IGridNode): TickingRequest = TickingRequest(TickRates.ChemicalExportBus.min, TickRates.ChemicalExportBus.max, isSleeping, false)

    override fun tickingRequest(node: IGridNode, ticksSinceLastCall: Int): TickRateModulation {
        return if (canDoBusWork() && Mods.MEKANISM.isEnabled) doWork() else TickRateModulation.IDLE
    }

    override fun doWork(): TickRateModulation {
        if (!canDoBusWork()) {
            return TickRateModulation.IDLE
        }
        
        if (facingChemicalTank == null) {
            return TickRateModulation.SLOWER
        }
        
        try {
            val alreadyUsedChemicals = mutableMapOf<Chemical<*>, Boolean>()
            var didWork = false
            for (item in getChemicalConfig()) {
                if (item == null) {
                    continue
                }
                
                if (alreadyUsedChemicals.containsKey(item)) {
                    continue
                }
                
                alreadyUsedChemicals[item] = true
                
                val aeChemicalStack = AEChemicalStack(item)
                
                aeChemicalStack.stackSize = calculateThroughput()
                
                val extracted = AppEng.API!!.storage().poweredExtraction(powerSource, proxy.storage.getInventory(StorageChannels.CHEMICAL), aeChemicalStack, MachineSource(this), Actionable.SIMULATE)
                    ?: continue

                val remaining = Mekanism.insertChemicalForChemicalCapability(facingChemicalTank!!, side!!.facing.opposite, extracted.getChemicalStack(), Action.EXECUTE)
                
                if (remaining != null && remaining.getAmount() > 0) {
                    extracted.stackSize -= remaining.getAmount()
                }
                
                AppEng.API!!.storage().poweredExtraction(powerSource, proxy.storage.getInventory(StorageChannels.CHEMICAL), extracted, MachineSource(this), Actionable.MODULATE)
                
                didWork = true
            }
            
            if (didWork) {
                return TickRateModulation.FASTER
            }
            
            return TickRateModulation.SLOWER
        } catch (e: GridAccessException) {
            //
        }

        return TickRateModulation.SLEEP
    }
}