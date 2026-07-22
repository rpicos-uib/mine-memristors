package com.rpicos.circuitcraft.block;

import com.mojang.serialization.MapCodec;
import com.rpicos.circuitcraft.blockentity.AcSourceBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class AcSourceBlock extends ComponentBlock {
	public static final MapCodec<AcSourceBlock> CODEC = simpleCodec(AcSourceBlock::new);

	public AcSourceBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public MapCodec<AcSourceBlock> codec() {
		return CODEC;
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new AcSourceBlockEntity(pos, state);
	}
}
