package me.jhonlendo.altcon.bukkit.networking.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import me.jhonlendo.altcon.bukkit.boot.BukkitBoot;
import me.jhonlendo.altcon.common.networking.manager.CustomPacketHandler;
import me.jhonlendo.altcon.common.networking.packets.TPacket;
import me.jhonlendo.altcon.common.networking.packets.TPacketException;

public class WorkerServerEntranceDoor {
	
	private DataInputStream stream;
	
	private boolean run = false;
	
	private WorkerServerEntranceDoorThread entranceThread;
	
	private WorkerServer server;
	
	public WorkerServerEntranceDoor(InputStream stream, WorkerServer server) {
		this.stream = new DataInputStream(stream);
		this.entranceThread = new WorkerServerEntranceDoorThread();
		this.server = server;
	}
	
	public void startWorking() {
		this.run = true;
		this.entranceThread.start();
	}
	
	public void stop() {
		this.run = false;
		
	}
	
	// Check whether it's still running
	public boolean running() {
		return this.run;
	}
	
	public WorkerServer getWorkerServer() {
		return this.server;
	}
	
	class WorkerServerEntranceDoorThread extends Thread {
		
		/*
		 * This thread is supposed to receive all the packets and send to the packet handler(s)
		 */
		
		public void run() {
			while (run) {
				try {
					short id = stream.readShort();
					Class<? extends TPacket> constructor = BukkitBoot.getInstance().getPacketManager().getPacketClassFromID(id);
				    if (constructor == null) {
				    	server.getExitDoor().sendPacket(new TPacketException("The given packet ID (" + id + ") does not match with any of our packets. (It might probably be a desync.)")); //
				    	continue;
				    }
					TPacket packet = constructor.newInstance();
					packet.read(stream);
					BukkitBoot.getInstance().getPacketHandler().handlePacket(packet);
					for (CustomPacketHandler handler : BukkitBoot.getInstance().getPacketManager().getCustomPacketHandlers()) {
						handler.handle(packet);
					}
				} catch (IOException e) {
					// It's not necessary to catch this exception
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

}
