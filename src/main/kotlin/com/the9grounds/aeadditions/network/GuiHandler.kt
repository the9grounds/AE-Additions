package com.the9grounds.aeadditions.network

import appeng.api.config.SecurityPermissions
import appeng.api.networking.energy.IEnergyGrid
import appeng.api.networking.security.IActionHost
import appeng.api.networking.security.ISecurityGrid
import appeng.api.parts.IPart
import appeng.api.parts.IPartHost
import appeng.api.storage.IMEMonitor
import appeng.api.util.AEPartLocation
import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.api.IPortableGasStorageCell
import com.the9grounds.aeadditions.api.IWirelessGasTermHandler
import com.the9grounds.aeadditions.api.gas.IAEGasStack
import com.the9grounds.aeadditions.block.IGuiBlock
import com.the9grounds.aeadditions.container.gas.ContainerGasStorage
import com.the9grounds.aeadditions.gui.GuiStorage
import com.the9grounds.aeadditions.integration.mekanism.gas.MEMonitorFluidGasWrapper
import com.the9grounds.aeadditions.part.PartECBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.IGuiHandler
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

object GuiHandler: IGuiHandler {

    var hand: EnumHand? = null;

    var temp = arrayOf<Any>()

    fun getContainer(id: Int, player: EntityPlayer, args: Array<Any>): Any? {
        return when(id) {
            4 -> {
                val gasInventory = MEMonitorFluidGasWrapper(args[0] as IMEMonitor<IAEGasStack>)
                ContainerGasStorage(gasInventory, player, hand)
            }
            5 -> {
                val gasInventory = MEMonitorFluidGasWrapper(args[0] as IMEMonitor<IAEGasStack>)
                val handler = args[1] as IWirelessGasTermHandler
                ContainerGasStorage(gasInventory, player, handler, hand)
            }
            6 -> {
                val gasInventory = MEMonitorFluidGasWrapper(args[0] as IMEMonitor<IAEGasStack>)
                val storageCell = args[1] as IPortableGasStorageCell
                ContainerGasStorage(gasInventory, player, storageCell, hand)
            }
            else -> {
                null
            }
        }
    }

    @SideOnly(Side.CLIENT)
    fun getGui(id: Int, player: EntityPlayer): Any? {
        return when(id) {
            4 -> GuiStorage(ContainerGasStorage(player, hand), "com.the9grounds.aeadditions.part.gas.terminal.name");
            5 -> GuiStorage(ContainerGasStorage(player, hand), "com.the9grounds.aeadditions.part.gas.terminal.name");
            6 -> GuiStorage(ContainerGasStorage(player, hand), "com.the9grounds.aeadditions.item.storage.gas.portable.name");
            else -> null
        }
    }

    @JvmStatic fun getGuiId(guiId: Int) = guiId + 6

    @JvmStatic fun getGuiId(part: PartECBase) = part.facing.ordinal

