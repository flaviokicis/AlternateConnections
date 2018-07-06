package me.jhonlendo.altcon.common.networking.manager;

import me.jhonlendo.altcon.common.networking.packets.TPacket;

public interface CustomPacketHandler {
	
	public void handle(TPacket packet);

}
