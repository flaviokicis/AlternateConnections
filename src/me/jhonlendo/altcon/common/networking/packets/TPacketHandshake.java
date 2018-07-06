package me.jhonlendo.altcon.common.networking.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TPacketHandshake extends TPacket {
	
	private String id;
	
	
	public TPacketHandshake(String id) {
		this.id = id;
	}
	
	/* The Handshake packet without a message
	 * is mostly used by the Boss Server to tell
	 * the Worker that the Handshake was a success
	*/
	public TPacketHandshake() {
	}

	@Override
	public void write(DataOutputStream stream) throws IOException {
		stream.writeUTF(this.id);
	}

	@Override
	public void read(DataInputStream stream) throws IOException {
		this.id = stream.readUTF();
	}
	
	public String getID() {
		return this.id;
	}

}
