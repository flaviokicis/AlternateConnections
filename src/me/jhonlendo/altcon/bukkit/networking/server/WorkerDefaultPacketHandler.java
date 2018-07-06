package me.jhonlendo.altcon.bukkit.networking.server;

import me.jhonlendo.altcon.bukkit.boot.BukkitBoot;
import me.jhonlendo.altcon.common.networking.packets.TPacket;
import me.jhonlendo.altcon.common.networking.packets.TPacketDisconnect;
import me.jhonlendo.altcon.common.networking.packets.TPacketException;
import me.jhonlendo.altcon.common.networking.packets.TPacketHandshake;

public class WorkerDefaultPacketHandler {
	
	// Handles the Worker-Side Packets
	public void handlePacket(TPacket packet) {
		if (packet instanceof TPacketHandshake) {
			// send keepAlive packets periodically
			BukkitBoot.getInstance().startKeepAliveTask();
			BukkitBoot.getInstance().getConsole().sendMessage("§aSuccesfull Handshake.");
		} else if (packet instanceof TPacketException) {
			// Whenever there is an exception sent from the master server, it'll be reported to the console.
			TPacketException exception = (TPacketException)packet;
			BukkitBoot.getInstance().getConsole().sendMessage("§cException Received from Master Server > \n" + exception.getMessage());
		} else if (packet instanceof TPacketDisconnect) {
			// Same thing with disconnection packets, it'll be reported to the console.
			TPacketDisconnect disconnect = (TPacketDisconnect)packet;
			BukkitBoot.getInstance().getConsole().sendMessage("Disconnected > \n" + disconnect.getMessage());
			if (BukkitBoot.getInstance().getWorkerServer().isRunning())
			BukkitBoot.getInstance().getWorkerServer().stop();
		}
	}

}
