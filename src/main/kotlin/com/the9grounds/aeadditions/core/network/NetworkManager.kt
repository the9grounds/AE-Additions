package com.the9grounds.aeadditions.core.network

import appeng.core.sync.network.TargetPoint
import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.core.network.packet.BasePacket
import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.RunningOnDifferentThreadException
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.network.ServerGamePacketListenerImpl
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.network.NetworkDirection
import net.minecraftforge.network.NetworkEvent
import net.minecraftforge.network.NetworkEvent.ServerCustomPayloadLoginEvent
import net.minecraftforge.network.NetworkRegistry
import net.minecraftforge.server.ServerLifecycleHooks
import java.util.function.Supplier

object NetworkManager {
    
    var channel: ResourceLocation? = ResourceLocation(AEAdditions.ID, "network_channel")
    var clientHandler: IPacketHandler? = null
    var serverHandler: IPacketHandler? = null
    
    init {
        val ec = NetworkRegistry.ChannelBuilder.named(channel)
            .networkProtocolVersion { "1" }.clientAcceptedVersions { true }.serverAcceptedVersions { true }
            .eventNetworkChannel()

        ec.registerObject(this)

        clientHandler = DistExecutor.unsafeRunForDist({ Supplier { ClientPacketHandler } }, { Supplier { null } })
        serverHandler = try {
            ServerPacketHandler
        } catch (e: Throwable) {
            null
        }
    }
    
    fun init() {
        
    }
    
    @SubscribeEvent
    fun serverPacket(event: NetworkEvent.ClientCustomPayloadEvent) {
        if (serverHandler != null) {
            try {
                val ctx = event.source.get()
                val networkHandler = ctx.networkManager.packetListener as ServerGamePacketListenerImpl
                ctx.packetHandled = true
                ctx.enqueueWork {
                    serverHandler!!.onPacketData(networkHandler, event.payload, networkHandler.player)
                }
            } catch(idk: RunningOnDifferentThreadException) {
                
            }
        }
    }

    @SubscribeEvent
    fun clientPacket(event: NetworkEvent.ServerCustomPayloadEvent) {
        if (event is ServerCustomPayloadLoginEvent) {
            return
        }
        if (clientHandler != null) {
            try {
                val ctx = event.source.get()
                val networkHandler = ctx.networkManager.packetListener
                ctx.packetHandled = true
                ctx.enqueueWork {
                    clientHandler!!.onPacketData(networkHandler, event.payload, null)
                }
            } catch(idk: RunningOnDifferentThreadException) {

            }
        }
    }
    
    fun sendToAllAround(message: BasePacket, point: TargetPoint) {
        val server = ServerLifecycleHooks.getCurrentServer()
        
        if (server != null) {
            val packet = message.toPacket(NetworkDirection.PLAY_TO_CLIENT)
            server.playerList.broadcast(point.excluded, point.x, point.y, point.z, point.r2, point.level.dimension(), packet)
        }
    }
    
    fun sendTo(message: BasePacket, player: ServerPlayer) {
        player.connection.send(message.toPacket(NetworkDirection.PLAY_TO_CLIENT))
    }
    
    fun sendToServer(message: BasePacket) {
        Minecraft.getInstance().connection!!.send(message.toPacket(NetworkDirection.PLAY_TO_SERVER))
    }
}