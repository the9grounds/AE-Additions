package com.the9grounds.aeadditions.core.network.packet

import com.the9grounds.aeadditions.AEAdditions
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type
import net.minecraft.resources.ResourceLocation

interface Packet: CustomPacketPayload {
    companion object {
        fun <T: CustomPacketPayload> createType(name: String): Type<T> {
            return Type(ResourceLocation.fromNamespaceAndPath(AEAdditions.ID, name))
        }
    }
}