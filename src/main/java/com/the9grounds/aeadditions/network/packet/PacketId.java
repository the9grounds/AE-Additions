package com.the9grounds.aeadditions.network.packet;

public enum PacketId {
	FLUID_SLOT,
	FLUID_CONTAINER_SLOT,
	FLUID_INTERFACE,
	EXPORT_ORE,
	UPDATE,
	CONTAINER_UPDATE,
	TERMINAL_UPDATE_FLUID,
	TERMINAL_SELECT_FLUID,
	TERMINAL_OPEN_CONTAINER,
	STORAGE_OPEN_CONTAINER,
	STORAGE_UPDATE_FLUID,
	STORAGE_SELECT_FLUID,
	STORAGE_UPDATE_STATE,
	PART_CONFIG,
	FLUID_CRAFTER_CAPACITY,
	FLUID_CRAFTER_DROPPED_ITEM,
	FLUID_FILLER_SLOT_UPDATE,
	FLUID_FILLER_SYNC_CLIENT,
	GAS_INTERFACE,
	GAS_INTERFACE_SERVER;

	private IPacketHandlerServer handlerServer;
	private IPacketHandlerClient handlerClient;

	public void registerHandler(IPacketHandler handler) {
		if (handler instanceof IPacketHandlerServer) {
			registerHandler((IPacketHandlerServer) handler);
		}
		if (handler instanceof IPacketHandlerClient) {
			registerHandler((IPacketHandlerClient) handler);
		}
	}

	public void registerHandler(IPacketHandlerServer handlerServer) {
		this.handlerServer = handlerServer;
	}

	public void registerHandler(IPacketHandlerClient handlerClient) {
		this.handlerClient = handlerClient;
	}

	public IPacketHandlerClient getHandlerClient() {
		return handlerClient;
	}

	public IPacketHandlerServer getHandlerServer() {
		return handlerServer;
	}
}
