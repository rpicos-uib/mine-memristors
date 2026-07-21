package com.rpicos.minememristors.client;

import com.rpicos.minememristors.MineMemristors;
import com.rpicos.minememristors.item.ProbeItem;
import com.rpicos.minememristors.network.ProbeDataPayload;
import com.rpicos.minememristors.network.ProbeWatchPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class MineMemristorsClient implements ClientModInitializer {
	private static final Identifier OSCILLOSCOPE_HUD_ID = MineMemristors.id("oscilloscope");

	@Override
	public void onInitializeClient() {
		HudElementRegistry.addLast(OSCILLOSCOPE_HUD_ID, new OscilloscopeHud());

		ClientPlayNetworking.registerGlobalReceiver(ProbeDataPayload.TYPE,
				(payload, context) -> ProbeClientState.update(payload));

		ClientTickEvents.END_CLIENT_TICK.register(MineMemristorsClient::sendWatchHeartbeatIfNeeded);
	}

	private static void sendWatchHeartbeatIfNeeded(Minecraft client) {
		LocalPlayer player = client.player;
		if (player == null || client.getConnection() == null) {
			return;
		}

		ItemStack main = player.getMainHandItem();
		ItemStack off = player.getOffhandItem();
		if (!(main.getItem() instanceof ProbeItem) && !(off.getItem() instanceof ProbeItem)) {
			return;
		}

		if (client.hitResult instanceof BlockHitResult blockHit && blockHit.getType() == HitResult.Type.BLOCK) {
			ClientPlayNetworking.send(new ProbeWatchPayload(blockHit.getBlockPos()));
		}
	}
}
