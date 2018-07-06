package me.jhonlendo.altcon.common.networking.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import me.jhonlendo.altcon.bungee.boot.BungeeBoot;

public abstract class TPacket {
	
	// Here we write to the OutputStream, getting the packet fields values and sending them to the stream
	public abstract void write(DataOutputStream stream) throws IOException;
	
	// Here we read from the InputStream, giving each field in the packet class a specific value.
	public abstract void read(DataInputStream stream) throws IOException;
	
	// That should do the work
	public int ID() {
		return BungeeBoot.getInstance().getPacketManager().getIDFromPacketClass(getClass());
	}

}
