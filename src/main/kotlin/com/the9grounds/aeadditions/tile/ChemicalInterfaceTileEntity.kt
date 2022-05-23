package com.the9grounds.aeadditions.tile

import appeng.api.networking.IGridHost
import appeng.api.networking.IGridNode
import appeng.api.networking.security.IActionHost
import appeng.api.networking.ticking.IGridTickable
import appeng.api.networking.ticking.TickRateModulation
import appeng.api.networking.ticking.TickingRequest
import appeng.api.util.AECableType
import appeng.api.util.AEPartLocation
import com.the9grounds.aeadditions.integration.Mods
import com.the9grounds.aeadditions.integration.appeng.AppEng
import com.the9grounds.aeadditions.integration.mekanism.holder.ChemicalInterfaceHolder
import com.the9grounds.aeadditions.me.AEAGridBlock
import com.the9grounds.aeadditions.registries.Tiles
import mekanism.api.chemical.Chemical
import mekanism.api.chemical.IChemicalTank
import mekanism.api.chemical.gas.Gas
import mekanism.api.chemical.gas.IGasTank
import mekanism.api.chemical.infuse.IInfusionTank
import mekanism.api.chemical.infuse.InfuseType
import mekanism.api.chemical.merged.MergedChemicalTank
import mekanism.api.chemical.pigment.IPigmentTank
import mekanism.api.chemical.pigment.Pigment
import mekanism.api.chemical.slurry.ISlurryTank
import mekanism.api.chemical.slurry.Slurry
import mekanism.common.capabilities.DynamicHandler.InteractPredicate
import mekanism.common.capabilities.chemical.ChemicalTankChemicalTank
import mekanism.common.capabilities.chemical.dynamic.DynamicChemicalHandler
import mekanism.common.capabilities.resolver.ICapabilityResolver
import mekanism.common.capabilities.resolver.manager.ChemicalHandlerManager
import mekanism.common.tier.ChemicalTankTier
import net.minecraft.util.Direction
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.fml.loading.FMLEnvironment

class ChemicalInterfaceTileEntity : AbstractAEATileEntity(Tiles.CHEMICAL_INTERFACE.get()), IGridTickable, IGridHost, IActionHost {
    
    private var gasHandlerManager: ChemicalHandlerManager.GasHandlerManager? = null
    private var infusionHandlerManager: ChemicalHandlerManager.InfusionHandlerManager? = null
    private var slurryHandlerManager: ChemicalHandlerManager.SlurryHandlerManager? = null
    private var pigmentHandlerManager: ChemicalHandlerManager.PigmentHandlerManager? = null
    
    val gridBlock = AEAGridBlock(this)

    private var node: IGridNode? = null
    private var isFirstGetGridNode = true
    
    private var capabilityMap: MutableMap<Capability<*>, ICapabilityResolver> = mutableMapOf()
    
    var chemicalTanks: MutableList<MergedChemicalTank> = mutableListOf()
    var chemicalConfig: MutableList<Chemical<*>?> = mutableListOf()
    
    init {
        if (Mods.MEKANISM.isEnabled) {
            gasHandlerManager = ChemicalHandlerManager.GasHandlerManager(ChemicalInterfaceHolder(this, Gas::class.java), DynamicChemicalHandler.DynamicGasHandler(
                this::getGasTanks,
                getExtractPredicate(),
                getInsertPredicate(),
                null
            ))
            infusionHandlerManager = ChemicalHandlerManager.InfusionHandlerManager(ChemicalInterfaceHolder(this, InfuseType::class.java), DynamicChemicalHandler.DynamicInfusionHandler(
                this::getInfusionTanks,
                getExtractPredicate(),
                getInsertPredicate(),
                null
            ))
            slurryHandlerManager = ChemicalHandlerManager.SlurryHandlerManager(ChemicalInterfaceHolder(this, Slurry::class.java), DynamicChemicalHandler.DynamicSlurryHandler(
                this::getSlurryTanks,
                getExtractPredicate(),
                getInsertPredicate(),
                null
            ))
            pigmentHandlerManager = ChemicalHandlerManager.PigmentHandlerManager(ChemicalInterfaceHolder(this, Pigment::class.java), DynamicChemicalHandler.DynamicPigmentHandler(
                this::getPigmentTanks,
                getExtractPredicate(),
                getInsertPredicate(),
                null
            ))

            addCapabilityResolverToMap(pigmentHandlerManager!!)
            addCapabilityResolverToMap(slurryHandlerManager!!)
            addCapabilityResolverToMap(infusionHandlerManager!!)
            addCapabilityResolverToMap(gasHandlerManager!!)
            
            for (i in 0 until 6) {
                chemicalTanks.add(i, ChemicalTankChemicalTank.create(ChemicalTankTier.ULTIMATE, null))
                chemicalConfig.add(i, null)
            }
        }
    }
    
