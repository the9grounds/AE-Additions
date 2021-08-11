package com.the9grounds.aeadditions.network

import appeng.core.sync.network.TargetPoint
import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.network.handler.ClientPacketHandler
import com.the9grounds.aeadditions.network.handler.IPacketHandler
import com.the9grounds.aeadditions.network.handler.ServerPacketHandler
import com.the9grounds.aeadditions.network.packets.BasePacket
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.network.ThreadQuickExitException
import net.minecraft.network.play.ServerPlayNetHandler
import net.minecraft.server.MinecraftServer
import net.minecraft.util.ResourceLocation
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.fml.LogicalSide
import net.minecraftforge.fml.LogicalSidedProvider
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent
import net.minecraftforge.fml.network.NetworkRegistry
import java.util.function.Supplier

object NetworkManager {
    
    val channel = ResourceLocation(AEAdditions.ID, "main")
    
    val clientPacketHandler: IPacketHandler?
    val serverPacketHandler: IPacketHandler?
    
    fun init() {
        // Get that money
    }
    
    init {
        val ec = NetworkRegistry.ChannelBuilder.named(channel)
            .clientAcceptedVersions { true }
            .serverAcceptedVersions { true }
            .networkProtocolVersion { "1.0" }
            .eventNetworkChannel()
        
        ec.registerObject(this)

        clientPacketHandler = DistExecutor.unsafeRunForDist({ Supplier { ClientPacketHandler() } }, { Supplier { null } })
        
        serverPacketHandler = try {
            ServerPacketHandler()
        } catch (e: Throwable) {
            null
        }
    }
    
    @SubscribeEvent
    fun serverPacket(event: NetworkEvent.ClientCustomPayloadEvent) {
        if (this.serverPacketHandler != null) {
            try {
                val ctx = event.source.get()
                val netHandler = ctx.networkManager.netHandler as ServerPlayNetHandler
                ctx.packetHandled = true
                val packet = event.payload as AEAPacketBuffer
                ctx.enqueueWork { this.serverPacketHandler.onPacketData(netHandler, packet,  netHandler.player) }
            } catch (e: ThreadQuickExitException) {
                //
            }
        }
    }
    
    @SubscribeEvent
    fun clientPacket(event: NetworkEvent.ServerCustomPayloadEvent) {
        if (event is NetworkEvent.ServerCustomPayloadLoginEvent) {
            return
        }
        
        if (this.clientPacketHandler != null) {
            try {
                val ctx = event.source.get()
                val netHandler = ctx.networkManager.netHandler
                val packet = event.payload as AEAPacketBuffer
                ctx.enqueueWork { this.clientPacketHandler.onPacketData(netHandler, packet, null) }
            } catch (e: ThreadQuickExitException) {
                //
            }
        }
    }

    fun sendToAll(message: BasePacket) {
        getServer()!!.playerList.sendPacketToAllPlayers(message.toPacket(NetworkDirection.PLAY_TO_CLIENT))
    }

    fun sendTo(message: BasePacket, player: ServerPlayerEntity) {
        player.connection.sendPacket(message.toPacket(NetworkDirection.PLAY_TO_CLIENT))
    }

    fun sendToAllAround(message: BasePacket, point: TargetPoint) {
        val pkt = message.toPacket(NetworkDirection.PLAY_TO_CLIENT)
        getServer()!!.playerList.sendToAllNearExcept(
            point.excluded, point.x, point.y, point.z, point.r2,
            point.world.dimensionKey, pkt
        )
    }
    
    fun sendToServer(message: BasePacket) {
        Minecraft.getInstance().connection!!.sendPacket(message.toPacket(NetworkDirection.PLAY_TO_SERVER))
    }

    private fun getServer(): MinecraftServer? {
        return LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER)
    }
}