package me.jhonlendo.altcon.common.networking.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TPacketException extends TPacket {
	
	private String message;
	
	public TPacketException(String message) {
		this.message = message;
	}

	@Override
	public void write(DataOutputStream stream) throws IOException {
		stream.writeUTF(message);
	}

	@Override
	public void read(DataInputStream stream) throws IOException {
		this.message = stream.readUTF();
	}
	
	public String getMessage() {
		return this.message;
	}

}
