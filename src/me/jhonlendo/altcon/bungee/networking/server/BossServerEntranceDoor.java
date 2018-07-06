package me.jhonlendo.altcon.bungee.networking.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import me.jhonlendo.altcon.bungee.boot.BungeeBoot;
import me.jhonlendo.altcon.bungee.networking.workers.Worker;
import me.jhonlendo.altcon.common.networking.manager.CustomPacketHandler;
import me.jhonlendo.altcon.common.networking.packets.TPacket;
import me.jhonlendo.altcon.common.networking.packets.TPacketException;

public class BossServerEntranceDoor {
	
	private DataInputStream stream;
	
	private boolean run = false;
	
	private BossServerEntranceDoorThread entranceThread;
	
	private Worker worker;
	
	public BossServerEntranceDoor(InputStream stream, Worker worker) {
		this.stream = new DataInputStream(stream);
		this.entranceThread = new BossServerEntranceDoorThread();
		this.worker = worker;
	}
	
	public void startWorking() {
		this.run = true;
		this.entranceThread.start();
	}
	
	public void stop() {
		this.run = false;
	}
	
	public boolean isRunning() {
		return this.run;
	}
	
	class BossServerEntranceDoorThread extends Thread {
		
		/*
		 * This thread is supposed to receive all the packets and send to the packet handler
		 */
		
		public void run() {
			while (run) {
				try {
					short id = stream.readShort();
					Class<? extends TPacket> constructor = BungeeBoot.getInstance().getPacketManager().getPacketClassFromID(id);
				    if (constructor == null) {
				    	worker.getExitDoor().sendPacket(new TPacketException("The given packet ID (" + id + ") does not match with any of our packets. (It might probably be a desync.)")); //
				    	continue;
				    }
					TPacket packet = constructor.newInstance();
					packet.read(stream);
					BungeeBoot.getInstance().getPacketHandler().handlePacket(worker, packet);
					for (CustomPacketHandler handler : BungeeBoot.getInstance().getPacketManager().getCustomPacketHandlers()) {
						handler.handle(packet);
					}
				} catch (IOException e) {
					worker.getExitDoor().sendPacket(new TPacketException("We couldn't receive packet"));
				} catch (InstantiationException e) {
					worker.getExitDoor().sendPacket(new TPacketException("We couldn't init packet"));
				} catch (IllegalAccessException e) {
					worker.getExitDoor().sendPacket(new TPacketException("We couldn't init packet"));
				}
			}
		}
		
	}

}
