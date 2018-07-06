package me.jhonlendo.altcon.common.networking.manager;

import java.util.concurrent.atomic.AtomicInteger;

public class PacketIDAssigner {
	
	private static AtomicInteger idIteration = new AtomicInteger(0);
	
	// Gets a new ID from AtomicInteger
	public static short assignID() {
		return Integer.valueOf(idIteration.incrementAndGet()).shortValue();
	}

}
