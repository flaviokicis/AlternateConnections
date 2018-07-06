package me.jhonlendo.altcon.bungee.networking.workers;

import java.io.IOException;
import java.net.Socket;

import me.jhonlendo.altcon.bungee.networking.server.BossServerEntranceDoor;
import me.jhonlendo.altcon.bungee.networking.server.BossServerExitDoor;
import me.jhonlendo.altcon.common.networking.packets.TPacketDisconnect;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class Worker {
	
	private String id = "UNKNOWN-CLIENT";
	
	private Socket workerSocket;
	
	private BossServerExitDoor exitDoor;
	
	private BossServerEntranceDoor entranceDoor;
	
	private boolean hasIdentified = false;
	
	private ScheduledTask disconnectTask;
	
	public Worker(Socket socket) throws IOException {
		this.workerSocket = socket;
		this.exitDoor = new BossServerExitDoor(socket.getOutputStream(), this);
		this.entranceDoor = new BossServerEntranceDoor(socket.getInputStream(), this);
		this.exitDoor.startWorking();
		this.entranceDoor.startWorking();
	}
	
	// Set Worker's ID
	public void setID(String id) {
		this.id = id;
		this.hasIdentified = true;
	}
	
	// Get unique id
	public String getID() {
		return this.id;
	}
	
	public Socket getWorkerSocket() {
		return this.workerSocket;
	}
	
	// Disconnect the worker
	public void disconnect(String message) {
		getExitDoor().sendPacket(new TPacketDisconnect(message));
		leave();
	}
	
	// Close the worker's socket
	public void leave() {
		try {
			this.getExitDoor().stop();
			this.getEntranceDoor().stop();
			this.workerSocket.close();
		} catch (Exception ex) {
		}
	}
	
	// The exit door is where the packets get sent to the worker
	public BossServerExitDoor getExitDoor() {
		return this.exitDoor;
	}
	
	public BossServerEntranceDoor getEntranceDoor() {
		return this.entranceDoor;
	}
 
	// If this task completes, the worker will be disconnected because they didn't respond to the keepAlive process.
	public void setDisconnectTask(ScheduledTask task) {
		this.disconnectTask = task;
	}
	
	public ScheduledTask getDisconnectTask() {
		return this.disconnectTask;
	}
	
	// Checks if the worker has already sent 'TPacketHandshake'
	public boolean hasIdentified() {
		return this.hasIdentified;
	}

}
