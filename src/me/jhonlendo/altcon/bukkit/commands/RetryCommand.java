package me.jhonlendo.altcon.bukkit.commands;

import java.util.concurrent.TimeUnit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

import me.jhonlendo.altcon.bukkit.boot.BukkitBoot;
import me.jhonlendo.altcon.bukkit.networking.server.WorkerServer;

public class RetryCommand implements CommandExecutor {

	public RetryCommand(PluginCommand c) {
		c.setPermissionMessage("§cYou don't have permission to execute this command");
		c.setDescription("§cRetry a failed connection");
		c.setUsage("/retry");
		c.setExecutor(this);
	}
	
	private long lastRetry = System.currentTimeMillis();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// Double-Checking permission
		if (sender.hasPermission(cmd.getPermission())) {
			if (TimeUnit.MILLISECONDS.toSeconds((System.currentTimeMillis() - lastRetry)) > 60) {
				WorkerServer server = BukkitBoot.getInstance().getWorkerServer();
				if (server.isRunning()) {
					sender.sendMessage("§cThe connection is already established.");
				} else {
					// Retries the connection
					sender.sendMessage("§aRetrying connection to the master server...");
					server.setupThread(BukkitBoot.getInstance().getPort());
					server.start();
					sender.sendMessage("§aRequest sent! Please, check your console for further information.");
				}
			} else {
				sender.sendMessage("§cWait just a bit longer in order to use the 'retry'.");
			}
		} else {
			sender.sendMessage(cmd.getPermissionMessage());
		}
		return true;
	}

}
