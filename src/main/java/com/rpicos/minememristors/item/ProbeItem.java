package com.rpicos.minememristors.item;

import com.rpicos.minememristors.blockentity.ComponentBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

/**
 * v0.1 oscilloscope probe: right-click a component to read its live voltage/current/state on the
 * action bar. A scrolling waveform HUD (rendered while the probe is held, like looking at a map)
 * is the planned next step, once there's a way to test client rendering against a real window.
 */
public class ProbeItem extends Item {
	public ProbeItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		}

		if (level.getBlockEntity(context.getClickedPos()) instanceof ComponentBlockEntity component) {
			Player player = context.getPlayer();
			if (player != null) {
				String message = String.format("%s | V=%.2fV  I=%.4fA",
						component.probeSummary(), component.probeVoltage(), component.probeCurrent());
				player.sendOverlayMessage(Component.literal(message));
			}
			return InteractionResult.SUCCESS_SERVER;
		}

		return InteractionResult.PASS;
	}
}