    @JvmStatic fun getPartContainer(side: EnumFacing, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any {
        return ((world.getTileEntity(BlockPos(x, y, z)) as IPartHost).getPart(side) as PartECBase).getServerGuiElement(player)
    }

    @JvmStatic fun getPartGui(side: EnumFacing, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any {
        return ((world.getTileEntity(BlockPos(x, y, z)) as IPartHost).getPart(side) as PartECBase).getClientGuiElement(player)
    }

    @JvmStatic fun launchGui(id: Int, player: EntityPlayer, hand: EnumHand, args: Array<Any>) {
        temp = args
        this.hand = hand
        player.openGui(AEAdditions, id, player.entityWorld, player.posX.toInt(), player.posY.toInt(), player.posZ.toInt())
    }

    @JvmStatic fun launchGui(id: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any {
        return player.openGui(AEAdditions, id, world, x, y, z)
    }

    @JvmStatic fun hasPermissions(pos: BlockPos, side: AEPartLocation, player: EntityPlayer): Boolean {
        val world = player.entityWorld
        val tileEntity: TileEntity? = world.getTileEntity(pos)

        if (tileEntity == null) {
            return true
        } else if (tileEntity is IGuiProvider) {
            return securityCheck(tileEntity, player)
        } else if (tileEntity is IPartHost) {
            val part: IPart? = tileEntity.getPart(side)

            if (part != null) {
                return securityCheck(part, player)
            }
        }
        return false
    }

    private fun securityCheck(tileEntity: Any, player: EntityPlayer): Boolean {
        if (tileEntity is IActionHost) {
            val actionableNode = tileEntity.actionableNode

            if (actionableNode != null) {
                val grid = actionableNode.grid

                val requirePower = false
                if (requirePower) {
                    val energyGrid = grid.getCache<IEnergyGrid>(IEnergyGrid::class.java)
                    if (!energyGrid.isNetworkPowered) {
                        return false
                    }
                }

                val securityGrid = grid.getCache<ISecurityGrid>(ISecurityGrid::class.java)

                if (securityGrid.hasPermission(player, SecurityPermissions.BUILD)) {
                    return true
                }
            }

            return false
        }

        return true
    }

    override fun getServerGuiElement(id: Int, player: EntityPlayer?, world: World?, x: Int, y: Int, z: Int): Any? {
        if (player == null) {
            return null
        }

        val container = getContainerBlockElement(player, world, x, y, z)

        if (container != null) {
            return container
        }

        var side: EnumFacing? = null

        if (id <= 5) {
            side = EnumFacing.VALUES[id]
        }

        val pos = BlockPos(x, y, z)

        val tileEntity = world?.getTileEntity(pos)

        if (tileEntity == null) {
            if (id >= 6) {
                return getContainer(id - 6, player, temp)
            }

            return null
        } else if (tileEntity is IGuiProvider) {
            return tileEntity.getServerGuiElement(player)
        } else if (tileEntity is IPartHost) {
            if (world != null && side != null) {
                return getPartContainer(side, player, world, x, y, z)
            }

            if (id >= 6) {
                return getContainer(id - 6, player, temp)
            }
        }

        return null
    }

    override fun getClientGuiElement(id: Int, player: EntityPlayer?, world: World?, x: Int, y: Int, z: Int): Any? {

        if (player == null) {
            return null
        }

        val gui = getGuiBlockElement(player, world, x, y, z)

        if (gui != null) {
            return gui
        }

        var side: EnumFacing? = null

        if (id <= 5) {
            side = EnumFacing.VALUES[id]
        }

        val pos = BlockPos(x, y, z)

        val tileEntity = world?.getTileEntity(pos)

        if (tileEntity == null) {
            if (id >= 6) {
                return getGui(id - 6, player)
            }

            return null
        } else if (tileEntity is IGuiProvider) {
            return tileEntity.getClientGuiElement(player)
        } else if (tileEntity is IPartHost) {
            if (world != null && side != null) {
                return getPartGui(side, player, world, x, y, z)
            }

            if (id >= 6) {
                return getGui(id - 6, player)
            }
        }

        return null
    }

    fun getGuiBlockElement(player: EntityPlayer?, world: World?, x: Int, y: Int, z: Int): Any? {
        if (world == null || player == null) {
            return null
        }
        val pos = BlockPos(x, y, z)
        val block = world.getBlockState(pos).block ?: return null

        return when(block) {
            is IGuiBlock -> block.getClientGuiElement(player, world, pos)
            else -> null
        }
    }

    fun getContainerBlockElement(player: EntityPlayer?, world: World?, x: Int, y: Int, z: Int): Any? {
        if (world == null || player == null) {
            return null
        }
        val pos = BlockPos(x, y, z)
        val block = world.getBlockState(pos).block ?: return null

        return when(block) {
            is IGuiBlock -> block.getServerGuiElement(player, world, pos)
            else -> null
        }
    }
}