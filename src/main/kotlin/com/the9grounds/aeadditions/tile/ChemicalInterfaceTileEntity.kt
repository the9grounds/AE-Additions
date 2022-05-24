package com.the9grounds.aeadditions.tile

import appeng.api.config.Actionable
import appeng.api.networking.IGridHost
import appeng.api.networking.IGridNode
import appeng.api.networking.energy.IEnergyGrid
import appeng.api.networking.energy.IEnergySource
import appeng.api.networking.security.IActionHost
import appeng.api.networking.storage.IStorageGrid
import appeng.api.networking.ticking.IGridTickable
import appeng.api.networking.ticking.TickRateModulation
import appeng.api.networking.ticking.TickingRequest
import appeng.api.storage.IMEMonitor
import appeng.api.util.AECableType
import appeng.api.util.AEPartLocation
import appeng.core.sync.network.TargetPoint
import appeng.me.helpers.ChannelPowerSrc
import appeng.me.helpers.MachineSource
import com.the9grounds.aeadditions.api.IAEAChemicalConfig
import com.the9grounds.aeadditions.api.IAEAHasChemicalConfig
import com.the9grounds.aeadditions.api.chemical.IAEChemicalStack
import com.the9grounds.aeadditions.container.chemical.ChemicalInterfaceContainer
import com.the9grounds.aeadditions.core.AEAChemicalConfig
import com.the9grounds.aeadditions.integration.Mods
import com.the9grounds.aeadditions.integration.appeng.AppEng
import com.the9grounds.aeadditions.integration.mekanism.Mekanism
import com.the9grounds.aeadditions.integration.mekanism.chemical.AEChemicalStack
import com.the9grounds.aeadditions.me.AEAGridBlock
import com.the9grounds.aeadditions.network.NetworkManager
import com.the9grounds.aeadditions.network.packets.ChemicalInterfaceContentsChangedPacket
import com.the9grounds.aeadditions.registries.Tiles
import com.the9grounds.aeadditions.util.StorageChannels
import mekanism.api.Action
import mekanism.api.IContentsListener
import mekanism.api.chemical.Chemical
import mekanism.api.chemical.ChemicalStack
import mekanism.api.chemical.IChemicalHandler
import mekanism.api.chemical.IChemicalTank
import mekanism.api.chemical.gas.Gas
import mekanism.api.chemical.gas.GasStack
import mekanism.api.chemical.gas.IGasHandler
import mekanism.api.chemical.infuse.IInfusionHandler
import mekanism.api.chemical.infuse.InfuseType
import mekanism.api.chemical.infuse.InfusionStack
import mekanism.api.chemical.merged.MergedChemicalTank
import mekanism.api.chemical.pigment.IPigmentHandler
import mekanism.api.chemical.pigment.Pigment
import mekanism.api.chemical.pigment.PigmentStack
import mekanism.api.chemical.slurry.ISlurryHandler
import mekanism.api.chemical.slurry.Slurry
import mekanism.api.chemical.slurry.SlurryStack
import mekanism.api.inventory.AutomationType
import mekanism.common.capabilities.Capabilities
import mekanism.common.capabilities.chemical.ChemicalTankChemicalTank
import mekanism.common.tier.ChemicalTankTier
import net.minecraft.block.BlockState
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.Direction
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.fml.common.thread.SidedThreadGroups

class ChemicalInterfaceTileEntity : AbstractAEATileEntity(Tiles.CHEMICAL_INTERFACE.get()), IGridTickable, IGridHost, IActionHost, IAEAHasChemicalConfig {
    
    val gridBlock = AEAGridBlock(this)

    private var node: IGridNode? = null
    private var isFirstGetGridNode = true
    var isReady = false
    
    var chemicalHandlers: MutableList<MergedChemicalHandler> = mutableListOf()
    var _chemicalConfig = AEAChemicalConfig(6)
    
    init {
        if (Mods.MEKANISM.isEnabled) {
            for (i in 0 until 6) {
                chemicalHandlers.add(i, MergedChemicalHandler(i))
            }
        }
    }

    override fun getChemicalConfig(): IAEAChemicalConfig = _chemicalConfig

