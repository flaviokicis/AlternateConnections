package me.jhonlendo.altcon.bungee.networking.server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.LinkedBlockingQueue;

import me.jhonlendo.altcon.bungee.boot.BungeeBoot;
import me.jhonlendo.altcon.bungee.networking.workers.Worker;
import me.jhonlendo.altcon.common.networking.packets.TPacket;

public class BossServerExitDoor {

	private LinkedBlockingQueue<TPacket> sendQueue = new LinkedBlockingQueue<>();

	private Thread sendThread;

	private boolean run = false;

	private DataOutputStream stream;
	
	private Worker worker;

	public BossServerExitDoor(OutputStream stream, Worker worker) {
		this.stream = new DataOutputStream(stream);
		this.sendThread = new BossServerExitDoorThread(); // Right at the bottom of this class
		this.worker = worker;
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

	class BossServerExitDoorThread extends Thread {

		public void run() {
			while (run) {
				try {
					TPacket packet = sendQueue.poll();
					stream.writeShort(packet.ID()); // Sends PACKET ID
					packet.write(stream); // Now It's the packet job to send the rest of the data
				} catch (IOException e) {
					BungeeBoot.getInstance().getConsole().sendMessage("§cError while sending a packet:");
					e.printStackTrace();
				}
			}
		}

	}

}
