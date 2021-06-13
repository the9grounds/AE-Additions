package com.the9grounds.aeadditions.network.packet;

import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

public interface IPacket {
	FMLProxyPacket getPacket();

	PacketId getPacketId();
}