    override fun read(state: BlockState, nbt: CompoundNBT) {
        super.read(state, nbt)
        _chemicalConfig.readFromNbt(nbt, "config")
        
        val chemicalListNbt = nbt.getCompound("chemicalTankList")
        
        val size = chemicalListNbt.getInt("size")
        
        val chemicalList = ChemicalInterfaceContainer.ChemicalTankList(size)
        
        chemicalList.readFromNbt(chemicalListNbt)

        chemicalList.chemicalTanks.forEachIndexed { index, chemicalTank -> 
            if (chemicalTank != null) {
                when (chemicalTank.chemical) {
                    is Gas -> chemicalHandlers[index].tank.gasTank.stack = chemicalTank.chemical!!.getStack(chemicalTank!!.amount) as GasStack
                    is InfuseType -> chemicalHandlers[index].tank.infusionTank.stack = chemicalTank.chemical!!.getStack(chemicalTank!!.amount) as InfusionStack
                    is Pigment -> chemicalHandlers[index].tank.pigmentTank.stack = chemicalTank.chemical!!.getStack(chemicalTank!!.amount) as PigmentStack
                    is Slurry -> chemicalHandlers[index].tank.slurryTank.stack = chemicalTank.chemical!!.getStack(chemicalTank!!.amount) as SlurryStack
                }
            }
        }
        
        isReady = true
    }

    override fun write(compound: CompoundNBT): CompoundNBT {
        super.write(compound)
        _chemicalConfig.writeToNbt(compound, "config")

        val chemicalList = getChemicalTankListToSend()

        val chemicalListCompound = CompoundNBT()
        chemicalList.writeToNbt(chemicalListCompound)
        compound.put("chemicalTankList", chemicalListCompound)
        
        return compound
    }

    override fun getGridNode(dir: AEPartLocation): IGridNode? {
        if (Thread.currentThread().threadGroup == SidedThreadGroups.CLIENT) {
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
        var areTanksEmpty = true
        
        for (handler in chemicalHandlers) {
            if (handler.tank.current !== MergedChemicalTank.Current.EMPTY && !handler.tank.getTankFromCurrent(handler.tank.current).isEmpty()) {
                areTanksEmpty = false
            }
        }
        
        if (areTanksEmpty) {
            return TickRateModulation.IDLE
        }
        
        for (handler in chemicalHandlers) {
            if (handler.tank.current !== MergedChemicalTank.Current.EMPTY && !handler.tank.getTankFromCurrent(handler.tank.current).isEmpty()) {
                val currentStack = handler.tank.getTankFromCurrent(handler.tank.current).getStack()
                
                val simulated = AppEng.API!!.storage().poweredInsert(powerSource, chemicalStorage, AEChemicalStack(currentStack), MachineSource(this), Actionable.SIMULATE) as AEChemicalStack?
                
                if (simulated != null && simulated.stackSize === currentStack.getAmount()) {
                    continue
                }
                
                val notInserted = AppEng.API!!.storage().poweredInsert(powerSource, chemicalStorage, AEChemicalStack(currentStack), MachineSource(this), Actionable.MODULATE) as AEChemicalStack?
                
                if (notInserted === null) {
                    handler.tank.getTankFromCurrent(handler.tank.current).setEmpty()
                    continue
                }
                
                val amountInserted = currentStack.getAmount() - notInserted.stackSize
                
                if (amountInserted > 0) {
                    handler.tank.getTankFromCurrent(handler.tank.current).extract(amountInserted, Action.EXECUTE, AutomationType.INTERNAL)
                }
            }
        }
        
        return TickRateModulation.SAME;
    }
    
    val powerUsage: Double
    get() = 1.0

    override fun getActionableNode(): IGridNode? {
        if (Thread.currentThread().threadGroup === SidedThreadGroups.CLIENT) {
            return node
        }
        
        if (node == null) {
            node = AppEng.API!!.grid().createGridNode(gridBlock)
        }
        
        return node
    }

    override fun <T : Any?> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T> {
        if (side != null) {
            return when (cap) {
                Capabilities.GAS_HANDLER_CAPABILITY -> LazyOptional.of({ chemicalHandlers[side.ordinal].gasHandler as T })
                Capabilities.SLURRY_HANDLER_CAPABILITY -> LazyOptional.of({ chemicalHandlers[side.ordinal].slurryHandler as T })
                Capabilities.PIGMENT_HANDLER_CAPABILITY -> LazyOptional.of({ chemicalHandlers[side.ordinal].pigmentHandler as T })
                Capabilities.INFUSION_HANDLER_CAPABILITY -> LazyOptional.of({ chemicalHandlers[side.ordinal].infusionHandler as T })
                else -> super.getCapability(cap, side)
            }
        }

        return super.getCapability(cap, side)
    }
    
    val chemicalStorage: IMEMonitor<IAEChemicalStack>
        get() = getGridNode(AEPartLocation.INTERNAL)!!.grid!!.getCache<IStorageGrid>(IStorageGrid::class.java).getInventory(StorageChannels.CHEMICAL)

    protected val powerSource: IEnergySource?
        get() {
            if (node == null || node!!.grid == null) {
                return null
            }

            return ChannelPowerSrc(node, node!!.grid.getCache(IEnergyGrid::class.java))
        }
    
    fun onTankContentsChanged() {
        markDirty()
        val chemicalTankList = getChemicalTankListToSend()

        if (isReady) {
            NetworkManager.sendToAllAround(ChemicalInterfaceContentsChangedPacket(chemicalTankList), TargetPoint(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), 32.toDouble(), world))
        }
    }