    private fun addCapabilityResolverToMap(resolver: ICapabilityResolver) {
        for (capability in resolver.supportedCapabilities) {
            capabilityMap?.set(capability, resolver)
        }
    }

    override fun getGridNode(dir: AEPartLocation): IGridNode? {
        if (FMLEnvironment.dist.isClient && (getWorld() == null) || getWorld()!!.isRemote) {
            return null
        }
        
        if (isFirstGetGridNode) {
            isFirstGetGridNode = false
            actionableNode?.updateState()
        }
        
        return node
    }

    override fun getCableConnectionType(dir: AEPartLocation): AECableType = AECableType.SMART

    override fun securityBreak() {
        //
    }

    override fun getTickingRequest(node: IGridNode): TickingRequest {
        return TickingRequest(0, 20, false, false)
    }

    override fun tickingRequest(node: IGridNode, ticksSinceLastCall: Int): TickRateModulation {
        return TickRateModulation.IDLE
    }

    private fun getGasTanks(side: Direction?): List<IGasTank?>? {
        return gasHandlerManager!!.getContainers(side)
    }

    private fun getInfusionTanks(side: Direction?): List<IInfusionTank?>? {
        return infusionHandlerManager!!.getContainers(side)
    }

    private fun getPigmentTanks(side: Direction?): List<IPigmentTank?>? {
        return pigmentHandlerManager!!.getContainers(side)
    }

    private fun getSlurryTanks(side: Direction?): List<ISlurryTank?>? {
        return slurryHandlerManager!!.getContainers(side)
    }
    
    fun getTankForSide(direction: Direction): IChemicalTank<*, *>? {
        val current = chemicalTanks[direction.ordinal].current
        
        if (current === MergedChemicalTank.Current.EMPTY) {
            return null
        }
        
        return chemicalTanks[direction.ordinal].getTankFromCurrent(current)
    }

    protected fun getExtractPredicate(): InteractPredicate? {
        return InteractPredicate { tank: Int, side: Direction? ->
            if (side == null) {
                //Note: We return true here, but insertion isn't actually allowed and gets blocked by the read only handler
                return@InteractPredicate false
            }
            //If we have a side only allow inserting if our connection allows it
            chemicalConfig[side.ordinal] !== null
        }
    }

    protected fun getInsertPredicate(): InteractPredicate? {
        return InteractPredicate { tank: Int, side: Direction? ->
            if (side == null) {
                //Note: We return true here, but insertion isn't actually allowed and gets blocked by the read only handler
                return@InteractPredicate false
            }
            //If we have a side only allow inserting if our connection allows it
            true
        }
    }
    
    val powerUsage: Double
    get() = 1.0

    override fun getActionableNode(): IGridNode? {
        if (FMLEnvironment.dist.isClient) {
            return node
        }
        
        if (node == null) {
            node = AppEng.API!!.grid().createGridNode(gridBlock)
        }
        
        return node
    }

    override fun <T : Any?> getCapability(cap: Capability<T>): LazyOptional<T> {
        if (capabilityMap.containsKey(cap)) {
            return capabilityMap.get(cap)!!.resolve(cap, null)
        }
        
        return super.getCapability(cap)
    }

    override fun <T : Any?> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T> {
        if (capabilityMap.containsKey(cap)) {
            return capabilityMap.get(cap)!!.resolve(cap, side)
        }

        return super.getCapability(cap, side)
    }
}