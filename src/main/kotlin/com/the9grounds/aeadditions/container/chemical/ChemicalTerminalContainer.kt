package com.the9grounds.aeadditions.container.chemical

import appeng.api.config.Actionable
import appeng.api.config.PowerMultiplier
import appeng.api.config.SecurityPermissions
import appeng.api.networking.IGridHost
import appeng.api.networking.IGridNode
import appeng.api.networking.energy.IEnergyGrid
import appeng.api.networking.energy.IEnergySource
import appeng.api.networking.security.IActionHost
import appeng.api.networking.security.IActionSource
import appeng.api.networking.storage.IBaseMonitor
import appeng.api.storage.IMEMonitor
import appeng.api.storage.IMEMonitorHandlerReceiver
import appeng.api.storage.ITerminalHost
import appeng.api.util.AEPartLocation
import appeng.container.me.common.IClientRepo
import appeng.container.me.common.IMEInteractionHandler
import appeng.container.me.common.IncrementalUpdateHelper
import appeng.helpers.InventoryAction
import appeng.me.helpers.ChannelPowerSrc
import appeng.util.IConfigManagerHost
import com.the9grounds.aeadditions.Logger
import com.the9grounds.aeadditions.api.gas.IAEChemicalStack
import com.the9grounds.aeadditions.container.AbstractContainer
import com.the9grounds.aeadditions.container.ContainerTypeBuilder
import com.the9grounds.aeadditions.integration.appeng.AppEng
import com.the9grounds.aeadditions.integration.mekanism.Mekanism
import com.the9grounds.aeadditions.network.NetworkManager
import com.the9grounds.aeadditions.network.packets.MEInteractionPacket
import com.the9grounds.aeadditions.network.packets.MEInventoryUpdatePacket
import com.the9grounds.aeadditions.sync.gui.GuiSync
import com.the9grounds.aeadditions.util.StorageChannels
import mekanism.api.Action
import mekanism.api.chemical.ChemicalStack
import mekanism.api.chemical.IChemicalHandler
import mekanism.api.chemical.gas.GasStack
import mekanism.api.chemical.gas.IGasHandler
import mekanism.api.chemical.infuse.IInfusionHandler
import mekanism.api.chemical.infuse.InfusionStack
import mekanism.api.chemical.pigment.IPigmentHandler
import mekanism.api.chemical.pigment.PigmentStack
import mekanism.api.chemical.slurry.ISlurryHandler
import mekanism.api.chemical.slurry.SlurryStack
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.inventory.container.ContainerType
import net.minecraft.inventory.container.IContainerListener