    fun getChemicalTankListToSend(): ChemicalInterfaceContainer.ChemicalTankList {
        val chemicalTankList = ChemicalInterfaceContainer.ChemicalTankList(6)

        chemicalHandlers.forEachIndexed { index, mergedChemicalHandler ->
            chemicalTankList.setChemicalTank(
                index,
                mergedChemicalHandler.chemical,
                mergedChemicalHandler.currentTank?.getStored() ?: 0L,
                mergedChemicalHandler.currentTank?.getCapacity() ?: 0L
            )
        }
        return chemicalTankList
    }

    inner class MergedChemicalHandler(val index: Int): IContentsListener {
        val gasHandler: GasHandler = GasHandler()
        val slurryHandler: SlurryHandler = SlurryHandler()
        val pigmentHandler = PigmentHandler()
        val infusionHandler = InfusionHandler()
        val tank = ChemicalTankChemicalTank.create(ChemicalTankTier.ULTIMATE, this)

        override fun onContentsChanged() {
            onTankContentsChanged()
        }

        val chemical: Chemical<*>?
        get() {
            if (tank.current === MergedChemicalTank.Current.EMPTY) {
                return null
            }
            
            return tank.getTankFromCurrent(tank.current).type
        }
        
        val currentTank: IChemicalTank<*, *>?
        get() {
            if (chemical !== null) {
                return tank.getTankFromCurrent(tank.current)
            }
            
            return null
        }
        
        val classToHandlerMap = mutableMapOf<Class<*>, ChemicalHandler<*, *>>(
            Gas::class.java to gasHandler,
            Slurry::class.java to slurryHandler,
            InfuseType::class.java to infusionHandler,
            Pigment::class.java to pigmentHandler
        )
        val classToCurrentMap = mutableMapOf<Class<*>, MergedChemicalTank.Current>(
            Gas::class.java to MergedChemicalTank.Current.GAS,
            Slurry::class.java to MergedChemicalTank.Current.SLURRY,
            InfuseType::class.java to MergedChemicalTank.Current.INFUSION,
            Pigment::class.java to MergedChemicalTank.Current.PIGMENT
        )
        
        val classToTank = mutableMapOf<Class<*>, IChemicalTank<*, *>>(
            Gas::class.java to tank.gasTank,
            Slurry::class.java to tank.slurryTank,
            InfuseType::class.java to tank.infusionTank,
            Pigment::class.java to tank.pigmentTank
        )
        
        val classToEmpty = mutableMapOf<Class<*>, ChemicalStack<*>>(
            Gas::class.java to GasStack.EMPTY,
            Slurry::class.java to SlurryStack.EMPTY,
            InfuseType::class.java to InfusionStack.EMPTY,
            Pigment::class.java to PigmentStack.EMPTY
        )

        abstract inner class ChemicalHandler<CHEMICAL: Chemical<CHEMICAL>, STACK: ChemicalStack<CHEMICAL>>(val clazz: Class<*>, val chemicalStackClass: Class<*>): IChemicalHandler<CHEMICAL, STACK> {
            override fun getTanks(): Int {
                return 1
            }

            override fun getChemicalInTank(p0: Int): STACK {
                if (tank.current == MergedChemicalTank.Current.EMPTY) {
                    return classToEmpty.get(clazz) as STACK
                }
                
                return classToTank.get(clazz)!!.stack as STACK
            }

            override fun setChemicalInTank(p0: Int, p1: STACK) {
                if (tank.current !== classToCurrentMap.get(clazz)) {
                    classToTank.get(clazz)!!.stack = p1
                }
            }

            override fun getTankCapacity(p0: Int): Long {
                return classToTank.get(clazz)!!.capacity
            }

            override fun isValid(p0: Int, p1: STACK): Boolean {
                if (_chemicalConfig.getChemicalInSlot(index) != null && !_chemicalConfig.getChemicalInSlot(index)!!.equals(p1.type)) {
                    return false
                }
                
                return tank.current === classToCurrentMap.get(clazz) || p1.type === classToTank.get(clazz)!!.type
            }
            
            private fun insertChemicalIntoTank(stack: ChemicalStack<*>, action: Action, automationType: AutomationType): ChemicalStack<*> {
                return when(stack) {
                    is SlurryStack -> tank.slurryTank.insert(stack, action, automationType)
                    is GasStack -> tank.gasTank.insert(stack, action, automationType)
                    is InfusionStack -> tank.infusionTank.insert(stack, action, automationType)
                    is PigmentStack -> tank.pigmentTank.insert(stack, action, automationType)
                    else -> throw RuntimeException("Invalid chemical type")
                }
            }
            
            fun isValidForTank(stack: ChemicalStack<*>): Boolean {
                return when(stack) {
                    is SlurryStack -> tank.slurryTank.isValid(stack)
                    is GasStack -> tank.gasTank.isValid(stack)
                    is InfusionStack -> tank.infusionTank.isValid(stack)
                    is PigmentStack -> tank.pigmentTank.isValid(stack)
                    else -> throw RuntimeException("Invalid chemical type")
                }
            }

            override fun insertChemical(p0: Int, p1: STACK, p2: Action): STACK {
                if (tank.current !== classToCurrentMap.get(clazz) && p1.type !== tank.slurryTank.type && tank.current !== MergedChemicalTank.Current.EMPTY) {
                    return p1
                }

                val action = if (p2 == Action.SIMULATE) {
                    Actionable.SIMULATE
                } else {
                    Actionable.MODULATE
                }

                val chemicalTankIndexesForChemical = mutableListOf<Int>()
                _chemicalConfig.forEachIndexed { index, item ->
                    if (item != null && p1.type.equals(item)) {
                        chemicalTankIndexesForChemical.add(index)
                    }
                }

                val originalAmount = p1.amount

                var amount = p1.amount

                for (chemicalTankIndex in chemicalTankIndexesForChemical) {
                    val tank = chemicalHandlers[chemicalTankIndex]

                    if (tank.classToHandlerMap[clazz]!!.isValidForTank(Mekanism.getStackFromStack(p1, amount))) {
                        val amtNotTaken = tank.classToHandlerMap[clazz]!!.insertChemicalIntoTank(Mekanism.getStackFromStack(p1, amount), p2, AutomationType.EXTERNAL)

                        amount -= amount - amtNotTaken.amount

                        if (amount <= 0) {
                            break
                        }
                    }
                }

                if (amount <= 0) {
                    return classToEmpty.get(clazz) as STACK
                }

                if (_chemicalConfig.getChemicalInSlot(index) != null && !_chemicalConfig.getChemicalInSlot(index)!!.equals(p1.type)) {
                    return Mekanism.getStackFromStack(p1, amount) as STACK
                }

                val chemicalStack = Mekanism.getStackFromStack(p1, amount)

                val aeChemicalStack = AEChemicalStack(chemicalStack)
                
                if (powerSource !== null) {

                    val notInserted = AppEng.API!!.storage().poweredInsert(
                        powerSource,
                        chemicalStorage,
                        aeChemicalStack,
                        MachineSource(this@ChemicalInterfaceTileEntity),
                        action
                    )

                    if (notInserted == null) {
                        return classToEmpty.get(clazz) as STACK;
                    }

                    val chemicalStackNotInserted = notInserted.getChemicalStack()

                    if (chemicalStackNotInserted.getAmount() === 0L) {
                        return classToEmpty.get(clazz) as STACK;
                    }

                    return insertChemicalIntoTank(chemicalStackNotInserted, p2, AutomationType.EXTERNAL) as STACK
                }
                
                return insertChemicalIntoTank(chemicalStack, p2, AutomationType.EXTERNAL) as STACK
            }

            override fun extractChemical(p0: Int, p1: Long, p2: Action): STACK {
                return classToEmpty.get(clazz) as STACK
            }
        }
        
        inner class SlurryHandler: ChemicalHandler<Slurry, SlurryStack>(Slurry::class.java, SlurryStack::class.java), ISlurryHandler {}
        
        inner class GasHandler : ChemicalHandler<Gas, GasStack>(Gas::class.java, GasStack::class.java), IGasHandler {}
        
        inner class PigmentHandler : ChemicalHandler<Pigment, PigmentStack>(Pigment::class.java, PigmentStack::class.java), IPigmentHandler {}
        
        inner class InfusionHandler : ChemicalHandler<InfuseType, InfusionStack>(InfuseType::class.java, InfusionStack::class.java), IInfusionHandler {}
    }
}