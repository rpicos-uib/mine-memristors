package com.rpicos.minememristors.block;

import com.mojang.serialization.MapCodec;
import com.rpicos.minememristors.blockentity.FunctionGeneratorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class FunctionGeneratorBlock extends ComponentBlock {
	public static final MapCodec<FunctionGeneratorBlock> CODEC = simpleCodec(FunctionGeneratorBlock::new);

	public FunctionGeneratorBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public MapCodec<FunctionGeneratorBlock> codec() {
		return CODEC;
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new FunctionGeneratorBlockEntity(pos, state);
	}
}
