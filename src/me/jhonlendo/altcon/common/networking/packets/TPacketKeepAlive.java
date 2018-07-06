package me.jhonlendo.altcon.common.networking.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TPacketKeepAlive extends TPacket {

	/*
	 * This packet does not contain any useful information. It's only to make sure that the worker didn't crash without sending
	 * the disconnection packet.
	 */
	
	@Override
	public void write(DataOutputStream stream) throws IOException {}

	@Override
	public void read(DataInputStream stream) throws IOException {}

}
