package com.rpicos.minememristors.network;

import com.rpicos.minememristors.MineMemristors;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/** Client -> server heartbeat: "I'm holding the probe and looking at this position", sent every
 *  client tick while relevant. The server drops a watch if it hasn't heard from a player in a
 *  while, so there's no separate "stop watching" message to worry about losing. */
public record ProbeWatchPayload(BlockPos pos) implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<ProbeWatchPayload> TYPE =
			new CustomPacketPayload.Type<>(MineMemristors.id("probe_watch"));

	public static final StreamCodec<RegistryFriendlyByteBuf, ProbeWatchPayload> STREAM_CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC, ProbeWatchPayload::pos,
			ProbeWatchPayload::new
	);

	@Override
	public CustomPacketPayload.Type<ProbeWatchPayload> type() {
		return TYPE;
	}
}
