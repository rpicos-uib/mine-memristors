package com.rpicos.minememristors.client;

import com.rpicos.minememristors.item.ProbeItem;
import com.rpicos.minememristors.network.ProbeDataPayload;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * Renders like looking at a held map: a scope box appears in the corner while the probe is in
 * either hand, showing a scrolling trace of the currently-targeted component's voltage.
 */
public class OscilloscopeHud implements HudElement {

	private static final int WIDTH = 118;
	private static final int HEIGHT = 74;
	private static final int MARGIN = 6;

	@Override
	public void extractRenderState(GuiGraphicsExtractor extractor, DeltaTracker deltaTracker) {
		Minecraft client = Minecraft.getInstance();
		LocalPlayer player = client.player;
		if (player == null || !isHoldingProbe(player)) {
			return;
		}

		ProbeDataPayload data = ProbeClientState.current();

		int x0 = MARGIN;
		int y0 = extractor.guiHeight() - HEIGHT - MARGIN;
		int x1 = x0 + WIDTH;
		int y1 = y0 + HEIGHT;

		extractor.fill(x0, y0, x1, y1, 0xC0101014);
		extractor.outline(x0, y0, WIDTH, HEIGHT, 0xFF3A3A40);

		Font font = client.font;
		if (data == null) {
			extractor.text(font, "no signal", x0 + 6, y0 + HEIGHT / 2 - 4, 0xFF808080);
			return;
		}

		List<Float> history = data.history();
		int graphX0 = x0 + 4;
		int graphY0 = y0 + 4;
		int graphX1 = x1 - 4;
		int graphY1 = y0 + HEIGHT - 24;
		int graphHeight = graphY1 - graphY0;
		int midY = graphY0 + graphHeight / 2;

		extractor.horizontalLine(graphX0, graphX1, midY, 0xFF2E4A2E);

		float maxAbs = 0.001f;
		for (float v : history) {
			maxAbs = Math.max(maxAbs, Math.abs(v));
		}

		if (history.size() >= 2) {
			int n = history.size();
			int span = graphX1 - graphX0;
			int prevX = graphX0;
			int prevY = midY - Math.round(history.get(0) / maxAbs * (graphHeight / 2f));
			for (int i = 1; i < n; i++) {
				int x = graphX0 + span * i / (n - 1);
				int y = midY - Math.round(history.get(i) / maxAbs * (graphHeight / 2f));
				extractor.fill(prevX, Math.min(prevY, y), x + 1, Math.max(prevY, y) + 1, 0xFF60E080);
				prevX = x;
				prevY = y;
			}
		}

		String reading = String.format("%.2fV  %.3fA", data.voltage(), data.current());
		extractor.text(font, reading, x0 + 4, graphY1 + 2, 0xFF60E080, false);
		extractor.text(font, data.summary(), x0 + 4, graphY1 + 12, 0xFFDDDDDD, false);
	}

	private static boolean isHoldingProbe(Player player) {
		ItemStack main = player.getMainHandItem();
		ItemStack off = player.getOffhandItem();
		return main.getItem() instanceof ProbeItem || off.getItem() instanceof ProbeItem;
	}
}
