package com.rpicos.minememristors.client;

import com.rpicos.minememristors.network.ProbeDataPayload;

import java.util.List;

/** Latest oscilloscope data received from the server, for the HUD to render. */
final class ProbeClientState {
	private static volatile ProbeDataPayload latest;
	private static volatile long lastReceivedAtMillis;

	private ProbeClientState() {
	}

	static void update(ProbeDataPayload payload) {
		latest = payload;
		lastReceivedAtMillis = System.currentTimeMillis();
	}

	/** Null if nothing has arrived recently (probe not pointed at a component, or data went stale). */
	static ProbeDataPayload current() {
		ProbeDataPayload payload = latest;
		if (payload == null) {
			return null;
		}
		return System.currentTimeMillis() - lastReceivedAtMillis > 1000 ? null : payload;
	}

	static List<Float> historyOrEmpty() {
		ProbeDataPayload payload = current();
		return payload == null ? List.of() : payload.history();
	}
}
