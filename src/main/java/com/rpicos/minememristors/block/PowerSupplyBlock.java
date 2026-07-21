package com.rpicos.minememristors.block;

import com.mojang.serialization.MapCodec;
import com.rpicos.minememristors.blockentity.PowerSupplyBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class PowerSupplyBlock extends ComponentBlock {
	public static final MapCodec<PowerSupplyBlock> CODEC = simpleCodec(PowerSupplyBlock::new);

	public PowerSupplyBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public MapCodec<PowerSupplyBlock> codec() {
		return CODEC;
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new PowerSupplyBlockEntity(pos, state);
	}
}