class ChemicalTerminalContainer(
    type: ContainerType<*>,
    windowId: Int,
    playerInventory: PlayerInventory,
    private val host: ITerminalHost,
    bindPlayerInventory: Boolean
) : AbstractContainer(type, windowId, playerInventory, bindPlayerInventory, host),
    IMEMonitorHandlerReceiver<IAEChemicalStack>, IMEInteractionHandler {

//    val clientCM = ConfigManager(this)
//
//    var serverCM: IConfigManager? = null

    val updateHelper = IncrementalUpdateHelper<IAEChemicalStack>()

    var gui: IConfigManagerHost? = null
    var networkNode: IGridNode? = null
    var powerSource: IEnergySource? = null

    @GuiSync("hasPower")
    var hasPower = false

    val storageChannel = StorageChannels.CHEMICAL

    var monitor: IMEMonitor<IAEChemicalStack>? = null

    var clientRepo: IClientRepo<IAEChemicalStack>? = null

    companion object {
        val TYPE = ContainerTypeBuilder({ windowId, playerInventory, host ->
            ChemicalTerminalContainer(windowId, playerInventory!!, host)
        }, ITerminalHost::class)
            .requirePermission(SecurityPermissions.BUILD)
            .build("chemical_terminal")
    }

    constructor(windowId: Int, playerInventory: PlayerInventory, host: ITerminalHost) : this(
        TYPE,
        windowId,
        playerInventory,
        host,
        true
    ) {
    }

    init {
//        clientCM.registerSetting(Settings.SORT_BY, SortOrder.NAME)
//        clientCM.registerSetting(Settings.SORT_DIRECTION, SortDir.ASCENDING)

        var powerSource: IEnergySource? = null
        if (isServer) {
//            serverCM = host.configManager

            monitor = host.getInventory(storageChannel)

            if (monitor != null) {
                monitor!!.addListener(this, null)

                if (host is IGridHost || host is IActionHost) {
                    val node = when (host) {
                        is IGridHost -> host.getGridNode(AEPartLocation.INTERNAL)
                        is IActionHost -> host.actionableNode
                        else -> null
                    }

                    if (node != null) {
                        networkNode = node

                        powerSource = ChannelPowerSrc(networkNode, node.grid.getCache(IEnergyGrid::class.java))
                    }
                }
            } else {
                isValidContainer = false
            }
        }
        this.powerSource = powerSource
    }

    override fun removeListener(listener: IContainerListener) {
        super.removeListener(listener)

        if (listeners.isEmpty() && monitor != null) {
            monitor!!.removeListener(this)
        }
    }

    override fun onContainerClosed(player: PlayerEntity) {
        super.onContainerClosed(player)

        monitor?.removeListener(this)
    }

    override fun isValid(verificationToken: Any?): Boolean = true

    override fun detectAndSendChanges() {
        if (isServer) {
            if (monitor != host.getInventory(storageChannel)) {
                isValidContainer = false
                return
            }

            if (updateHelper.hasChanges()) {
                try {
                    val builder = MEInventoryUpdatePacket.Builder(windowId, updateHelper.isFullUpdate)

                    val storageList = monitor!!.storageList

                    if (updateHelper.isFullUpdate) {
                        builder.addFull(updateHelper, storageList)
                    } else {
                        builder.addChanges(updateHelper, storageList)
                    }

                    builder.buildAndSend(this::sendPacketToClient)
                } catch (e: Throwable) {
                    Logger.warn("Could not sync me data", e)
                }

                updateHelper.commitChanges()
            }

            updatePowerStatus()

            super.detectAndSendChanges()
        }
    }

    private fun updatePowerStatus() {
        try {
            if (networkNode != null) {
                hasPower = networkNode!!.isActive
            } else if (powerSource is IEnergyGrid) {
                hasPower = (powerSource as IEnergyGrid?)!!.isNetworkPowered
            } else {
                hasPower = powerSource!!.extractAEPower(1.0, Actionable.SIMULATE, PowerMultiplier.CONFIG) > 0.8
            }
        } catch (t: Throwable) {
            // :P
        }
    }

//    override fun updateSetting(manager: IConfigManager?, settingName: Settings?, newValue: Enum<*>?) {
//        gui?.updateSetting(manager, settingName, newValue)
//    }
//
//    override fun getConfigManager(): IConfigManager {
//        return if (isServer) {
//            serverCM!!
//        } else {
//            clientCM
//        }
//    }

    val canInteractWithGrid: Boolean
        get() = monitor != null && powerSource != null && hasPower

    override fun postChange(
        monitor: IBaseMonitor<IAEChemicalStack>?,
        change: MutableIterable<IAEChemicalStack>?,
        actionSource: IActionSource?
    ) {
        for (stack in change!!) {
            updateHelper.addChange(stack)
        }
    }

    override fun onListUpdate() {
        if (isServer) {
            updateHelper.clear()
        }
    }

    override fun handleInteraction(serial: Long, action: InventoryAction) {
        if (isClient) {
            NetworkManager.sendToServer(MEInteractionPacket(windowId, serial, action))

            return
        }

        if (!canInteractWithGrid) {
            return
        }

        val player = this.playerInventory.player as ServerPlayerEntity

        if (serial == -1L) {
            handleNetworkInteraction(player, null, action)
            
            return
        }
        
        val stack = getStackBySerial(serial) ?: return

        handleNetworkInteraction(player, stack, action)
    }
    
    private fun getStackBySerial(serial: Long): IAEChemicalStack? {
        return updateHelper.getBySerial(serial)
    }

    private fun handleNetworkInteraction(
        player: ServerPlayerEntity?,
        stack: IAEChemicalStack?,
        action: InventoryAction?
    ) {
        if (action != InventoryAction.FILL_ITEM || action != InventoryAction.EMPTY_ITEM) {
            return
        }

        val held = player!!.inventory.itemStack
        if (held.count != 1) {
            // only support stacksize 1 for now, since filled items are _usually_ not stackable
            return
        }
        
        val handler = Mekanism.capabilityFromChemicalStorageItem(held) ?: return
        
        if (action == InventoryAction.FILL_ITEM && stack != null) {
            stack.stackSize = Integer.MAX_VALUE.toLong()
            
            val maxFill = fillContainer(stack, handler) ?: return
            
            stack.stackSize = maxFill.getAmount()
            
            val canPull = AppEng.API!!.storage().poweredExtraction(powerSource, monitor, stack, mySrc, Actionable.SIMULATE)
            
            if (canPull == null || canPull.stackSize < 1) {
                return
            }
            
            val cannotFill = fillContainer(canPull, handler) ?: return
            
            if (cannotFill.getAmount() == 0L) {
                return
            }
            
            stack.stackSize = stack.stackSize - cannotFill.getAmount()
            
            val pulled = AppEng.API!!.storage().poweredExtraction(powerSource, monitor, stack, mySrc, Actionable.MODULATE)
            
            if (pulled == null || pulled.stackSize < 1) {
                Logger.warn("Unable to pull chemical from ME system even though simulation said yes")
                return
            }
            
            val notFilled = fillContainer(pulled, handler, Action.EXECUTE)
            
            if (notFilled == null || notFilled.getAmount() == pulled.stackSize) {
                Logger.warn("Unable to insert chemical into held tank, even though simulation worked, reinserting into ME system")
                
                monitor!!.injectItems(pulled, Actionable.MODULATE, mySrc)
                
                return
            }
            
            if (notFilled.getAmount() != cannotFill.getAmount()) {
                Logger.warn("Filled is different than can fill for {}", held.displayName)
            }
            
        } else if (action == InventoryAction.EMPTY_ITEM) {
            
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
        } ?: return null
    }
}