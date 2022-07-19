package com.the9grounds.aeadditions.core.network.packet

import com.the9grounds.aeadditions.menu.MEWirelessTransceiverMenu
import com.the9grounds.aeadditions.registries.Capability
import com.the9grounds.aeadditions.util.ChannelInfo
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.minecraftforge.server.ServerLifecycleHooks
import java.util.*

class DeleteChannelPacket : BasePacket {
    var id = ""
    constructor(packet: FriendlyByteBuf) {
        val nbt = packet.readNbt()
        id = nbt!!.getString("id")
    }
    
    constructor(channelInfo: ChannelInfo) {
        val data = this.getInitialData()
        
        val nbt = CompoundTag()
        nbt.putString("id", channelInfo.id.toString())
        data.writeNbt(nbt)
        
        configureWrite(data)
    }

    override fun serverPacketData(player: Player) {
        
        val level = player.level

        val channelHolder = level.getCapability(Capability.CHANNEL_HOLDER).resolve().get()
        
        val channelInfo = channelHolder.getChannelById(UUID.fromString(id))
        
        if (channelInfo == null || !channelInfo.hasAccessTo(player as ServerPlayer)) {
            return
        }
        
        channelHolder.removeChannel(channelInfo)
        
        val containerMenu = player.containerMenu
        
        if (containerMenu !is MEWirelessTransceiverMenu) {
            return
        }
        
        val server = ServerLifecycleHooks.getCurrentServer()

        server?.playerList?.players?.forEach {
            val innerContainerMenu = it.containerMenu
            if (innerContainerMenu is MEWirelessTransceiverMenu && innerContainerMenu.blockEntity!! == containerMenu.blockEntity) {
                innerContainerMenu.sendChannelStuffToClient()
            }
        }
    }
}