package com.rpicos.minememristors.blockentity;

import com.rpicos.minememristors.ModBlockEntities;
import com.rpicos.minememristors.sim.Circuit;
import com.rpicos.minememristors.sim.VoltageSource;
import com.rpicos.minememristors.sim.Waveform;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class FunctionGeneratorBlockEntity extends ComponentBlockEntity {

	private record Preset(String name, Waveform waveform) {
	}

	private static final Preset[] PRESETS = {
			new Preset("sine 5V 1Hz", Waveform.sine(5, 1, 0, 0)),
			new Preset("square 5V 1Hz", Waveform.square(5, 1, 0)),
			new Preset("triangle 5V 1Hz", Waveform.triangle(5, 1, 0)),
			new Preset("sine 5V 5Hz", Waveform.sine(5, 5, 0, 0)),
	};

	private int presetIndex = 0;
	private VoltageSource live;

	public FunctionGeneratorBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.FUNCTION_GENERATOR, pos, state);
	}

	@Override
	public void cyclePreset() {
		presetIndex = (presetIndex + 1) % PRESETS.length;
	}

	@Override
	public void addToCircuit(Circuit circuit, int nodeA, int nodeB) {
		live = new VoltageSource(nodeA, nodeB, PRESETS[presetIndex].waveform());
		circuit.add(live);
		bindNodes(circuit, nodeA, nodeB);
	}

	@Override
	public double probeCurrent() {
		return live == null ? 0 : live.current();
	}

	@Override
	public String probeSummary() {
		return "Function Generator: " + PRESETS[presetIndex].name();
	}
}
