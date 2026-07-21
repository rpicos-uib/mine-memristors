package com.rpicos.minememristors.blockentity;

import com.rpicos.minememristors.ModBlockEntities;
import com.rpicos.minememristors.sim.Circuit;
import com.rpicos.minememristors.sim.VoltageSource;
import com.rpicos.minememristors.sim.Waveform;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class PowerSupplyBlockEntity extends ComponentBlockEntity {

	private static final double[] PRESETS_VOLTS = {1.5, 5, 9, 12, 24};

	private int presetIndex = 1;
	private VoltageSource live;

	public PowerSupplyBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.POWER_SUPPLY, pos, state);
	}

	@Override
	public void cyclePreset() {
		presetIndex = (presetIndex + 1) % PRESETS_VOLTS.length;
	}

	@Override
	public void addToCircuit(Circuit circuit, int nodeA, int nodeB) {
		live = new VoltageSource(nodeA, nodeB, Waveform.dc(PRESETS_VOLTS[presetIndex]));
		circuit.add(live);
		bindNodes(circuit, nodeA, nodeB);
	}

	@Override
	public double probeCurrent() {
		return live == null ? 0 : live.current();
	}

	@Override
	public String probeSummary() {
		return "Power Supply " + PRESETS_VOLTS[presetIndex] + " V DC";
	}
}
