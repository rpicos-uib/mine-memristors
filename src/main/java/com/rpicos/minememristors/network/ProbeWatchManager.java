package com.rpicos.minememristors.network;

import com.rpicos.minememristors.blockentity.ComponentBlockEntity;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Tracks which players are actively pointing the probe at a component, and streams that
 *  component's live readout to them each tick. A watch expires on its own if the client stops
 *  sending heartbeats (item put away, looked elsewhere, disconnected) - no explicit "stop" message
 *  needed. */
public final class ProbeWatchManager {
	private static final long TIMEOUT_TICKS = 40; // 2 seconds of silence drops the watch

	private record Watch(BlockPos pos, long lastSeenTick) {
	}

	private static final Map<UUID, Watch> WATCHES = new ConcurrentHashMap<>();

	private ProbeWatchManager() {
	}

	public static void heartbeat(ServerPlayer player, BlockPos pos, long currentTick) {
		WATCHES.put(player.getUUID(), new Watch(pos, currentTick));
	}

	public static void tick(ServerLevel level, long currentTick) {
		for (ServerPlayer player : level.players()) {
			Watch watch = WATCHES.get(player.getUUID());
			if (watch == null) {
				continue;
			}
			if (currentTick - watch.lastSeenTick() > TIMEOUT_TICKS) {
				WATCHES.remove(player.getUUID());
				continue;
			}
			if (level.getBlockEntity(watch.pos()) instanceof ComponentBlockEntity component) {
				List<Float> history = component.historySnapshot();
				ProbeDataPayload payload = new ProbeDataPayload(
						watch.pos(), component.probeSummary(), (float) component.probeVoltage(),
						(float) component.probeCurrent(), history);
				ServerPlayNetworking.send(player, payload);
			}
		}
	}
}
