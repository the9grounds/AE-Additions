package com.the9grounds.aeadditions.container

import appeng.api.config.SecurityPermissions
import appeng.api.implementations.guiobjects.IGuiItem
import appeng.api.parts.IPartHost
import appeng.util.Platform
import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.Logger
import com.the9grounds.aeadditions.helpers.ICustomNameObject
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.inventory.container.ContainerType
import net.minecraft.inventory.container.INamedContainerProvider
import net.minecraft.inventory.container.SimpleNamedContainerProvider
import net.minecraft.network.PacketBuffer
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import net.minecraftforge.common.extensions.IForgeContainerType
import net.minecraftforge.fml.network.NetworkHooks
import kotlin.reflect.KClass
import kotlin.reflect.cast

class ContainerTypeBuilder<C : AbstractContainer<C>, I : Any>(private val factory: (Int, PlayerInventory?, I) -> C, private val hostInterface: KClass<out I>) {
    private var requiredPermission: SecurityPermissions? = null
    
    private var containerType: ContainerType<C>? = null
    
    fun requirePermission(permission: SecurityPermissions): ContainerTypeBuilder<C, I> {
        requiredPermission = permission
        
        return this
    }
    
    private fun open(player: PlayerEntity, locator: Locator): Boolean {
        if (player !is ServerPlayerEntity) {
            return false;
        }
        
        val accessInterface: I = getHostFromLocator(player, locator) ?: return false
        
        if (!checkPermission(player, accessInterface)) {
            return false
        }
        
        val title = getDefaultContainerTitle(accessInterface)

        val container =
            SimpleNamedContainerProvider({ window: Int, playerInventory: PlayerInventory?, _: PlayerEntity? ->
                val c: C = factory(window, playerInventory, accessInterface)
                // Set the original locator on the opened server-side container for it to more
                // easily remember how to re-open after being closed.
                c.locator = locator
                c
            }, title!!)
        
        NetworkHooks.openGui(player, container) { packetBuffer: PacketBuffer ->
            locator.write(packetBuffer)
        }

        return true
    }

    private fun getDefaultContainerTitle(accessInterface: I): ITextComponent {
        if (accessInterface is ICustomNameObject) {
            if (accessInterface.hasCustomInventoryName) {
                return accessInterface.customInventoryName
            }
        }
        return StringTextComponent.EMPTY
    }
    
    private fun fromNetwork(windowId: Int, inventory: PlayerInventory, packetBuffer: PacketBuffer): C? {
        val host: I? = getHostFromLocator(inventory.player, Locator.read(packetBuffer))
        
        if (host != null) {

            return factory(windowId, inventory, host)
        }
        
        return null
    }

    private fun checkPermission(player: PlayerEntity, accessInterface: Any): Boolean {
        return if (requiredPermission != null) {
            Platform.checkPermissions(player, accessInterface, requiredPermission, true)
        } else true
    }

    private fun getHostFromLocator(player: PlayerEntity, locator: Locator): I? {
        if (locator.hasItemIndex()) {
            return getHostFromPlayerInventory(player, locator)
        }
        if (!locator.hasBlockPos()) {
            return null // No block was clicked
        }
        val tileEntity = player.world.getTileEntity(locator.getBlockPos())

        // The tile entity itself can host a terminal (i.e. Chest!)
        if (hostInterface.isInstance(tileEntity)) {
            return hostInterface.cast(tileEntity)
        }
        if (!locator.hasSide()) {
            return null
        }
        return if (tileEntity is IPartHost) {
            // But it could also be a part attached to the tile entity
            val partHost = tileEntity as IPartHost
            val part = partHost.getPart(locator.getSide()) ?: return null
            if (hostInterface.isInstance(part)) {
                hostInterface.cast(part)
            } else {
                Logger.debug(
                    "Trying to open a container @ %s for a %s, but the container requires %s", locator,
                    part::class.java, hostInterface
                )
                null
            }
        } else {
            // FIXME: Logging? Dont know how to obtain the terminal host
            null
        }
    }

    private fun getHostFromPlayerInventory(player: PlayerEntity, locator: Locator): I? {
        val it = player.inventory.getStackInSlot(locator.itemIndex)
        if (it.isEmpty) {
            Logger.debug(
                "Cannot open container for player %s since they no longer hold the item in slot %d", player,
                locator.hasItemIndex()
            )
            return null
        }
        if (it.item is IGuiItem) {
            val guiItem = it.item as IGuiItem
            // Optionally contains the block the item was used on to open the container
            val blockPos = if (locator.hasBlockPos()) locator.getBlockPos() else null
            val guiObject = guiItem.getGuiObject(it, locator.itemIndex, player.world, blockPos)
            if (hostInterface.isInstance(guiObject)) {
                return hostInterface.cast(guiObject)
            }
        }
        return null
    }
    
    fun build(id: String): ContainerType<C> {
        require(containerType == null) { "build was already called" }
        
        containerType = IForgeContainerType.create(this::fromNetwork)
        containerType!!.setRegistryName(AEAdditions.ID, id)
        ContainerOpener.add(containerType!!, this::open)
        
        return containerType!!
    }
}