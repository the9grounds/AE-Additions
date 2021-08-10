package com.the9grounds.aeadditions.container

import appeng.api.util.AEPartLocation
import com.the9grounds.aeadditions.parts.AEABasePart
import io.netty.handler.codec.DecoderException
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemUseContext
import net.minecraft.network.PacketBuffer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.Direction
import net.minecraft.util.Hand
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class Locator private constructor(private val type: Type, val itemIndex: Int, val worldId: ResourceLocation?, private val blockPos: BlockPos?, private val side: AEPartLocation?) {
    private enum class Type {
        PLAYER_INVENTORY,
        PLAYER_INVENTORY_WITH_BLOCK_CONTEXT,
        BLOCK,
        PART
    }
    
    private constructor(type: Type, itemIndex: Int, world: World, blockPos: BlockPos?, side: AEPartLocation?): this(type, itemIndex, world.dimensionKey.location, blockPos, side) {
        
    }
    
    companion object {
        fun forTileEntity(tileEntity: TileEntity): Locator {
            if (tileEntity.world == null) {
                throw IllegalArgumentException("Tile Entity does not have world")
            }
            
            return Locator(Type.BLOCK, -1, tileEntity.world!!, tileEntity.pos, null)
        }
        
        fun forTileEntitySide(tileEntity: TileEntity, side: Direction): Locator {
            if (tileEntity.world == null) {
                throw IllegalArgumentException("Tile Entity does not have world")
            }
            
            return Locator(Type.PART, -1, tileEntity.world!!, tileEntity.pos, AEPartLocation.fromFacing(side))
        }
        
        fun forItemUseContext(context: ItemUseContext): Locator {
            val player = context.player ?: throw IllegalArgumentException("Cannot open container without player")

            val slot = getPlayerInventorySlotFromHand(player, context.hand)
            
            val side = AEPartLocation.fromFacing(context.face)
            
            return Locator(Type.PLAYER_INVENTORY_WITH_BLOCK_CONTEXT, slot, player.world, context.pos, side)
        }
        
        fun forHand(playerEntity: PlayerEntity, hand: Hand): Locator {
            val slot = getPlayerInventorySlotFromHand(playerEntity, hand)
            
            return Locator(Type.PLAYER_INVENTORY, slot, playerEntity.world, null, null)
        }
        
        fun forPart(part: AEABasePart): Locator {
            val host = part.host
            val pos = host!!.location
            
            return Locator(Type.PART, -1, pos.world, pos.blockPos, part.side)
        }
        
        private fun getPlayerInventorySlotFromHand(playerEntity: PlayerEntity, hand: Hand): Int {
            val itemStack = playerEntity.getHeldItem(hand)
            
            if (itemStack.isEmpty) {
                throw IllegalArgumentException("Cannot open an item-inventory with empty hands")
            }
            
            val invSize = playerEntity.inventory.sizeInventory
            
            for (i in 0 until invSize) {
                if (playerEntity.inventory.getStackInSlot(i) == itemStack) {
                    return i
                }
            }
            
            throw IllegalArgumentException("Could not find item held in hand $hand in player inventory")
        }

        fun read(packetBuffer: PacketBuffer): Locator {
            return when(val type = packetBuffer.readByte().toInt()) {
                0 -> Locator(Type.PLAYER_INVENTORY, packetBuffer.readInt(), null as ResourceLocation, null, null)
                1 -> Locator(Type.PLAYER_INVENTORY_WITH_BLOCK_CONTEXT, packetBuffer.readInt(), packetBuffer.readResourceLocation(), packetBuffer.readBlockPos(), AEPartLocation.fromOrdinal(packetBuffer.readByte().toInt()))
                2 -> Locator(Type.BLOCK, -1, packetBuffer.readResourceLocation(), packetBuffer.readBlockPos(), null)
                3 -> Locator(Type.PART, -1, packetBuffer.readResourceLocation(), packetBuffer.readBlockPos(), AEPartLocation.fromOrdinal(packetBuffer.readByte().toInt()))
                else -> throw DecoderException("Locator type out of range: $type")
            }
        }
    }
    
    fun hasBlockPos(): Boolean {
        return when (type) {
            Type.BLOCK, Type.PART, Type.PLAYER_INVENTORY_WITH_BLOCK_CONTEXT -> true
            else -> false
        }
    }
    
    fun hasSide(): Boolean {
        return when(type) {
            Type.PART, Type.PLAYER_INVENTORY_WITH_BLOCK_CONTEXT -> true
            else -> false
        }
    }
    
    fun hasItemIndex(): Boolean {
        return when(type) {
            Type.PLAYER_INVENTORY, Type.PLAYER_INVENTORY_WITH_BLOCK_CONTEXT -> true
            else -> false
        }
    }
    
    fun getSide(): AEPartLocation {
        require(hasSide())
        return side!!
    }
    
    fun getBlockPos(): BlockPos {
        require(hasBlockPos())
        return blockPos!!
    }
    
    fun write(packetBuffer: PacketBuffer) {
        when(type) {
            Type.PLAYER_INVENTORY -> {
                packetBuffer.writeByte(0)
                packetBuffer.writeInt(itemIndex)
            }
            Type.PLAYER_INVENTORY_WITH_BLOCK_CONTEXT -> {
                packetBuffer.writeByte(1)
                packetBuffer.writeInt(itemIndex)
                packetBuffer.writeResourceLocation(worldId!!)
                packetBuffer.writeBlockPos(blockPos!!)
                packetBuffer.writeByte(side!!.ordinal)
            }
            Type.BLOCK -> {
                packetBuffer.writeByte(2)
                packetBuffer.writeResourceLocation(worldId!!)
                packetBuffer.writeBlockPos(blockPos!!)
            }
            Type.PART -> {
                packetBuffer.writeByte(3)
                packetBuffer.writeResourceLocation(worldId!!)
                packetBuffer.writeBlockPos(blockPos!!)
                packetBuffer.writeByte(side!!.ordinal)
            }
            else -> throw IllegalStateException("Invalid Locator type $type")
        }
    }

    override fun toString(): String {
        val result = StringBuilder(type.name)
        result.append("{")
        if (hasItemIndex()) {
            result.append("slot=").append(itemIndex).append(',')
        }
        if (hasBlockPos()) {
            result.append("dim=").append(worldId).append(',')
            result.append("pos=").append(blockPos).append(',')
        }
        if (hasSide()) {
            result.append("side=").append(side).append(',')
        }
        if (result[result.length - 1] == ',') {
            result.setLength(result.length - 1)
        }
        result.append('}')
        
        return result.toString()
    }
}