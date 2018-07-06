package me.jhonlendo.altcon.common.networking.manager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import me.jhonlendo.altcon.common.exceptions.PacketAlreadyExistsException;
import me.jhonlendo.altcon.common.networking.packets.TPacket;

public class PacketManager {
	
	private HashMap<Short, Class<? extends TPacket>> packetSet = new HashMap<>();
	
	private final HashSet<CustomPacketHandler> handlers = new HashSet<>();
	
	// Register a new packet with the very next available ID
	public void registerPacket(Class<? extends TPacket> packet) throws PacketAlreadyExistsException {
		if (isOperating(packet))
			throw new PacketAlreadyExistsException("Packet already exists");
		// If there is no errors, just go ahead and execute the next piece of code
		short id = PacketIDAssigner.assignID();
		packetSet.put(id, packet);
	}
	
	public void registerPacket(Class<? extends TPacket> packet, short id) throws PacketAlreadyExistsException {
		if (isOperating(packet))
			throw new PacketAlreadyExistsException("Packet already exists");
		if (this.packetSet.containsKey(id)) {
			// Can't override an existing packet
			throw new PacketAlreadyExistsException("Packet already exists");
		}
		// If there is no errors, just go ahead and execute the next piece of code
		packetSet.put(id, packet);
	}
	
	// It might be null If the defined id does not correspond to any packet
	public Class<? extends TPacket> getPacketClassFromID(short id) {
		return packetSet.get(id);
	}
	
	// Returns -1 If the packet does not match any ID in the map. It shouldn't ever return -1.
	public short getIDFromPacketClass(Class<? extends TPacket> packet) {
		for (Map.Entry<Short, Class<? extends TPacket>> entry : packetSet.entrySet()) {
			if (entry.getValue().equals(packet)) {
				return entry.getKey();
			}
		}
		return -1;
	}
	
	private boolean isOperating(Class<? extends TPacket> packet) {
		return packetSet.containsValue(packet);
	}
	
	public void addCustomPacketHandler(CustomPacketHandler handler) {
		this.handlers.add(handler);
	}
	
	public void remCustomPacketHandler(CustomPacketHandler handler) {
		this.handlers.remove(handler);
	}
	
	public CustomPacketHandler[] getCustomPacketHandlers() {
		return this.handlers.toArray(new CustomPacketHandler[0]);
	}
	
}
