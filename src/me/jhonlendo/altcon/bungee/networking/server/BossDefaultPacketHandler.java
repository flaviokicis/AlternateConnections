package me.jhonlendo.altcon.bungee.networking.server;

import me.jhonlendo.altcon.bungee.boot.BungeeBoot;
import me.jhonlendo.altcon.bungee.networking.workers.Worker;
import me.jhonlendo.altcon.common.networking.packets.TPacket;
import me.jhonlendo.altcon.common.networking.packets.TPacketDisconnect;
import me.jhonlendo.altcon.common.networking.packets.TPacketException;
import me.jhonlendo.altcon.common.networking.packets.TPacketHandshake;
import me.jhonlendo.altcon.common.networking.packets.TPacketKeepAlive;

public class BossDefaultPacketHandler {

	public void handlePacket(Worker worker, TPacket packet) {
		if ((!(packet instanceof TPacketHandshake)) && (!worker.hasIdentified())) {
			worker.getExitDoor().sendPacket(new TPacketException(
					"You haven't identified yourself yet. In order to do that, please use the 'TPacketHandshake' packet."));
		} else {
			if (packet instanceof TPacketHandshake) {
				TPacketHandshake handshake = (TPacketHandshake) packet;
				if (BungeeBoot.getInstance().getWorkerManager().isIDAvailable(handshake.getID())) {
					BungeeBoot.getInstance().getWorkerManager().addWorker(worker);
					worker.getExitDoor().sendPacket(new TPacketHandshake());
				} else {
					worker.disconnect("This ID has already been registrated. Please, check for repeated IDs.");
				}
			} else if (packet instanceof TPacketKeepAlive) {
               // Cancel the old task
               worker.getDisconnectTask().cancel();
               // Start a new 20s task
               worker.setDisconnectTask(BungeeBoot.getInstance().getWorkerManager().getNewDisconnectionTask(worker));
			} else if (packet instanceof TPacketDisconnect) {
			   worker.leave();
			} else if (packet instanceof TPacketException) {
				TPacketException exception = (TPacketException)packet;
				BungeeBoot.getInstance().getConsole().sendMessage("§cException in Worker [" + worker.getID() + "] > \n" + exception.getMessage());
			}
		}
	}

}
