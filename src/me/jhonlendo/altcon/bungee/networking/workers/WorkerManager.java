package me.jhonlendo.altcon.bungee.networking.workers;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import me.jhonlendo.altcon.bungee.boot.BungeeBoot;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class WorkerManager {
	
	// All the workers (bukkit servers) will be stored right below
	private HashMap<String, Worker> workers = new HashMap<>();
	
	// Add a worker to the pool
	public void addWorker(Worker worker) {
		this.workers.put(worker.getID(), worker);
	}
	
	// Remove a worker from the pool
    public void disconnectWorker(Worker worker) {
    	this.workers.remove(worker.getID());
    }
	
    // Get all workers in the pool
	public Worker[] getWorkers() {
		return workers.values().toArray(new Worker[0]);
	}
	
	// Check if an ID is available
	public boolean isIDAvailable(String id) {
		return this.workers.containsKey(id);
	}
	
	// Get a new Scheduled Task
	public ScheduledTask getNewDisconnectionTask(Worker worker) {
		return BungeeCord.getInstance().getScheduler().schedule(BungeeBoot.getInstance(), new Runnable() {
			public void run() {
				worker.disconnect("It's been 15 seconds or more since you sent your last 'keepAlive' packet.");
			}
		}, 15L, TimeUnit.SECONDS);
	}

}
