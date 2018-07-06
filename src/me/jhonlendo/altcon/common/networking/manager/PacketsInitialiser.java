package me.jhonlendo.altcon.common.networking.manager;

import me.jhonlendo.altcon.common.exceptions.PacketAlreadyExistsException;
import me.jhonlendo.altcon.common.networking.packets.TPacket;
import me.jhonlendo.altcon.common.utils.ClassGetter;

public class PacketsInitialiser {
	
	// Gets all classes from a specific package
	public static void initAllPacketsFromPackage(Object plugin, String packageName, PacketManager packetManager) throws PacketAlreadyExistsException {
		for (Class<? extends TPacket> clazz : ClassGetter.getClassesInPackage(plugin, packageName)) {
			packetManager.registerPacket(clazz);
		}
	}

}
