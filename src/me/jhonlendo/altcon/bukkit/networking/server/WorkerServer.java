package me.jhonlendo.altcon.bukkit.networking.server;

import java.net.Socket;

import me.jhonlendo.altcon.bukkit.boot.BukkitBoot;

public class WorkerServer {
	
	private Thread workerThread;
	
	private Socket connectionSocket;
	
	private WorkerServerExitDoor exitDoor;
	
	private WorkerServerEntranceDoor entranceDoor;
	
	private boolean running = false;
	
	public void setupThread(int port) {
		this.workerThread = new Thread() {
			public void run() {
				try {
					connectionSocket = new Socket("127.0.0.1", port);
					entranceDoor = new WorkerServerEntranceDoor(connectionSocket.getInputStream(), WorkerServer.this);
					exitDoor = new WorkerServerExitDoor(connectionSocket.getOutputStream(), WorkerServer.this);
					running = true;
				} catch (Exception ex) {
					running = false;
					BukkitBoot.getInstance().getConsole().sendMessage("§cThere was an error while connecting to the master server:");
					ex.printStackTrace();
				}
			}
		};
	}
	
	public Socket getConnectionSocket() {
		return this.connectionSocket;
	}
	
	public void start() {
		this.running = true;
		this.workerThread.start();
		this.entranceDoor.startWorking();
		this.exitDoor.startWorking();
	}
	
	public void stop() {
		try {
			this.connectionSocket.close();
			this.running = false;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public boolean isRunning() {
		return this.running;
	}
	
	public WorkerServerExitDoor getExitDoor() {
		return this.exitDoor;
	}
	
	public WorkerServerEntranceDoor getEntranceDoor() {
		return this.entranceDoor;
	}

}
