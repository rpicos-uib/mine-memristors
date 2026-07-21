package com.rpicos.minememristors.blockentity;

import com.rpicos.minememristors.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class WireBlockEntity extends NetworkBlockEntity {

	public WireBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.WIRE, pos, state);
	}

	@Override
	public boolean isConductiveTowards(Direction direction) {
		return true;
	}
}
