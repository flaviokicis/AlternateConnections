package me.jhonlendo.altcon.bukkit.boot;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.jhonlendo.altcon.bukkit.commands.RetryCommand;
import me.jhonlendo.altcon.bukkit.networking.server.WorkerDefaultPacketHandler;
import me.jhonlendo.altcon.bukkit.networking.server.WorkerServer;
import me.jhonlendo.altcon.common.exceptions.PacketAlreadyExistsException;
import me.jhonlendo.altcon.common.networking.manager.PacketManager;
import me.jhonlendo.altcon.common.networking.manager.PacketsInitialiser;
import me.jhonlendo.altcon.common.networking.packets.TPacketDisconnect;
import me.jhonlendo.altcon.common.networking.packets.TPacketKeepAlive;

public class BukkitBoot extends JavaPlugin {
	
	private static BukkitBoot instance;
	
	private CommandSender console;
	
	private String ID;
	
	private int port;
	
	private WorkerServer workerServer;
	
	private PacketManager packetManager;
	
	private WorkerDefaultPacketHandler packetHandler;
	
	public void onEnable() {
		this.console = getServer().getConsoleSender();
		this.console.sendMessage("§aBooting up the Worker System.");
		instance = this;
		saveDefaultConfig();
		this.packetManager = new PacketManager();
		initPackets();
		ID = getConfig().getString("ServerID");
		port = getConfig().getInt("port");
		this.packetHandler = new WorkerDefaultPacketHandler();
		this.workerServer = new WorkerServer();
		this.workerServer.setupThread(port);
		this.workerServer.start();
		new RetryCommand(getCommand("retry"));
	}
	
	public void onDisable() {
		this.workerServer.getExitDoor().sendPacket(new TPacketDisconnect("closing server"));
		this.console.sendMessage("§cDisabling Worker System.");
	}
	
	public static BukkitBoot getInstance() {
		return instance;
	}
	
	public CommandSender getConsole() {
		return this.console;
	}
	
	public String getID() {
		return this.ID;
	}
	
	public int getPort() {
		return this.port;
	}
	
	public WorkerServer getWorkerServer() {
		return this.workerServer;
	}
	
	public PacketManager getPacketManager() {
		return this.packetManager;
	}
	
	public WorkerDefaultPacketHandler getPacketHandler() {
		return this.packetHandler;
	}
	
	public void startKeepAliveTask() {
		new BukkitRunnable() {
			public void run() {
				workerServer.getExitDoor().sendPacket(new TPacketKeepAlive());
			}
		}.runTaskTimerAsynchronously(this, 20 * 5, 20 * 5);
		// 1 second = 20 ticks
		// We want 5 seconds as a periodic task, so we put 20 * 5 (100 ticks)
	}
	
	private void initPackets() {
		try {
			PacketsInitialiser.initAllPacketsFromPackage(this, "me.jhonlendo.altcon.common.networking.packets", packetManager);
		} catch (PacketAlreadyExistsException e) {
			this.console.sendMessage("§cError while trying to init packets:");
			e.printStackTrace();
		}
	}

}
