package me.jhonlendo.altcon.bungee.boot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.google.common.io.ByteStreams;

import me.jhonlendo.altcon.bungee.networking.server.BossDefaultPacketHandler;
import me.jhonlendo.altcon.bungee.networking.server.BossServer;
import me.jhonlendo.altcon.bungee.networking.workers.WorkerManager;
import me.jhonlendo.altcon.common.exceptions.PacketAlreadyExistsException;
import me.jhonlendo.altcon.common.networking.manager.PacketManager;
import me.jhonlendo.altcon.common.networking.manager.PacketsInitialiser;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class BungeeBoot extends Plugin {
	
	private CommandSender console;
	
	private static BungeeBoot instance;
	
	private PacketManager packetManager;
	
	private WorkerManager workerManager;
	
	private BossDefaultPacketHandler packetHandler;
	
	private BossServer server;
	
	private int port;
	
	// Starts the server
	public void onEnable() {
		this.console = getProxy().getConsole();
		this.console.sendMessage("§aBooting Up Boss Server.");
		// Set the instance
		instance = this;
		this.packetManager = new PacketManager();
		// Config in which the port can be changed
		this.createConfigFile();
		try {
			Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "configuration.yml"));
		    this.port = configuration.getInt("port");
		} catch (Exception e) {
			this.console.sendMessage("§cThere was an error while trying to retrieve config info:");
			e.printStackTrace();
		}
		// Get all the packets from the main package and load them
		initPackets();
		this.packetHandler = new BossDefaultPacketHandler();
		this.workerManager = new WorkerManager();
		this.server = new BossServer();
		this.server.setupServerSocket(port);
		this.server.setupThread();
		this.server.start();
	}
	
	// This method is invoked when the server is being disabled
	public void onDisable() {
		// Kills the server
		this.server.stop();
		this.console.sendMessage("§cDisabling Boss Server.");
	}
	
	public CommandSender getConsole() {
		return this.console;
	}
	
	// Gets an instance of this class
	public static BungeeBoot getInstance() {
		return instance;
	}
	
	// Gets the packet manager
	public PacketManager getPacketManager() {
		return this.packetManager;
	}
	
	public BossDefaultPacketHandler getPacketHandler() {
		return this.packetHandler;
	}
	
	public WorkerManager getWorkerManager() {
		return this.workerManager;
	}
	
	private void initPackets() {
		try {
			PacketsInitialiser.initAllPacketsFromPackage(this, "me.jhonlendo.altcon.common.networking.packets", packetManager);
		} catch (PacketAlreadyExistsException e) {
			this.console.sendMessage("§cError while trying to init packets:");
			e.printStackTrace();
		}
	}
	
	// Create a new config file If there is none
    private void createConfigFile() {
    	if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        File configFile = new File(getDataFolder(), "configuration.yml");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                try (InputStream is = getResourceAsStream("configuration.yml");
                     OutputStream os = new FileOutputStream(configFile)) {
                    ByteStreams.copy(is, os);
                }
            } catch (IOException e) {
                throw new RuntimeException("Unable to create configuration file", e);
            }
        }
    }

}
