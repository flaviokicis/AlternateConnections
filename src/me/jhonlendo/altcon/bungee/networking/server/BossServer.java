package me.jhonlendo.altcon.bungee.networking.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import me.jhonlendo.altcon.bungee.boot.BungeeBoot;
import me.jhonlendo.altcon.bungee.networking.workers.Worker;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class BossServer {

	private Thread mainThread;

	private ServerSocket socketServer;
	
	private boolean run = false;
	
	private Thread socketServerThread;

	// Creates a instance of ServerSocket init thread.
	public void setupServerSocket(int port) {
		this.socketServerThread = new Thread() { public void run() {
		try {
			BossServer.this.socketServer = new ServerSocket(port);
		} catch (IOException e) {
			run = false;
			BungeeBoot.getInstance().getConsole().sendMessage("§cThere was an error in the Socket Server Init:");
			e.printStackTrace();
		}}};
	}
	
	// Creates a instance without running it.
	public void setupThread() {
		mainThread = new Thread() {
			public void run() {
				while (run) {
                try {
					Socket client = socketServer.accept();
					Worker worker = new Worker(client);
					// If the worker don't send any 'keepAlive' packet within 20 seconds, it'll be disconnected.
					// (This avoids crashes)
					worker.setDisconnectTask(BungeeBoot.getInstance().getWorkerManager().getNewDisconnectionTask(worker));
				} catch (IOException e) {
					run = false;
					BungeeBoot.getInstance().getConsole().sendMessage("§cThere was an error while accepting a client:");
					e.printStackTrace();
				}
				}
			}
		};
	}

	// Starts all the threads
	public void start() {
		this.run = true;
		this.socketServerThread.run();
		mainThread.run();
	}
	
	public boolean isRunning() {
		return this.run;
	}
	
	// Setting run to false will stop the threads' 'while loops'
	public void stop() {
		this.run = false;
	    for (Worker worker : BungeeBoot.getInstance().getWorkerManager().getWorkers()) {
	    	worker.disconnect("The Master Server has been disabled.");
	    }
	}

}
