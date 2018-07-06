package me.jhonlendo.altcon.bukkit.networking.server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.LinkedBlockingQueue;

import me.jhonlendo.altcon.bukkit.boot.BukkitBoot;
import me.jhonlendo.altcon.common.networking.packets.TPacket;

public class WorkerServerExitDoor {

	private LinkedBlockingQueue<TPacket> sendQueue = new LinkedBlockingQueue<>();

	private Thread sendThread;

	private boolean run = false;

	private DataOutputStream stream;
	
	private WorkerServer server;

	public WorkerServerExitDoor(OutputStream stream, WorkerServer server) {
		this.stream = new DataOutputStream(stream);
		this.sendThread = new WorkerServerExitDoorThread(); // Right at the bottom of this class
		this.server = server;
	}

	public void startWorking() {
		this.run = true;
		this.sendThread.start();
	}

	public void stop() {
		this.run = false;
	}

	public void sendPacket(TPacket packet) {
		this.sendQueue.offer(packet);
	}
	
	public WorkerServer getWorkerServer() {
		return this.server;
	}

	class WorkerServerExitDoorThread extends Thread {

		public void run() {
			while (run) {
				try {
					TPacket packet = sendQueue.poll();
					stream.writeShort(packet.ID()); // Sends PACKET ID
					packet.write(stream); // Now It's the packet job to send the rest of the data
				} catch (IOException e) {
					BukkitBoot.getInstance().getConsole().sendMessage("§cError while sending a packet:");
					e.printStackTrace();
				}
			}
		}

	}

}
